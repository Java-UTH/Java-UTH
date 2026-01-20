package com.example.SP26SE025.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;
import java.util.List;

/**
 * DTO for complete AI-Service response (FR-3 AI Output)
 * Maps JSON response from FastAPI /predict endpoint
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AiResponseDto {

    private String status;
    private AiInputDto input;
    private AiAnalysisDto analysis;
    private List<PredictionDto> predictions;
    private AiMetadataDto meta;

    // Constructors
    public AiResponseDto() {
    }

    public AiResponseDto(String status, AiInputDto input, AiAnalysisDto analysis,
            List<PredictionDto> predictions, AiMetadataDto meta) {
        this.status = status;
        this.input = input;
        this.analysis = analysis;
        this.predictions = predictions;
        this.meta = meta;
    }

    // Getters and Setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public AiInputDto getInput() {
        return input;
    }

    public void setInput(AiInputDto input) {
        this.input = input;
    }

    public AiAnalysisDto getAnalysis() {
        return analysis;
    }

    public void setAnalysis(AiAnalysisDto analysis) {
        this.analysis = analysis;
    }

    public List<PredictionDto> getPredictions() {
        return predictions;
    }

    public void setPredictions(List<PredictionDto> predictions) {
        this.predictions = predictions;
    }

    public AiMetadataDto getMeta() {
        return meta;
    }

    public void setMeta(AiMetadataDto meta) {
        this.meta = meta;
    }
}
