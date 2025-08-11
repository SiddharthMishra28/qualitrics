package com.testmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA Entity for executions table
 * Represents a test execution record
 */
@Entity
@Table(name = "executions")
public class Execution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "uuid", unique = true)
    private UUID uuid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @NotNull(message = "Application is required")
    private Application application;

    @Column(name = "execution_type", nullable = false, length = 64)
    @Enumerated(EnumType.STRING)
    private ExecutionType executionType;

    @Column(name = "execution_suite_category", nullable = false, length = 128)
    @Enumerated(EnumType.STRING)
    private ExecutionSuiteCategory executionSuiteCategory;

    @Column(name = "total_test_cases", nullable = false)
    @Min(value = 0, message = "Total test cases cannot be negative")
    private Integer totalTestCases = 0;

    @Column(name = "count_passed", nullable = false)
    @Min(value = 0, message = "Count passed cannot be negative")
    private Integer countPassed = 0;

    @Column(name = "count_failed", nullable = false)
    @Min(value = 0, message = "Count failed cannot be negative")
    private Integer countFailed = 0;

    @Column(name = "count_skipped", nullable = false)
    @Min(value = 0, message = "Count skipped cannot be negative")
    private Integer countSkipped = 0;

    @Column(name = "execution_time", nullable = false)
    @Min(value = 0, message = "Execution time cannot be negative")
    private Double executionTime = 0.0;

    @Column(name = "report_link")
    private String reportLink;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Execution Type Enum
    public enum ExecutionType {
        functional, regression
    }

    // Execution Suite Category Enum
    public enum ExecutionSuiteCategory {
        sanity, smoke
    }

    // Default constructor
    public Execution() {
        this.uuid = UUID.randomUUID();
    }

    // Constructor with required fields
    public Execution(Application application, ExecutionType executionType, ExecutionSuiteCategory executionSuiteCategory) {
        this();
        this.application = application;
        this.executionType = executionType;
        this.executionSuiteCategory = executionSuiteCategory;
    }

    @PrePersist
    public void prePersist() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    /**
     * Calculate overall build status based on test results
     */
    public String getOverallBuildStatus() {
        if (countFailed > 0) {
            return "FAILED";
        } else if (countPassed > 0) {
            return "PASSED";
        } else {
            return "UNKNOWN";
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    public ExecutionSuiteCategory getExecutionSuiteCategory() {
        return executionSuiteCategory;
    }

    public void setExecutionSuiteCategory(ExecutionSuiteCategory executionSuiteCategory) {
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
