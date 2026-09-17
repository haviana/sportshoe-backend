package com.sportshoe.common

import com.sportshoe.testsupport.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

class HealthEndpointIntegrationTest(
	@Autowired private val restTemplate: TestRestTemplate,
) : AbstractIntegrationTest() {

	@Test
	fun `health endpoint reports UP`() {
		val response = restTemplate.getForEntity("/actuator/health", String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(response.body).contains("\"status\":\"UP\"")
	}
}
