package com.testmanagement.controller;

import com.testmanagement.dto.ApplicationDto;
import com.testmanagement.dto.ApplicationResponse;
import com.testmanagement.dto.ExecutionsByApplicationResponse;
import com.testmanagement.service.ApplicationService;
import com.testmanagement.service.ExecutionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Application endpoints
 * Handles HTTP requests for application management
 */
@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5000"})
public class ApplicationController {

    private final ApplicationService applicationService;
    private final ExecutionService executionService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, ExecutionService executionService) {
        this.applicationService = applicationService;
        this.executionService = executionService;
    }

    /**
     * POST /api/applications/register
     * Register a new application
     */
    @PostMapping("/register")
    public ResponseEntity<ApplicationDto> registerApplication(@Valid @RequestBody ApplicationDto applicationDto) {
        ApplicationDto registeredApplication = applicationService.registerApplication(applicationDto);
        return new ResponseEntity<>(registeredApplication, HttpStatus.CREATED);
    }

    /**
     * GET /api/applications
     * Get all applications
     */
    @GetMapping
    public ResponseEntity<ApplicationResponse> getAllApplications() {
        List<ApplicationDto> applications = applicationService.getAllApplications();
        ApplicationResponse response = new ApplicationResponse(applications);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/{applicationId}
     * Get application by application ID
     */
    @GetMapping("/{applicationId}")
    public ResponseEntity<ApplicationDto> getApplicationByApplicationId(@PathVariable String applicationId) {
        ApplicationDto application = applicationService.getApplicationByApplicationId(applicationId);
        return ResponseEntity.ok(application);
    }

    /**
     * GET /api/applications/{applicationId}/executions
     * Get executions for a specific application
     */
    @GetMapping("/{applicationId}/executions")
    public ResponseEntity<ExecutionsByApplicationResponse> getExecutionsByApplicationId(@PathVariable String applicationId) {
        ExecutionsByApplicationResponse response = executionService.getExecutionsByApplicationId(applicationId);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/stream/{streamName}
     * Get applications by stream
     */
    @GetMapping("/stream/{streamName}")
    public ResponseEntity<ApplicationResponse> getApplicationsByStream(@PathVariable String streamName) {
        List<ApplicationDto> applications = applicationService.getApplicationsByStream(streamName);
        ApplicationResponse response = new ApplicationResponse(applications);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/stream/{streamName}/crew/{crewName}
     * Get applications by stream and crew
     */
    @GetMapping("/stream/{streamName}/crew/{crewName}")
    public ResponseEntity<ApplicationResponse> getApplicationsByStreamAndCrew(
            @PathVariable String streamName, 
            @PathVariable String crewName) {
        List<ApplicationDto> applications = applicationService.getApplicationsByStreamAndCrew(streamName, crewName);
        ApplicationResponse response = new ApplicationResponse(applications);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/crew/{crewName}
     * Get applications by crew
     */
    @GetMapping("/crew/{crewName}")
    public ResponseEntity<ApplicationResponse> getApplicationsByCrew(@PathVariable String crewName) {
        List<ApplicationDto> applications = applicationService.getApplicationsByCrew(crewName);
        ApplicationResponse response = new ApplicationResponse(applications);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/applications/streams
     * Get all distinct stream names
     */
    @GetMapping("/streams")
    public ResponseEntity<List<String>> getDistinctStreams() {
        List<String> streams = applicationService.getDistinctStreams();
        return ResponseEntity.ok(streams);
    }

    /**
     * GET /api/applications/crews
     * Get all distinct crew names
     */
    @GetMapping("/crews")
    public ResponseEntity<List<String>> getDistinctCrews() {
        List<String> crews = applicationService.getDistinctCrews();
        return ResponseEntity.ok(crews);
    }
}
