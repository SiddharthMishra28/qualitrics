package com.testmanagement.dto;

import java.time.LocalDate;
import java.sql.Date; // Added import

public class DailyExecutionSummary {
    private LocalDate date;
    private Long totalBuilds;
    private Long passed;
    private Long failed;

    public DailyExecutionSummary(java.sql.Date date, Long totalBuilds, Long passed, Long failed) {
        this.date = date.toLocalDate(); // Convert java.sql.Date to LocalDate
        this.totalBuilds = totalBuilds;
        this.passed = passed;
        this.failed = failed;
    }

    // Getters
    public LocalDate getDate() {
        return date;
    }

    public Long getTotalBuilds() {
        return totalBuilds;
    }

    public Long getPassed() {
        return passed;
    }

    public Long getFailed() {
        return failed;
    }
}
