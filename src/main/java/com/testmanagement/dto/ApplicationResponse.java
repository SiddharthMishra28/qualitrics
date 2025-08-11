package com.testmanagement.dto;

import java.util.List;

/**
 * DTO for application list response
 * Wrapper for multiple applications
 */
public class ApplicationResponse {

    private List<ApplicationDto> applications;

    // Default constructor
    public ApplicationResponse() {}

    // Constructor
    public ApplicationResponse(List<ApplicationDto> applications) {
        this.applications = applications;
    }

    // Getters and Setters
    public List<ApplicationDto> getApplications() {
        return applications;
    }

    public void setApplications(List<ApplicationDto> applications) {
        this.applications = applications;
    }
}
