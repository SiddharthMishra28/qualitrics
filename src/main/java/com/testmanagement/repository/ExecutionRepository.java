package com.testmanagement.repository;

import com.testmanagement.entity.Execution;
import com.testmanagement.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository interface for Execution entity
 * Provides CRUD operations and custom queries for dashboard analytics
 */
@Repository
public interface ExecutionRepository extends JpaRepository<Execution, Long> {

    /**
     * Find execution by UUID
     */
    Optional<Execution> findByUuid(UUID uuid);

    /**
     * Find executions by application
     */
    List<Execution> findByApplicationOrderByCreatedAtDesc(Application application);

    /**
     * Find executions by application ID
     */
    @Query("SELECT e FROM Execution e WHERE e.application.id = :applicationId ORDER BY e.createdAt DESC")
    List<Execution> findByApplicationIdOrderByCreatedAtDesc(@Param("applicationId") Long applicationId);

    /**
     * Find executions by execution type
     */
    List<Execution> findByExecutionTypeOrderByCreatedAtDesc(Execution.ExecutionType executionType);

    /**
     * Find executions by suite category
     */
    List<Execution> findByExecutionSuiteCategoryOrderByCreatedAtDesc(Execution.ExecutionSuiteCategory executionSuiteCategory);

    /**
     * Find executions by application and execution type
     */
    @Query("SELECT e FROM Execution e WHERE e.application.id = :applicationId AND e.executionType = :executionType ORDER BY e.createdAt DESC")
    List<Execution> findByApplicationIdAndExecutionTypeOrderByCreatedAtDesc(@Param("applicationId") Long applicationId, @Param("executionType") Execution.ExecutionType executionType);

    /**
     * Find executions by application and suite category
     */
    @Query("SELECT e FROM Execution e WHERE e.application.id = :applicationId AND e.executionSuiteCategory = :suiteCategory ORDER BY e.createdAt DESC")
    List<Execution> findByApplicationIdAndSuiteCategoryOrderByCreatedAtDesc(@Param("applicationId") Long applicationId, @Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory);

    /**
     * Find executions by application, execution type, and suite category
     */
    @Query("SELECT e FROM Execution e WHERE e.application.id = :applicationId AND e.executionType = :executionType AND e.executionSuiteCategory = :suiteCategory ORDER BY e.createdAt DESC")
    List<Execution> findByApplicationIdAndExecutionTypeAndSuiteCategoryOrderByCreatedAtDesc(
        @Param("applicationId") Long applicationId, 
        @Param("executionType") Execution.ExecutionType executionType, 
        @Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory);

