package com.testmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * JPA Entity for applications table
 * Represents a test application in the system
 */
@Entity
@Table(name = "applications")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "application_id", unique = true, nullable = false, length = 36)
    @NotBlank(message = "Application ID is required")
    @Size(max = 36, message = "Application ID cannot exceed 36 characters")
    private String applicationId;

    @Column(name = "application_name", nullable = false, length = 64)
    @NotBlank(message = "Application name is required")
    @Size(max = 64, message = "Application name cannot exceed 64 characters")
    private String applicationName;

    @Column(name = "application_description")
    private String applicationDescription;

    @Column(name = "stream", length = 64)
    @Size(max = 64, message = "Stream cannot exceed 64 characters")
    private String stream;

    @Column(name = "crew", length = 64)
    @Size(max = 64, message = "Crew cannot exceed 64 characters")
    private String crew;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Execution> executions;

    // Default constructor
    public Application() {}

    // Constructor with required fields
    public Application(String applicationName) {
        this.applicationName = applicationName;
    }

    @PrePersist
    public void prePersist() {
        if (this.applicationId == null) {
            this.applicationId = UUID.randomUUID().toString();
        }
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationDescription() {
        return applicationDescription;
    }

    public void setApplicationDescription(String applicationDescription) {
        this.applicationDescription = applicationDescription;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getCrew() {
        return crew;
    }

    public void setCrew(String crew) {
        this.crew = crew;
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

    public List<Execution> getExecutions() {
        return executions;
    }

    public void setExecutions(List<Execution> executions) {
        this.executions = executions;
    }
}
