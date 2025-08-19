package com.testmanagement.email.controller;

import com.testmanagement.email.models.TestSummaryReport;
import com.testmanagement.email.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-summary")
    public void sendTestSummaryEmail(@RequestBody TestSummaryReport testSummaryReport) {
        emailService.sendTestSummaryEmail(testSummaryReport);
    }
}