    /**
     * Count total executions
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countTotalExecutions(@Param("applicationId") String applicationId, @Param("stream") String stream, @Param("crew") String crew);

    /**
     * Count passed executions (where countFailed = 0 and countPassed > 0)
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.countFailed = 0 AND e.countPassed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countPassedExecutions(@Param("applicationId") String applicationId, @Param("stream") String stream, @Param("crew") String crew);

    /**
     * Count failed executions (where countFailed > 0)
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.countFailed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countFailedExecutions(@Param("applicationId") String applicationId, @Param("stream") String stream, @Param("crew") String crew);

    /**
     * Count executions by execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionType = :executionType " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countByExecutionType(@Param("executionType") Execution.ExecutionType executionType,
                              @Param("applicationId") String applicationId,
                              @Param("stream") String stream,
                              @Param("crew") String crew);

    /**
     * Count passed executions by execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionType = :executionType AND e.countFailed = 0 AND e.countPassed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countPassedByExecutionType(@Param("executionType") Execution.ExecutionType executionType,
                                    @Param("applicationId") String applicationId,
                                    @Param("stream") String stream,
                                    @Param("crew") String crew);

    /**
     * Count failed executions by execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionType = :executionType AND e.countFailed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countFailedByExecutionType(@Param("executionType") Execution.ExecutionType executionType,
                                    @Param("applicationId") String applicationId,
                                    @Param("stream") String stream,
                                    @Param("crew") String crew);

    /**
     * Count executions by suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionSuiteCategory = :suiteCategory " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countBySuiteCategory(@Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory,
                              @Param("applicationId") String applicationId,
                              @Param("stream") String stream,
                              @Param("crew") String crew);

    /**
     * Count passed executions by suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionSuiteCategory = :suiteCategory AND e.countFailed = 0 AND e.countPassed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countPassedBySuiteCategory(@Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory,
                                    @Param("applicationId") String applicationId,
                                    @Param("stream") String stream,
                                    @Param("crew") String crew);

    /**
     * Count failed executions by suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e " +
           "WHERE e.executionSuiteCategory = :suiteCategory AND e.countFailed > 0 " +
           "AND (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew)")
    Long countFailedBySuiteCategory(@Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory,
                                    @Param("applicationId") String applicationId,
                                    @Param("stream") String stream,
                                    @Param("crew") String crew);

    /**
     * Count executions by application ID
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId")
    Long countByApplicationId(@Param("applicationId") Long applicationId);

    /**
     * Count passed executions by application ID
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.countFailed = 0 AND e.countPassed > 0")
    Long countPassedByApplicationId(@Param("applicationId") Long applicationId);

    /**
     * Count failed executions by application ID
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.countFailed > 0")
    Long countFailedByApplicationId(@Param("applicationId") Long applicationId);

    /**
     * Count executions by application ID and execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionType = :executionType")
    Long countByApplicationIdAndExecutionType(@Param("applicationId") Long applicationId, @Param("executionType") Execution.ExecutionType executionType);

    /**
     * Count passed executions by application ID and execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionType = :executionType AND e.countFailed = 0 AND e.countPassed > 0")
    Long countPassedByApplicationIdAndExecutionType(@Param("applicationId") Long applicationId, @Param("executionType") Execution.ExecutionType executionType);

    /**
     * Count failed executions by application ID and execution type
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionType = :executionType AND e.countFailed > 0")
    Long countFailedByApplicationIdAndExecutionType(@Param("applicationId") Long applicationId, @Param("executionType") Execution.ExecutionType executionType);

    /**
     * Count executions by application ID and suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionSuiteCategory = :suiteCategory")
    Long countByApplicationIdAndSuiteCategory(@Param("applicationId") Long applicationId, @Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory);

    /**
     * Count passed executions by application ID and suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionSuiteCategory = :suiteCategory AND e.countFailed = 0 AND e.countPassed > 0")
    Long countPassedByApplicationIdAndSuiteCategory(@Param("applicationId") Long applicationId, @Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory);

    /**
     * Count failed executions by application ID and suite category
     */
    @Query("SELECT COUNT(e) FROM Execution e WHERE e.application.id = :applicationId AND e.executionSuiteCategory = :suiteCategory AND e.countFailed > 0")
    Long countFailedByApplicationIdAndSuiteCategory(@Param("applicationId") Long applicationId, @Param("suiteCategory") Execution.ExecutionSuiteCategory suiteCategory);

    /**
     * Get daily execution summaries for all applications.
     */
    @Query("SELECT NEW com.testmanagement.dto.DailyExecutionSummary(" +
           "CAST(e.createdAt AS java.sql.Date), " +
           "SUM(e.totalTestCases), " +
           "SUM(e.countPassed), " +
           "SUM(e.countFailed)) " +
           "FROM Execution e " +
           "WHERE (:applicationId IS NULL OR e.application.applicationId = :applicationId) " +
           "AND (:stream IS NULL OR e.application.stream = :stream) " +
           "AND (:crew IS NULL OR e.application.crew = :crew) " +
           "GROUP BY CAST(e.createdAt AS java.sql.Date) " +
           "ORDER BY CAST(e.createdAt AS java.sql.Date) ASC")
    List<com.testmanagement.dto.DailyExecutionSummary> findDailyExecutionSummaryOverall(@Param("applicationId") String applicationId, @Param("stream") String stream, @Param("crew") String crew);

    /**
     * Get test counts for a given application
     */
    @Query("SELECT NEW com.testmanagement.dto.TestCounts(" +
           "SUM(e.countPassed), " +
           "SUM(e.countFailed), " +
           "SUM(e.countSkipped)) " +
           "FROM Execution e " +
           "WHERE e.application.id = :applicationId")
    com.testmanagement.dto.TestCounts getTestCountsByApplicationId(@Param("applicationId") Long applicationId);
}
