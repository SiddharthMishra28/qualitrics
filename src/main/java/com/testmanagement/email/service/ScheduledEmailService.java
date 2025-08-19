package com.testmanagement.email.service;

import com.testmanagement.email.models.ApplicationTestSummary;
import com.testmanagement.email.models.TestSummaryReport;
import com.testmanagement.service.DashboardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduledEmailService {

    private final EmailService emailService;
    private final DashboardService dashboardService;

    @Scheduled(cron = "${email.summary.cron}")
    public void sendScheduledTestSummaryEmail() {
        log.info("Executing scheduled test summary email job.");
        List<ApplicationTestSummary> summaries = dashboardService.getTestSummaryForEmail();
        if (summaries.isEmpty()) {
            log.info("No test executions found, skipping email report.");
            return;
        }
        TestSummaryReport report = new TestSummaryReport(summaries);
        emailService.sendTestSummaryEmail(report);
    }
}
