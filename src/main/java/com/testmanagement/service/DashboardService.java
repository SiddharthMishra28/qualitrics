package com.testmanagement.service;

import com.testmanagement.dto.ApplicationExecutionSummary;
import com.testmanagement.dto.OverallExecutionSummary;
import com.testmanagement.entity.Application;
import com.testmanagement.entity.Execution;
import com.testmanagement.repository.ApplicationRepository;
import com.testmanagement.repository.ExecutionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Dashboard analytics
 * Handles business logic for dashboard summary and statistics
 */
@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ExecutionRepository executionRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationService applicationService;

    @Autowired
    public DashboardService(ExecutionRepository executionRepository, 
                          ApplicationRepository applicationRepository,
                          ApplicationService applicationService) {
        this.executionRepository = executionRepository;
        this.applicationRepository = applicationRepository;
        this.applicationService = applicationService;
    }

    /**
     * Get overall execution summary
     */
    public OverallExecutionSummary getOverallSummary(String applicationId, String stream, String crew) {
        Long totalBuilds = executionRepository.countTotalExecutions(applicationId, stream, crew);
        Long totalBuildsPassed = executionRepository.countPassedExecutions(applicationId, stream, crew);
        Long totalBuildsFailed = executionRepository.countFailedExecutions(applicationId, stream, crew);

        return new OverallExecutionSummary(
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get suite summary by suite category
     */
    public OverallExecutionSummary getSuiteSummaryBySuiteCategory(String suiteCategory, String applicationId, String stream, String crew) {
        Execution.ExecutionSuiteCategory category = Execution.ExecutionSuiteCategory.valueOf(suiteCategory);
        
        Long totalBuilds = executionRepository.countBySuiteCategory(category, applicationId, stream, crew);
        Long totalBuildsPassed = executionRepository.countPassedBySuiteCategory(category, applicationId, stream, crew);
        Long totalBuildsFailed = executionRepository.countFailedBySuiteCategory(category, applicationId, stream, crew);

        return new OverallExecutionSummary(
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get execution summary by execution type
     */
    public OverallExecutionSummary getExecutionSummaryByExecutionType(String executionType, String applicationId, String stream, String crew) {
        Execution.ExecutionType type = Execution.ExecutionType.valueOf(executionType);
        
        Long totalBuilds = executionRepository.countByExecutionType(type, applicationId, stream, crew);
        Long totalBuildsPassed = executionRepository.countPassedByExecutionType(type, applicationId, stream, crew);
        Long totalBuildsFailed = executionRepository.countFailedByExecutionType(type, applicationId, stream, crew);

        return new OverallExecutionSummary(
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get summary by application ID, execution type, and suite category
     */
    public OverallExecutionSummary getSummaryByApplicationExecutionTypeAndSuiteCategory(
            String applicationId, String executionType, String suiteCategory) {
        
        Application application = applicationService.getApplicationEntityByApplicationId(applicationId);
        Execution.ExecutionType type = Execution.ExecutionType.valueOf(executionType);
        Execution.ExecutionSuiteCategory category = Execution.ExecutionSuiteCategory.valueOf(suiteCategory);
        
        List<Execution> executions = executionRepository
                .findByApplicationIdAndExecutionTypeAndSuiteCategoryOrderByCreatedAtDesc(
                        application.getId(), type, category);
        
        long totalBuilds = executions.size();
        long totalBuildsPassed = executions.stream()
                .mapToLong(e -> e.getCountFailed() == 0 && e.getCountPassed() > 0 ? 1 : 0)
                .sum();
        long totalBuildsFailed = executions.stream()
                .mapToLong(e -> e.getCountFailed() > 0 ? 1 : 0)
                .sum();

        return new OverallExecutionSummary(
                (int) totalBuilds,
                (int) totalBuildsPassed,
                (int) totalBuildsFailed
        );
    }

    /**
     * Get execution summary by application ID and execution type
     */
    public OverallExecutionSummary getExecutionSummaryByApplicationAndExecutionType(
            String applicationId, String executionType) {
        
        Application application = applicationService.getApplicationEntityByApplicationId(applicationId);
        Execution.ExecutionType type = Execution.ExecutionType.valueOf(executionType);
        
        Long totalBuilds = executionRepository.countByApplicationIdAndExecutionType(application.getId(), type);
        Long totalBuildsPassed = executionRepository.countPassedByApplicationIdAndExecutionType(application.getId(), type);
        Long totalBuildsFailed = executionRepository.countFailedByApplicationIdAndExecutionType(application.getId(), type);

        return new OverallExecutionSummary(
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get summary by application ID
     */
    public ApplicationExecutionSummary getSummaryByApplication(String applicationId) {
        Application application = applicationService.getApplicationEntityByApplicationId(applicationId);
        
        Long totalBuilds = executionRepository.countByApplicationId(application.getId());
        Long totalBuildsPassed = executionRepository.countPassedByApplicationId(application.getId());
        Long totalBuildsFailed = executionRepository.countFailedByApplicationId(application.getId());

        return new ApplicationExecutionSummary(
                application.getApplicationId(),
                application.getApplicationName(),
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get suite summary by application ID and suite category
     */
    public OverallExecutionSummary getSuiteSummaryByApplicationAndSuiteCategory(
            String applicationId, String suiteCategory) {
        
        Application application = applicationService.getApplicationEntityByApplicationId(applicationId);
        Execution.ExecutionSuiteCategory category = Execution.ExecutionSuiteCategory.valueOf(suiteCategory);
        
        Long totalBuilds = executionRepository.countByApplicationIdAndSuiteCategory(application.getId(), category);
        Long totalBuildsPassed = executionRepository.countPassedByApplicationIdAndSuiteCategory(application.getId(), category);
        Long totalBuildsFailed = executionRepository.countFailedByApplicationIdAndSuiteCategory(application.getId(), category);

        return new OverallExecutionSummary(
                totalBuilds.intValue(),
                totalBuildsPassed.intValue(),
                totalBuildsFailed.intValue()
        );
    }

    /**
     * Get all applications execution summaries for overall dashboard
     */
    public List<ApplicationExecutionSummary> getAllApplicationsSummary(String applicationId, String stream, String crew) {
        List<Application> applications;

        if (applicationId != null) {
            Application app = applicationService.getApplicationEntityByApplicationId(applicationId);
            applications = app != null ? List.of(app) : List.of();
        } else if (stream != null && crew != null) {
            applications = applicationRepository.findByStreamAndCrewOrderByApplicationName(stream, crew);
        } else if (stream != null) {
            applications = applicationRepository.findByStreamOrderByApplicationName(stream);
        } else if (crew != null) {
            applications = applicationRepository.findByCrewOrderByApplicationName(crew);
        } else {
            applications = applicationRepository.findAllOrderByApplicationName();
        }
        
        return applications.stream()
                .map(app -> {
                    Long totalBuilds = executionRepository.countTotalExecutions(app.getApplicationId(), stream, crew);
                    Long totalBuildsPassed = executionRepository.countPassedExecutions(app.getApplicationId(), stream, crew);
                    Long totalBuildsFailed = executionRepository.countFailedExecutions(app.getApplicationId(), stream, crew);
                    
                    return new ApplicationExecutionSummary(
                            app.getApplicationId(),
                            app.getApplicationName(),
                            totalBuilds.intValue(),
                            totalBuildsPassed.intValue(),
                            totalBuildsFailed.intValue()
                    );
                })
                .filter(summary -> summary.getTotalBuilds() > 0)
                .collect(Collectors.toList());
    }

    /**
     * Get daily execution summaries for overall trends.
     */
    public List<com.testmanagement.dto.DailyExecutionSummary> getOverallDailyExecutionSummary(String applicationId, String stream, String crew) {
        return executionRepository.findDailyExecutionSummaryOverall(applicationId, stream, crew);
    }

    /**
     * Get daily execution summaries for a specific application.
     */
    public List<com.testmanagement.dto.DailyExecutionSummary> getApplicationDailyExecutionSummary(String applicationId, String stream, String crew) {
        return executionRepository.findDailyExecutionSummaryOverall(applicationId, stream, crew);
    }
}
