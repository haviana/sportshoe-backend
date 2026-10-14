package com.sportshoe.testsupport

import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureTestRestTemplate
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.postgresql.PostgreSQLContainer

@SpringBootTest(
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureTestRestTemplate
abstract class AbstractIntegrationTest {

	companion object {
		@ServiceConnection
		@JvmStatic
		val postgres: PostgreSQLContainer =
			PostgreSQLContainer("postgres:16-alpine")
				.withDatabaseName("sportshoe_test")
				.withUsername("sportshoe_test")
				.withPassword("sportshoe_test")
				.also { it.start() }
	}
}