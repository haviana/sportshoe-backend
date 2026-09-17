package com.sportshoe.testsupport

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.testcontainers.containers.PostgreSQLContainer

/**
 * Base class for integration tests that need a real PostgreSQL database.
 *
 * The Postgres container is started once, manually, as a JVM-wide singleton (NOT managed
 * by the JUnit Testcontainers extension) so it stays up and keeps the same mapped port for
 * every test class in the run - this is the standard Testcontainers "singleton container"
 * pattern. It is torn down automatically by Ryuk when the JVM exits.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class AbstractIntegrationTest {

	companion object {

		@ServiceConnection
		@JvmStatic
		val postgres: PostgreSQLContainer<*> = PostgreSQLContainer("postgres:16-alpine")
			.withDatabaseName("sportshoe_test")
			.withUsername("sportshoe_test")
			.withPassword("sportshoe_test")
			.apply { start() }
	}
}
