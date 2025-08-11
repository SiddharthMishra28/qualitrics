package com.testmanagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * DTO for execution data transfer
 * Used for publishing execution results
 */
public class ExecutionDto {

    @NotBlank(message = "Application ID is required")
    private String applicationId;

    @NotBlank(message = "Execution type is required")
    private String executionType;

    @NotBlank(message = "Execution suite category is required")
    private String executionSuiteCategory;

    @NotNull(message = "Total test cases is required")
    @Min(value = 0, message = "Total test cases cannot be negative")
    private Integer totalTestCases;

    @NotNull(message = "Count passed is required")
    @Min(value = 0, message = "Count passed cannot be negative")
    private Integer countPassed;

    @NotNull(message = "Count failed is required")
    @Min(value = 0, message = "Count failed cannot be negative")
    private Integer countFailed;

    @NotNull(message = "Count skipped is required")
    @Min(value = 0, message = "Count skipped cannot be negative")
    private Integer countSkipped;

    private String overallBuildStatus;

    @NotNull(message = "Execution time is required")
    @Min(value = 0, message = "Execution time cannot be negative")
    private Double executionTime;

    private String reportLink;

    private UUID uuid;

    private java.time.LocalDateTime createdAt;

    // Default constructor
    public ExecutionDto() {}

    // Constructor with required fields
    public ExecutionDto(String applicationId, String executionType, String executionSuiteCategory,
                       Integer totalTestCases, Integer countPassed, Integer countFailed,
                       Integer countSkipped, Double executionTime) {
        this.applicationId = applicationId;
        this.executionType = executionType;
        this.executionSuiteCategory = executionSuiteCategory;
        this.totalTestCases = totalTestCases;
        this.countPassed = countPassed;
        this.countFailed = countFailed;
        this.countSkipped = countSkipped;
        this.executionTime = executionTime;
        this.overallBuildStatus = calculateOverallBuildStatus();
    }

    /**
     * Calculate overall build status based on test results
     */
    public String calculateOverallBuildStatus() {
        if (countFailed != null && countFailed > 0) {
            return "FAILED";
        } else if (countPassed != null && countPassed > 0) {
            return "PASSED";
        } else {
            return "UNKNOWN";
        }
    }

    // Getters and Setters
    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getExecutionType() {
        return executionType;
    }

    public void setExecutionType(String executionType) {
        this.executionType = executionType;
    }

    public String getExecutionSuiteCategory() {
        return executionSuiteCategory;
    }

    public void setExecutionSuiteCategory(String executionSuiteCategory) {
        this.executionSuiteCategory = executionSuiteCategory;
    }

    public Integer getTotalTestCases() {
        return totalTestCases;
    }

    public void setTotalTestCases(Integer totalTestCases) {
        this.totalTestCases = totalTestCases;
    }

    public Integer getCountPassed() {
        return countPassed;
    }

    public void setCountPassed(Integer countPassed) {
        this.countPassed = countPassed;
    }

    public Integer getCountFailed() {
        return countFailed;
    }

    public void setCountFailed(Integer countFailed) {
        this.countFailed = countFailed;
    }

    public Integer getCountSkipped() {
        return countSkipped;
    }

    public void setCountSkipped(Integer countSkipped) {
        this.countSkipped = countSkipped;
    }

    public String getOverallBuildStatus() {
        if (overallBuildStatus == null) {
            overallBuildStatus = calculateOverallBuildStatus();
        }
        return overallBuildStatus;
    }

    public void setOverallBuildStatus(String overallBuildStatus) {
        this.overallBuildStatus = overallBuildStatus;
    }

    public Double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Double executionTime) {
        this.executionTime = executionTime;
    }

    public String getReportLink() {
        return reportLink;
    }

    public void setReportLink(String reportLink) {
        this.reportLink = reportLink;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public java.time.LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(java.time.LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
