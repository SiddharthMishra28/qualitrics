package com.testmanagement.email.service;

import com.testmanagement.email.models.TestSummaryReport;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.to}")
    private String to;

    @Value("${app.baseUrl}")
    private String appBaseUrl;

    public void sendTestSummaryEmail(TestSummaryReport testSummaryReport) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");

            Context context = new Context();
            context.setVariable("report", testSummaryReport);
            context.setVariable("appBaseUrl", appBaseUrl);

            String htmlContent = templateEngine.process("test-summary", context);

            helper.setTo(to);
            helper.setSubject("Test Execution Summary Report");
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
            log.info("Test summary email sent successfully to {}", to);
        } catch (MessagingException e) {
            log.error("Failed to send test summary email", e);
        }
    }
}
