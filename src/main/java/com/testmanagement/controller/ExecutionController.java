package com.testmanagement.controller;

import com.testmanagement.dto.ExecutionDto;
import com.testmanagement.dto.ResultPublishedDto;
import com.testmanagement.service.ExecutionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Execution endpoints
 * Handles HTTP requests for execution management
 */
@RestController
@RequestMapping("/api/executions")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5000"})
public class ExecutionController {

    private final ExecutionService executionService;

    @Autowired
    public ExecutionController(ExecutionService executionService) {
        this.executionService = executionService;
    }

    /**
     * POST /api/executions/publish-results
     * Publish execution results
     */
    @PostMapping("/publish-results")
    public ResponseEntity<ResultPublishedDto> publishExecutionResults(@Valid @RequestBody ExecutionDto executionDto) {
        ResultPublishedDto result = executionService.publishExecutionResults(executionDto);
        
        if ("SUCCESS".equals(result.getPublishResult())) {
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/executions/{executionReference}
     * Get execution by reference ID (UUID)
     */
    @GetMapping("/{executionReference}")
    public ResponseEntity<ExecutionDto> getExecutionByReference(@PathVariable UUID executionReference) {
        ExecutionDto execution = executionService.getExecutionByReference(executionReference);
        return ResponseEntity.ok(execution);
    }

    /**
     * GET /api/executions/type/{executionType}
     * Get executions by execution type
     */
    @GetMapping("/type/{executionType}")
    public ResponseEntity<List<ExecutionDto>> getExecutionsByType(@PathVariable String executionType) {
        List<ExecutionDto> executions = executionService.getExecutionsByType(executionType);
        return ResponseEntity.ok(executions);
    }

    /**
     * GET /api/executions/suite/{suiteCategory}
     * Get executions by suite category
     */
    @GetMapping("/suite/{suiteCategory}")
    public ResponseEntity<List<ExecutionDto>> getExecutionsBySuiteCategory(@PathVariable String suiteCategory) {
        List<ExecutionDto> executions = executionService.getExecutionsBySuiteCategory(suiteCategory);
        return ResponseEntity.ok(executions);
    }
}
