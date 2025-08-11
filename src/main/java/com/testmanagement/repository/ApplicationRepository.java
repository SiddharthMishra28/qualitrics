package com.testmanagement.repository;

import com.testmanagement.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Application entity
 * Provides CRUD operations and custom queries
 */
@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    /**
     * Find application by application ID
     */
    Optional<Application> findByApplicationId(String applicationId);

    /**
     * Find applications by stream
     */
    List<Application> findByStream(String stream);

    /**
     * Find applications by crew
     */
    List<Application> findByCrew(String crew);

    /**
     * Find applications by stream and crew
     */
    List<Application> findByStreamAndCrew(String stream, String crew);

    /**
     * Check if application exists by application ID
     */
    boolean existsByApplicationId(String applicationId);

    /**
     * Find all applications ordered by application name
     */
    @Query("SELECT a FROM Application a ORDER BY a.applicationName ASC")
    List<Application> findAllOrderByApplicationName();

    /**
     * Find applications by stream ordered by application name
     */
    @Query("SELECT a FROM Application a WHERE a.stream = :stream ORDER BY a.applicationName ASC")
    List<Application> findByStreamOrderByApplicationName(@Param("stream") String stream);

    /**
     * Find applications by crew ordered by application name
     */
    @Query("SELECT a FROM Application a WHERE a.crew = :crew ORDER BY a.applicationName ASC")
    List<Application> findByCrewOrderByApplicationName(@Param("crew") String crew);

    /**
     * Find applications by stream and crew ordered by application name
     */
    @Query("SELECT a FROM Application a WHERE a.stream = :stream AND a.crew = :crew ORDER BY a.applicationName ASC")
    List<Application> findByStreamAndCrewOrderByApplicationName(@Param("stream") String stream, @Param("crew") String crew);

    /**
     * Find all distinct stream names.
     */
    @Query("SELECT DISTINCT a.stream FROM Application a WHERE a.stream IS NOT NULL ORDER BY a.stream ASC")
    List<String> findDistinctStreams();

    /**
     * Find all distinct crew names.
     */
    @Query("SELECT DISTINCT a.crew FROM Application a WHERE a.crew IS NOT NULL ORDER BY a.crew ASC")
    List<String> findDistinctCrews();
}
