package com.sportshoe.common

import com.sportshoe.testsupport.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.http.HttpStatus

class HealthEndpointIntegrationTest(
) : AbstractIntegrationTest() {
	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Test
	fun `health endpoint reports UP`() {
		val response = restTemplate.getForEntity(
			"/actuator/health",
			String::class.java
		)

		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(response.body).contains("\"status\":\"UP\"")
	}
}
