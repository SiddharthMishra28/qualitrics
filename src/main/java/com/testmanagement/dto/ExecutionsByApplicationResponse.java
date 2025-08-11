package com.testmanagement.dto;

import java.util.List;

/**
 * DTO for executions by application response
 * Contains application info and its executions
 */
public class ExecutionsByApplicationResponse {

    private String applicationId;
    private String applicationName;
    private List<ExecutionDto> executions;

    // Default constructor
    public ExecutionsByApplicationResponse() {}

    // Constructor
    public ExecutionsByApplicationResponse(String applicationId, String applicationName, List<ExecutionDto> executions) {
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.executions = executions;
    }

    // Getters and Setters
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

    public List<ExecutionDto> getExecutions() {
        return executions;
    }

    public void setExecutions(List<ExecutionDto> executions) {
        this.executions = executions;
    }

    // Convenience method to get items (alias for executions)
    public List<ExecutionDto> getItems() {
        return this.executions;
    }

    public void setItems(List<ExecutionDto> items) {
        this.executions = items;
    }
}
