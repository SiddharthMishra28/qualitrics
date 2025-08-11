package com.testmanagement.dto;

import java.util.UUID;

/**
 * DTO for execution result publication response
 * Returned when execution results are successfully published
 */
public class ResultPublishedDto {

    private String publishResult;
    private UUID executionReferenceId;

    // Default constructor
    public ResultPublishedDto() {}

    // Constructor
    public ResultPublishedDto(String publishResult, UUID executionReferenceId) {
        this.publishResult = publishResult;
        this.executionReferenceId = executionReferenceId;
    }

    // Static factory method for success response
    public static ResultPublishedDto success(UUID executionReferenceId) {
        return new ResultPublishedDto("SUCCESS", executionReferenceId);
    }

    // Static factory method for failure response
    public static ResultPublishedDto failure() {
        return new ResultPublishedDto("FAILURE", null);
    }

    // Getters and Setters
    public String getPublishResult() {
        return publishResult;
    }

    public void setPublishResult(String publishResult) {
        this.publishResult = publishResult;
    }

    public UUID getExecutionReferenceId() {
        return executionReferenceId;
    }

    public void setExecutionReferenceId(UUID executionReferenceId) {
        this.executionReferenceId = executionReferenceId;
    }
}
