package com.testmanagement.controller;

import com.testmanagement.dto.ApplicationExecutionSummary;
import com.testmanagement.dto.OverallExecutionSummary;
import com.testmanagement.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller for Dashboard endpoints
 * Handles HTTP requests for dashboard analytics and summaries
 */
@RestController
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5000"})
public class DashboardController {

    private final DashboardService dashboardService;

    @Autowired
    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * GET /api/dashboard/summary
     * Get overall execution summary
     */
    @GetMapping("/summary")
    public ResponseEntity<OverallExecutionSummary> getOverallSummary(
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        OverallExecutionSummary summary = dashboardService.getOverallSummary(applicationId, stream, crew);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{suiteCategory}/suite-summary
     * Get suite summary by suite category
     */
    @GetMapping("/{suiteCategory}/suite-summary")
    public ResponseEntity<OverallExecutionSummary> getSuiteSummaryBySuiteCategory(
            @PathVariable String suiteCategory,
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        OverallExecutionSummary summary = dashboardService.getSuiteSummaryBySuiteCategory(suiteCategory, applicationId, stream, crew);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{executionType}/execution-summary
     * Get execution summary by execution type
     */
    @GetMapping("/{executionType}/execution-summary")
    public ResponseEntity<OverallExecutionSummary> getExecutionSummaryByExecutionType(
            @PathVariable String executionType,
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        OverallExecutionSummary summary = dashboardService.getExecutionSummaryByExecutionType(executionType, applicationId, stream, crew);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{applicationId}/{executionType}/{suiteCategory}/summary
     * Get summary by application ID, execution type, and suite category
     */
    @GetMapping("/{applicationId}/{executionType}/{suiteCategory}/summary")
    public ResponseEntity<OverallExecutionSummary> getSummaryByApplicationExecutionTypeAndSuiteCategory(
            @PathVariable String applicationId,
            @PathVariable String executionType,
            @PathVariable String suiteCategory) {
        OverallExecutionSummary summary = dashboardService
                .getSummaryByApplicationExecutionTypeAndSuiteCategory(applicationId, executionType, suiteCategory);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{applicationId}/{executionType}/execution-summary
     * Get execution summary by application ID and execution type
     */
    @GetMapping("/{applicationId}/{executionType}/execution-summary")
    public ResponseEntity<OverallExecutionSummary> getExecutionSummaryByApplicationAndExecutionType(
            @PathVariable String applicationId,
            @PathVariable String executionType) {
        OverallExecutionSummary summary = dashboardService
                .getExecutionSummaryByApplicationAndExecutionType(applicationId, executionType);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{applicationId}/summary
     * Get summary by application ID
     */
    @GetMapping("/{applicationId}/summary")
    public ResponseEntity<ApplicationExecutionSummary> getSummaryByApplication(@PathVariable String applicationId) {
        ApplicationExecutionSummary summary = dashboardService.getSummaryByApplication(applicationId);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/{applicationId}/{suiteCategory}/suite-summary
     * Get suite summary by application ID and suite category
     */
    @GetMapping("/{applicationId}/{suiteCategory}/suite-summary")
    public ResponseEntity<OverallExecutionSummary> getSuiteSummaryByApplicationAndSuiteCategory(
            @PathVariable String applicationId,
            @PathVariable String suiteCategory) {
        OverallExecutionSummary summary = dashboardService
                .getSuiteSummaryByApplicationAndSuiteCategory(applicationId, suiteCategory);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/dashboard/applications/summary
     * Get all applications execution summaries (additional endpoint for comprehensive dashboard)
     */
    @GetMapping("/applications/summary")
    public ResponseEntity<List<ApplicationExecutionSummary>> getAllApplicationsSummary(
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        List<ApplicationExecutionSummary> summaries = dashboardService.getAllApplicationsSummary(applicationId, stream, crew);
        return ResponseEntity.ok(summaries);
    }

    /**
     * GET /api/dashboard/trends/overall
     * Get overall daily execution trends
     */
    @GetMapping("/trends-data/overall")
    public ResponseEntity<List<com.testmanagement.dto.DailyExecutionSummary>> getOverallDailyExecutionTrends(
            @RequestParam(required = false) String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        List<com.testmanagement.dto.DailyExecutionSummary> trends = dashboardService.getOverallDailyExecutionSummary(applicationId, stream, crew);
        return ResponseEntity.ok(trends);
    }

    /**
     * GET /api/dashboard/trends/applications/{applicationId}
     * Get daily execution trends for a specific application
     */
    @GetMapping("/trends/applications/{applicationId}")
    public ResponseEntity<List<com.testmanagement.dto.DailyExecutionSummary>> getApplicationDailyExecutionTrends(
            @PathVariable String applicationId,
            @RequestParam(required = false) String stream,
            @RequestParam(required = false) String crew) {
        List<com.testmanagement.dto.DailyExecutionSummary> trends = dashboardService.getApplicationDailyExecutionSummary(applicationId, stream, crew);
        return ResponseEntity.ok(trends);
    }

    @GetMapping("/test-mapping")
    public ResponseEntity<String> testMapping() {
        return ResponseEntity.ok("Test mapping successful!");
    }
}
