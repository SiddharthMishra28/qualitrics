CREATE TABLE executions (
    id BIGSERIAL PRIMARY KEY,
    uuid UUID,
    application_id BIGINT NOT NULL,
    execution_type VARCHAR(64) NOT NULL CHECK (execution_type IN ('functional', 'regression')),
    execution_suite_category VARCHAR(128) NOT NULL CHECK (execution_suite_category IN ('sanity', 'smoke')),
    total_test_cases INT NOT NULL DEFAULT 0,
    count_passed INT NOT NULL DEFAULT 0,
    count_failed INT NOT NULL DEFAULT 0,
    count_skipped INT NOT NULL DEFAULT 0,
    execution_time FLOAT8 NOT NULL DEFAULT 0,
    report_link TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
);

-- Create index on uuid for faster lookups
CREATE INDEX idx_executions_uuid ON executions(uuid);

-- Create indexes for filtering and analytics
CREATE INDEX idx_executions_application_id ON executions(application_id);
CREATE INDEX idx_executions_type ON executions(execution_type);
CREATE INDEX idx_executions_suite_category ON executions(execution_suite_category);
CREATE INDEX idx_executions_created_at ON executions(created_at);

-- Composite indexes for common queries
CREATE INDEX idx_executions_app_type ON executions(application_id, execution_type);
CREATE INDEX idx_executions_app_suite ON executions(application_id, execution_suite_category);
CREATE INDEX idx_executions_type_suite ON executions(execution_type, execution_suite_category);
