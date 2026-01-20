package com.example.SP26SE025.repository;

import com.example.SP26SE025.entity.InferenceMetadata;
import com.example.SP26SE025.entity.AnalysisRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

/**
 * Repository for InferenceMetadata entity
 * Manages AI inference tracking and feedback data (FR-14/15, FR-19)
 */
@Repository
public interface InferenceMetadataRepository extends JpaRepository<InferenceMetadata, Long> {

    /**
     * Find inference metadata by inference ID from AI-Service
     */
    Optional<InferenceMetadata> findByInferenceId(String inferenceId);

    /**
     * Find inference metadata by associated analysis record
     */
    Optional<InferenceMetadata> findByAnalysisRecord(AnalysisRecord analysisRecord);

    /**
     * Find all inferences awaiting doctor review
     */
    List<InferenceMetadata> findByDoctorReviewedFalse();

    /**
     * Find all inferences reviewed by specific doctor
     */
    List<InferenceMetadata> findByReviewedByDoctorId(Long doctorId);

    /**
     * Find all inferences that were approved by doctor
     */
    List<InferenceMetadata> findByDoctorApprovedTrue();

    /**
     * Find all inferences that were rejected by doctor (approved = false)
     */
    List<InferenceMetadata> findByDoctorApprovedFalse();
}
