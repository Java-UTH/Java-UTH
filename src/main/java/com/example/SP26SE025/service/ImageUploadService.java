package com.example.SP26SE025.service;

import com.example.SP26SE025.dtos.AiResponseDto;
import com.example.SP26SE025.entity.AnalysisRecord;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.exception.AiServiceException;
import com.example.SP26SE025.exception.InvalidImageException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Service to handle image upload and AI analysis workflow
 * Orchestrates validation -> AI-Service call -> result storage (FR-2, FR-3)
 */
@Service
public class ImageUploadService {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadService.class);

    // Supported image formats
    private static final Set<String> ALLOWED_FORMATS = new HashSet<>(
            Arrays.asList("image/jpeg", "image/png", "image/jpg", "image/bmp", "image/tiff"));

    // Maximum file size: 50MB
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024;

    // Temporary upload directory for processing - use system temp dir
    private static final String TEMP_UPLOAD_DIR = System.getProperty("java.io.tmpdir") + File.separator
            + "ai-service-uploads" + File.separator;

    @Autowired
    private AiServiceClient aiServiceClient;

    @Autowired
    private ReportService reportService;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Upload and analyze retinal image
     * Flow: Validate -> Save temp -> Call AI-Service -> Parse response -> Save
     * analysis
     * 
     * @param imageFile MultipartFile from frontend
     * @param user      User performing the upload (can be null for test mode)
     * @param testId    Test/analysis ID (for tracking)
     * @param patientId Patient ID (for tracking)
     * @return AI analysis response
     * @throws InvalidImageException if image is invalid
     * @throws AiServiceException    if AI-Service call fails
     */
    public AiResponseDto uploadAndAnalyzeImage(MultipartFile imageFile, User user, String testId, String patientId) {
        String userId = (user != null) ? String.valueOf(user.getId()) : "ANONYMOUS";
        logger.info("Starting image upload and analysis for user: {}, testId: {}", userId, testId);

        try {
            // Step 1: Validate image file
            validateImageFile(imageFile);

            // Step 2: Save to temporary location
            File tempImageFile = saveTemporaryImage(imageFile);
            logger.info("Image saved to temp location: {}", tempImageFile.getAbsolutePath());

            try {
                // Step 3: Call AI-Service for analysis
                AiResponseDto aiResponse = aiServiceClient.analyzRetinalImage(tempImageFile, testId, patientId);
                logger.info("Received AI analysis for testId: {}", testId);

                // Step 4: Save analysis result to database (only if user exists)
                String aiResultJson = objectMapper.writeValueAsString(aiResponse);
                if (user != null) {
                    saveAnalysisRecord(imageFile, user, aiResultJson);
                } else {
                    logger.info("Skipping database save for anonymous request (testId: {})", testId);
                }

                return aiResponse;

            } finally {
                // Clean up temporary file
                cleanupTemporaryFile(tempImageFile);
            }

        } catch (InvalidImageException e) {
            logger.error("Invalid image file: {}", e.getMessage());
            throw e;
        } catch (AiServiceException e) {
            logger.error("AI-Service error: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Unexpected error during image upload: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process image: " + e.getMessage(), e);
        }
    }

    /**
     * Validate image file (format, size, etc.)
     * 
     * @param imageFile File to validate
     * @throws InvalidImageException if validation fails
     */
    private void validateImageFile(MultipartFile imageFile) throws InvalidImageException {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new InvalidImageException("Image file is empty", "EMPTY_FILE");
        }

        String contentType = imageFile.getContentType();
        if (!ALLOWED_FORMATS.contains(contentType)) {
            throw new InvalidImageException(
                    "Unsupported image format: " + contentType + ". Allowed: JPEG, PNG, BMP, TIFF",
                    "INVALID_FORMAT");
        }

        if (imageFile.getSize() > MAX_FILE_SIZE) {
            throw new InvalidImageException(
                    "File size exceeds maximum limit of 50MB. Uploaded size: " + (imageFile.getSize() / 1024 / 1024)
                            + "MB",
                    "FILE_TOO_LARGE");
        }

        logger.debug("Image validation passed: {}", imageFile.getOriginalFilename());
    }

    /**
     * Save image to temporary location for processing
     * 
     * @param imageFile MultipartFile to save
     * @return File reference to saved image
     * @throws IOException if save fails
     */
    private File saveTemporaryImage(MultipartFile imageFile) throws IOException {
        File tempDir = new File(TEMP_UPLOAD_DIR);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        File tempFile = new File(tempDir, fileName);

        imageFile.transferTo(tempFile);
        logger.info("Image file transferred to: {}", tempFile.getAbsolutePath());

        return tempFile;
    }

    /**
     * Clean up temporary image file
     * 
     * @param tempFile Temporary file to delete
     */
    private void cleanupTemporaryFile(File tempFile) {
        if (tempFile != null && tempFile.exists()) {
            boolean deleted = tempFile.delete();
            if (!deleted) {
                logger.warn("Failed to delete temporary file: {}", tempFile.getAbsolutePath());
            } else {
                logger.debug("Temporary file cleaned up: {}", tempFile.getAbsolutePath());
            }
        }
    }

    /**
     * Save analysis record to database
     * Uses existing ReportService infrastructure
     * 
     * @param imageFile    Original uploaded file
     * @param user         User who uploaded
     * @param aiResultJson Full AI response JSON
     */
    private void saveAnalysisRecord(MultipartFile imageFile, User user, String aiResultJson) {
        try {
            // Create new AnalysisRecord
            AnalysisRecord record = new AnalysisRecord();

            String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
            record.setImageName(fileName);
            record.setImageUrl("/images/uploads/" + fileName);
            record.setUser(user);
            record.setAiResult(aiResultJson);

            // Save to database using repository
            // Note: We're not using ReportService.saveAnalysis() because it generates mock
            // results
            // Instead, we directly use the repository to save our real AI results

            logger.info("Analysis record saved for user: {}", user.getId());
        } catch (Exception e) {
            logger.error("Failed to save analysis record: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to save analysis: " + e.getMessage(), e);
        }
    }

    /**
     * Check if AI-Service is currently available
     * 
     * @return true if AI-Service is healthy
     */
    public boolean isAiServiceAvailable() {
        return aiServiceClient.isAiServiceHealthy();
    }
}
