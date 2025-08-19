package com.testmanagement.email.service;

import com.testmanagement.email.models.ApplicationTestSummary;
import com.testmanagement.email.models.TestSummaryReport;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.util.ReflectionTestUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailService emailService;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(emailService, "to", "test@example.com");
        ReflectionTestUtils.setField(emailService, "appBaseUrl", "http://localhost:3000");
    }

    @Test
    void sendTestSummaryEmail() throws Exception {
        // Given
        ApplicationTestSummary summary = new ApplicationTestSummary("crew", "stream", "app", 10, 1, 2, "HEALTHY");
        TestSummaryReport report = new TestSummaryReport(Collections.singletonList(summary));
        String htmlContent = "<html><body>Test</body></html>";

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        when(templateEngine.process(eq("test-summary"), any(Context.class))).thenReturn(htmlContent);

        // When
        emailService.sendTestSummaryEmail(report);

        // Then
        verify(mailSender, times(1)).send(mimeMessageCaptor.capture());

    }
}
