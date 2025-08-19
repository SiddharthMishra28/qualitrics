package com.testmanagement.email.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationTestSummary {
    private String crew;
    private String stream;
    private String applicationName;
    private int passed;
    private int failed;
    private int skipped;
    private String overallHealth;
}
