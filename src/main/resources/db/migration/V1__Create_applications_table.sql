CREATE TABLE applications (
    id BIGSERIAL PRIMARY KEY,
    application_id VARCHAR(36) UNIQUE NOT NULL,
    application_name VARCHAR(64) NOT NULL,
    application_description TEXT,
    stream VARCHAR(64),
    crew VARCHAR(64),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create index on application_id for faster lookups
CREATE INDEX idx_applications_application_id ON applications(application_id);

-- Create indexes on stream and crew for filtering
CREATE INDEX idx_applications_stream ON applications(stream);
CREATE INDEX idx_applications_crew ON applications(crew);
