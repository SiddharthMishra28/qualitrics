package com.testmanagement.email.service;

import com.testmanagement.email.models.ApplicationTestSummary;
import com.testmanagement.email.models.TestSummaryReport;
import com.testmanagement.service.DashboardService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ScheduledEmailServiceTest {

    @Mock
    private EmailService emailService;

    @Mock
    private DashboardService dashboardService;

    @InjectMocks
    private ScheduledEmailService scheduledEmailService;

    @Test
    void sendScheduledTestSummaryEmail_shouldSendEmail_whenSummariesExist() {
        // Given
        ApplicationTestSummary summary = new ApplicationTestSummary("crew", "stream", "app", 10, 1, 2, "UNHEALTHY");
        List<ApplicationTestSummary> summaries = Collections.singletonList(summary);
        when(dashboardService.getTestSummaryForEmail()).thenReturn(summaries);

        // When
        scheduledEmailService.sendScheduledTestSummaryEmail();

        // Then
        ArgumentCaptor<TestSummaryReport> reportCaptor = ArgumentCaptor.forClass(TestSummaryReport.class);
        verify(emailService, times(1)).sendTestSummaryEmail(reportCaptor.capture());
        TestSummaryReport capturedReport = reportCaptor.getValue();
        assertEquals(1, capturedReport.getApplicationTestSummaries().size());
        assertEquals("app", capturedReport.getApplicationTestSummaries().get(0).getApplicationName());
    }

    @Test
    void sendScheduledTestSummaryEmail_shouldNotSendEmail_whenNoSummariesExist() {
        // Given
        when(dashboardService.getTestSummaryForEmail()).thenReturn(Collections.emptyList());

        // When
        scheduledEmailService.sendScheduledTestSummaryEmail();

        // Then
        verify(emailService, never()).sendTestSummaryEmail(any());
    }
}
