package com.testmanagement.service;

import com.testmanagement.dto.ApplicationDto;
import com.testmanagement.entity.Application;
import com.testmanagement.exception.ApplicationNotFoundException;
import com.testmanagement.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service class for Application entity
 * Handles business logic for application management
 */
@Service
@Transactional
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    /**
     * Register a new application
     */
    public ApplicationDto registerApplication(ApplicationDto applicationDto) {
        // Check if application already exists
        if (applicationRepository.existsByApplicationId(applicationDto.getApplicationId())) {
            throw new IllegalArgumentException("Application with ID " + applicationDto.getApplicationId() + " already exists");
        }

        Application application = convertToEntity(applicationDto);
        Application savedApplication = applicationRepository.save(application);
        return convertToDto(savedApplication);
    }

    /**
     * Get all applications
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getAllApplications() {
        List<Application> applications = applicationRepository.findAllOrderByApplicationName();
        return applications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get application by application ID
     */
    @Transactional(readOnly = true)
    public ApplicationDto getApplicationByApplicationId(String applicationId) {
        Application application = applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with ID: " + applicationId));
        return convertToDto(application);
    }

    /**
     * Get applications by stream
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByStream(String stream) {
        List<Application> applications = applicationRepository.findByStreamOrderByApplicationName(stream);
        return applications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get applications by crew
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByCrew(String crew) {
        List<Application> applications = applicationRepository.findByCrewOrderByApplicationName(crew);
        return applications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get applications by stream and crew
     */
    @Transactional(readOnly = true)
    public List<ApplicationDto> getApplicationsByStreamAndCrew(String stream, String crew) {
        List<Application> applications = applicationRepository.findByStreamAndCrewOrderByApplicationName(stream, crew);
        return applications.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Get application entity by application ID (for internal use)
     */
    @Transactional(readOnly = true)
    public Application getApplicationEntityByApplicationId(String applicationId) {
        return applicationRepository.findByApplicationId(applicationId)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with ID: " + applicationId));
    }

    /**
     * Get application entity by ID (for internal use)
     */
    @Transactional(readOnly = true)
    public Application getApplicationEntityById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ApplicationNotFoundException("Application not found with ID: " + id));
    }

    /**
     * Convert Application entity to DTO
     */
    private ApplicationDto convertToDto(Application application) {
        return new ApplicationDto(
                application.getId(),
                application.getApplicationId(),
                application.getApplicationName(),
                application.getApplicationDescription(),
                application.getStream(),
                application.getCrew()
        );
    }

    /**
     * Convert DTO to Application entity
     */
    private Application convertToEntity(ApplicationDto applicationDto) {
        Application application = new Application();
        application.setApplicationId(applicationDto.getApplicationId());
        application.setApplicationName(applicationDto.getApplicationName());
        application.setApplicationDescription(applicationDto.getApplicationDescription());
        application.setStream(applicationDto.getStream());
        application.setCrew(applicationDto.getCrew());
        return application;
    }

    /**
     * Get all distinct stream names.
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctStreams() {
        return applicationRepository.findDistinctStreams();
    }

    /**
     * Get all distinct crew names.
     */
    @Transactional(readOnly = true)
    public List<String> getDistinctCrews() {
        return applicationRepository.findDistinctCrews();
    }
}
