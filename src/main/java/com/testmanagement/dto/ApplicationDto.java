package com.testmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * DTO for application data transfer
 * Used for application registration and responses
 */
public class ApplicationDto {

    private Long id;

    @NotBlank(message = "Application ID is required")
    @Size(max = 32, message = "Application ID cannot exceed 32 characters")
    private String applicationId;

    @NotBlank(message = "Application name is required")
    @Size(max = 64, message = "Application name cannot exceed 64 characters")
    private String applicationName;

    private String applicationDescription;

    @Size(max = 64, message = "Stream cannot exceed 64 characters")
    private String stream;

    @Size(max = 64, message = "Crew cannot exceed 64 characters")
    private String crew;

    // Default constructor
    public ApplicationDto() {}

    // Constructor with required fields
    public ApplicationDto(String applicationId, String applicationName) {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
    }

    // Full constructor
    public ApplicationDto(Long id, String applicationId, String applicationName,
                         String applicationDescription, String stream, String crew) {
        this.id = id;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.applicationDescription = applicationDescription;
        this.stream = stream;
        this.crew = crew;
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
}
