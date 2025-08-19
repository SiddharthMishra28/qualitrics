package com.testmanagement.email.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TestSummaryReport {
    private List<ApplicationTestSummary> applicationTestSummaries;
}
