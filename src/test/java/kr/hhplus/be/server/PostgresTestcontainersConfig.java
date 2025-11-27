package kr.hhplus.be.server;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.PostgreSQLContainer;

@TestConfiguration
public class PostgresTestcontainersConfig {

	@Bean
	@ServiceConnection
	public PostgreSQLContainer<?> postgresqlContainer() {
		return new PostgreSQLContainer<>("postgres:15")
			.withDatabaseName("hhplus")
			.withUsername("test")
			.withPassword("test");
	}

}