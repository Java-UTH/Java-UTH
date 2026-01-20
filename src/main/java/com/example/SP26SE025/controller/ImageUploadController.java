package com.example.SP26SE025.controller;

import com.example.SP26SE025.dtos.AiResponseDto;
import com.example.SP26SE025.entity.User;
import com.example.SP26SE025.exception.AiServiceException;
import com.example.SP26SE025.exception.InvalidImageException;
import com.example.SP26SE025.security.CustomUserDetails;
import com.example.SP26SE025.service.ImageUploadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

/**
 * REST API Controller for image upload and analysis
 * Endpoints for uploading retinal images and triggering AI analysis (FR-2,
 * FR-3)
 */
@RestController
@RequestMapping("/api/reports")
public class ImageUploadController {

    private static final Logger logger = LoggerFactory.getLogger(ImageUploadController.class);

    @Autowired
    private ImageUploadService imageUploadService;

    /**
     * Upload retinal image for AI analysis
     * 
     * POST /api/reports/upload
     * 
     * @param imageFile      Retinal image file (JPEG, PNG, BMP, TIFF)
     * @param testId         Test/analysis ID (optional, for tracking)
     * @param patientId      Patient ID (optional, for tracking)
     * @param authentication Current user authentication
     * @return AI analysis response with predictions
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(
            @RequestParam("file") MultipartFile imageFile,
            @RequestParam(value = "testId", required = false) String testId,
            @RequestParam(value = "patientId", required = false) String patientId,
            Authentication authentication) {

        logger.info("Upload image request received. Filename: {}", imageFile.getOriginalFilename());

        try {
            // Get current user (handle case when authentication is null)
            User user = null;
            if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
                CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
                user = userDetails.getUser();
            }

            // Generate IDs if not provided
            if (testId == null || testId.isEmpty()) {
                testId = "TEST_" + System.currentTimeMillis();
            }
            if (patientId == null || patientId.isEmpty()) {
                // If no authenticated user, use test ID as patient ID
                patientId = (user != null) ? String.valueOf(user.getId()) : testId + "_PAT";
            }

            // Upload and analyze (user can be null for test mode)
            AiResponseDto aiResponse = imageUploadService.uploadAndAnalyzeImage(
                    imageFile, user, testId, patientId);

            logger.info("Image analysis completed. Status: {}", aiResponse.getStatus());

            // Prepare response
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "Image analysis completed successfully");
            response.put("data", aiResponse);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (InvalidImageException e) {
            logger.warn("Invalid image: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("reason", e.getReason());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);

        } catch (AiServiceException e) {
            logger.error("AI-Service error: {}", e.getMessage());

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "AI analysis service is temporarily unavailable");
            errorResponse.put("detail", e.getMessage());
            errorResponse.put("aiServiceUrl", e.getAiServiceUrl());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(errorResponse);

        } catch (Exception e) {
            logger.error("Unexpected error during upload", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to process image");
            errorResponse.put("detail", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    /**
     * Check AI-Service health status
     * 
     * GET /api/reports/health
     * 
     * @return Health status of AI-Service
     */
    @GetMapping("/health")
    public ResponseEntity<?> checkAiServiceHealth() {
        try {
            boolean isHealthy = imageUploadService.isAiServiceAvailable();

            Map<String, Object> response = new HashMap<>();
            response.put("status", isHealthy ? "healthy" : "unhealthy");
            response.put("aiServiceAvailable", isHealthy);
            response.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            logger.error("Error checking AI-Service health", e);

            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "error");
            errorResponse.put("message", "Failed to check AI-Service health");
            errorResponse.put("detail", e.getMessage());
            errorResponse.put("timestamp", System.currentTimeMillis());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}
