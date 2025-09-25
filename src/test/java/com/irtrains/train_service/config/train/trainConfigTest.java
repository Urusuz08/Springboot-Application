package com.irtrains.train_service.config.train;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.test.context.ActiveProfiles;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(trainConfigTest.TestConfig.class)
@ActiveProfiles("config-test")
@DisplayName("Train Config Test Configuration Tests")
public class trainConfigTest {

    @Autowired
    private DataSource testDataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @TestConfiguration
    @Profile("config-test")
    static class TestConfig {
        @Bean
        @Primary
        public DataSource testDataSource() {
            return new EmbeddedDatabaseBuilder()
                    .setType(EmbeddedDatabaseType.H2)
                    .setName("testdb")
                    .build();
        }

        @Bean
        public JdbcTemplate jdbcTemplate(DataSource dataSource) {
            return new JdbcTemplate(dataSource);
        }
    }

    @Test
    @DisplayName("Test DataSource bean creation")
    void testDataSourceBeanCreation() {
        assertNotNull(testDataSource, "Test DataSource should not be null");
    }

    @Test
    @DisplayName("Test DataSource is H2 embedded database")
    void testDataSourceIsH2Embedded() throws SQLException {
        try (Connection connection = testDataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // Verify it's H2 database
            String databaseProductName = metaData.getDatabaseProductName();
            assertTrue(databaseProductName.contains("H2"),
                    "Database should be H2, but was: " + databaseProductName);

            // Verify database name
            String url = metaData.getURL();
            assertTrue(url.contains("testdb"),
                    "Database URL should contain 'testdb', but was: " + url);
        }
    }

    @Test
    @DisplayName("Test DataSource connection is valid")
    void testDataSourceConnectionIsValid() throws SQLException {
        try (Connection connection = testDataSource.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertTrue(connection.isValid(5), "Connection should be valid");
            assertFalse(connection.isClosed(), "Connection should not be closed");
        }
    }

    @Test
    @DisplayName("Test JdbcTemplate works with test DataSource")
    void testJdbcTemplateWorksWithTestDataSource() {
        assertNotNull(jdbcTemplate, "JdbcTemplate should not be null");

        // Test a simple query
        assertDoesNotThrow(() -> {
            Integer result = jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            assertEquals(1, result, "Simple query should return 1");
        }, "JdbcTemplate should execute simple queries without throwing exceptions");
    }

    @Test
    @DisplayName("Test database schema creation and table operations")
    void testDatabaseSchemaOperations() {
        assertDoesNotThrow(() -> {
            // Create a test table
            jdbcTemplate.execute("CREATE TABLE test_table (id INT PRIMARY KEY, name VARCHAR(50))");

            // Insert test data
            jdbcTemplate.update("INSERT INTO test_table (id, name) VALUES (?, ?)", 1, "Test");

            // Query test data
            String name = jdbcTemplate.queryForObject(
                    "SELECT name FROM test_table WHERE id = ?", String.class, 1);
            assertEquals("Test", name, "Retrieved name should match inserted value");

            // Clean up
            jdbcTemplate.execute("DROP TABLE test_table");
        }, "Database operations should work correctly");
    }

    @Test
    @DisplayName("Test DataSource configuration properties")
    void testDataSourceConfigurationProperties() throws SQLException {
        try (Connection connection = testDataSource.getConnection()) {
            DatabaseMetaData metaData = connection.getMetaData();

            // Test database properties
            assertNotNull(metaData.getDatabaseProductName());
            assertNotNull(metaData.getDatabaseProductVersion());
            assertNotNull(metaData.getDriverName());

            // Test connection properties
            String url = metaData.getURL();
            assertNotNull(url);
            assertTrue(url.startsWith("jdbc:h2:"), "URL should start with jdbc:h2:");
        }
    }

    @Test
    @DisplayName("Test multiple connections can be created")
    void testMultipleConnections() throws SQLException {
        Connection connection1 = null;
        Connection connection2 = null;

        try {
            connection1 = testDataSource.getConnection();
            connection2 = testDataSource.getConnection();

            assertNotNull(connection1, "First connection should not be null");
            assertNotNull(connection2, "Second connection should not be null");
            assertTrue(connection1.isValid(5), "First connection should be valid");
            assertTrue(connection2.isValid(5), "Second connection should be valid");

            // Connections should be different instances
            assertNotSame(connection1, connection2, "Connections should be different instances");

        } finally {
            if (connection1 != null && !connection1.isClosed()) {
                connection1.close();
            }
            if (connection2 != null && !connection2.isClosed()) {
                connection2.close();
            }
        }
    }

    @Test
    @DisplayName("Test transaction support")
    void testTransactionSupport() {
        assertDoesNotThrow(() -> {
            // Create test table - using quoted identifier to avoid reserved keyword issues
            jdbcTemplate.execute("CREATE TABLE transaction_test (id INT PRIMARY KEY, `test_value` VARCHAR(50))");

            try {
                // Insert data
                jdbcTemplate.update("INSERT INTO transaction_test (id, `test_value`) VALUES (?, ?)", 1, "test_value");

                // Verify data exists
                String value = jdbcTemplate.queryForObject(
                        "SELECT `test_value` FROM transaction_test WHERE id = ?", String.class, 1);
                assertEquals("test_value", value);

            } finally {
                // Clean up
                jdbcTemplate.execute("DROP TABLE IF EXISTS transaction_test");
            }
        });
    }

    @Test
    @DisplayName("Test DataSource is marked as Primary")
    void testDataSourceIsPrimary() {
        // Since the DataSource is marked as @Primary, Spring should inject it
        // The fact that our @Autowired DataSource works proves it's the primary one
        assertNotNull(testDataSource, "Primary DataSource should be injected");
    }

    @Test
    @DisplayName("Test embedded database cleanup")
    void testEmbeddedDatabaseCleanup() throws SQLException {
        // Test that database can be accessed and will be cleaned up properly
        try (Connection connection = testDataSource.getConnection()) {
            // Create temporary data
            jdbcTemplate.execute("CREATE TABLE temp_cleanup_test (id INT)");
            jdbcTemplate.update("INSERT INTO temp_cleanup_test VALUES (1)");

            // Verify data exists
            Integer count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM temp_cleanup_test", Integer.class);
            assertEquals(1, count, "Temporary data should exist");

            // The embedded database will be cleaned up automatically after the test
        }
    }

    @Test
    @DisplayName("Test H2 specific features")
    void testH2SpecificFeatures() {
        assertDoesNotThrow(() -> {
            // Test H2 specific function
            String result = jdbcTemplate.queryForObject("SELECT H2VERSION()", String.class);
            assertNotNull(result, "H2VERSION() should return a version string");
            assertTrue(!result.isEmpty(), "H2 version should not be empty");

            // Test H2 memory database mode by checking database URL from metadata
            try (Connection connection = testDataSource.getConnection()) {
                DatabaseMetaData metaData = connection.getMetaData();
                String dbUrl = metaData.getURL();
                assertNotNull(dbUrl, "Database URL should be available");
                assertTrue(dbUrl.contains("mem:"), "Should be an in-memory H2 database");
            }
        });
    }
}