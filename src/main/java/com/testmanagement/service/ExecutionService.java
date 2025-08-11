package com.testmanagement.service;

import com.testmanagement.dto.ExecutionDto;
import com.testmanagement.dto.ExecutionsByApplicationResponse;
import com.testmanagement.dto.ResultPublishedDto;
import com.testmanagement.entity.Application;
import com.testmanagement.entity.Execution;
import com.testmanagement.exception.ExecutionNotFoundException;
import com.testmanagement.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service class for Execution entity
 * Handles business logic for execution management
 */
@Service
@Transactional
public class ExecutionService {

    private final ExecutionRepository executionRepository;
    private final ApplicationService applicationService;

    @Autowired
    public ExecutionService(ExecutionRepository executionRepository, ApplicationService applicationService) {
        this.executionRepository = executionRepository;
        this.applicationService = applicationService;
    }

    /**
     * Publish execution results
     */
    public ResultPublishedDto publishExecutionResults(ExecutionDto executionDto) {
        // Get the application
        Application application = applicationService.getApplicationEntityByApplicationId(executionDto.getApplicationId());
        
        // Create execution entity
        Execution execution = convertToEntity(executionDto, application);
        
        // Save execution
        Execution savedExecution = executionRepository.save(execution);
        
        return ResultPublishedDto.success(savedExecution.getUuid());
    }

    /**
     * Get execution by reference ID (UUID)
     */
    @Transactional(readOnly = true)
    public ExecutionDto getExecutionByReference(UUID executionReference) {
        Execution execution = executionRepository.findByUuid(executionReference)
                .orElseThrow(() -> new ExecutionNotFoundException("Execution not found with reference: " + executionReference));
        return convertToDto(execution);
    }

    /**
     * Get executions by application ID
     */
    @Transactional(readOnly = true)
    public ExecutionsByApplicationResponse getExecutionsByApplicationId(String applicationId) {
        Application application = applicationService.getApplicationEntityByApplicationId(applicationId);
        List<Execution> executions = executionRepository.findByApplicationOrderByCreatedAtDesc(application);
        
        List<ExecutionDto> executionDtos = executions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        
        return new ExecutionsByApplicationResponse(
                application.getApplicationId(),
                application.getApplicationName(),
                executionDtos
        );
    }

    /**
     * Get executions by execution type
     */
    @Transactional(readOnly = true)
    public List<ExecutionDto> getExecutionsByType(String executionType) {
        Execution.ExecutionType type = Execution.ExecutionType.valueOf(executionType.toLowerCase());
        List<Execution> executions = executionRepository.findByExecutionTypeOrderByCreatedAtDesc(type);
        
        return executions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get executions by suite category
     */
    @Transactional(readOnly = true)
    public List<ExecutionDto> getExecutionsBySuiteCategory(String suiteCategory) {
        Execution.ExecutionSuiteCategory category = Execution.ExecutionSuiteCategory.valueOf(suiteCategory.toLowerCase());
        List<Execution> executions = executionRepository.findByExecutionSuiteCategoryOrderByCreatedAtDesc(category);
        
        return executions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convert Execution entity to DTO
     */
    private ExecutionDto convertToDto(Execution execution) {
        ExecutionDto dto = new ExecutionDto();
        dto.setApplicationId(execution.getApplication().getApplicationId());
        dto.setExecutionType(execution.getExecutionType().name());
        dto.setExecutionSuiteCategory(execution.getExecutionSuiteCategory().name());
        dto.setTotalTestCases(execution.getTotalTestCases());
        dto.setCountPassed(execution.getCountPassed());
        dto.setCountFailed(execution.getCountFailed());
        dto.setCountSkipped(execution.getCountSkipped());
        dto.setExecutionTime(execution.getExecutionTime());
        dto.setReportLink(execution.getReportLink());
        dto.setOverallBuildStatus(execution.getOverallBuildStatus());
        dto.setUuid(execution.getUuid());
        dto.setCreatedAt(execution.getCreatedAt());
        return dto;
    }

    /**
     * Convert DTO to Execution entity
     */
    private Execution convertToEntity(ExecutionDto executionDto, Application application) {
        Execution execution = new Execution();
        execution.setApplication(application);
        execution.setExecutionType(Execution.ExecutionType.valueOf(executionDto.getExecutionType()));
        execution.setExecutionSuiteCategory(Execution.ExecutionSuiteCategory.valueOf(executionDto.getExecutionSuiteCategory()));
        execution.setTotalTestCases(executionDto.getTotalTestCases());
        execution.setCountPassed(executionDto.getCountPassed());
        execution.setCountFailed(executionDto.getCountFailed());
        execution.setCountSkipped(executionDto.getCountSkipped());
        execution.setExecutionTime(executionDto.getExecutionTime());
        execution.setReportLink(executionDto.getReportLink());
        return execution;
    }
}
