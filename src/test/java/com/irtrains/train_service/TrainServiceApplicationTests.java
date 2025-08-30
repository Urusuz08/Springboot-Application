package com.irtrains.train_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.sql.DataSource;

@SpringJUnitConfig(TrainServiceApplicationTests.TestConfig.class)
class TrainServiceApplicationTests {
		@Autowired
		private JdbcTemplate jdbcTemplate;

		@TestConfiguration
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
		void testDatabaseConnection() {
			jdbcTemplate.execute("SELECT 1");
			System.out.println("Database connection Successful");
		}
}
