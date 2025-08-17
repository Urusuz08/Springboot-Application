package com.irtrains.train_service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
class TrainServiceApplicationTests {
		@Autowired
		private JdbcTemplate jdbcTemplate;

		@Test
		void testDatabaseConnection() {
			jdbcTemplate.execute("SELECT 1");
			System.out.println("Database connection Successful");

		}


}
