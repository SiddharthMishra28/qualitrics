package com.testmanagement.dto;

/**
 * DTO for overall execution summary statistics
 * Used in dashboard analytics
 */
public class OverallExecutionSummary {

    private Integer totalBuilds;
    private Integer totalBuildsPassed;
    private Integer totalBuildsFailed;
    private Double percentPassed;
    private Double percentFailed;

    // Default constructor
    public OverallExecutionSummary() {}

    // Constructor
    public OverallExecutionSummary(Integer totalBuilds, Integer totalBuildsPassed, Integer totalBuildsFailed) {
        this.totalBuilds = totalBuilds;
        this.totalBuildsPassed = totalBuildsPassed;
        this.totalBuildsFailed = totalBuildsFailed;
        this.percentPassed = calculatePercentPassed();
        this.percentFailed = calculatePercentFailed();
    }

    /**
     * Calculate percentage of passed builds
     */
    public Double calculatePercentPassed() {
        if (totalBuilds == null || totalBuilds == 0) {
            return 0.0;
        }
        return (totalBuildsPassed != null) ? (totalBuildsPassed.doubleValue() / totalBuilds.doubleValue()) * 100 : 0.0;
    }

    /**
     * Calculate percentage of failed builds
     */
    public Double calculatePercentFailed() {
        if (totalBuilds == null || totalBuilds == 0) {
            return 0.0;
        }
        return (totalBuildsFailed != null) ? (totalBuildsFailed.doubleValue() / totalBuilds.doubleValue()) * 100 : 0.0;
    }

    // Getters and Setters
    public Integer getTotalBuilds() {
        return totalBuilds;
    }

    public void setTotalBuilds(Integer totalBuilds) {
        this.totalBuilds = totalBuilds;
        this.percentPassed = calculatePercentPassed();
        this.percentFailed = calculatePercentFailed();
    }

    public Integer getTotalBuildsPassed() {
        return totalBuildsPassed;
    }

    public void setTotalBuildsPassed(Integer totalBuildsPassed) {
        this.totalBuildsPassed = totalBuildsPassed;
        this.percentPassed = calculatePercentPassed();
    }

    public Integer getTotalBuildsFailed() {
        return totalBuildsFailed;
    }

    public void setTotalBuildsFailed(Integer totalBuildsFailed) {
        this.totalBuildsFailed = totalBuildsFailed;
        this.percentFailed = calculatePercentFailed();
    }

    public Double getPercentPassed() {
        if (percentPassed == null) {
            percentPassed = calculatePercentPassed();
        }
        return percentPassed;
    }

    public void setPercentPassed(Double percentPassed) {
        this.percentPassed = percentPassed;
    }

    public Double getPercentFailed() {
        if (percentFailed == null) {
            percentFailed = calculatePercentFailed();
        }
        return percentFailed;
    }

    public void setPercentFailed(Double percentFailed) {
        this.percentFailed = percentFailed;
    }
}
