package com.sportshoe.profile

import com.sportshoe.profile.repository.UserProfileRepository
import com.sportshoe.testsupport.AbstractIntegrationTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.resttestclient.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import java.util.*

class UserProfileControllerIntegrationTest(

) : AbstractIntegrationTest() {
	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var userProfileRepository: UserProfileRepository
	private fun jsonRequest(body: String): HttpEntity<String> {
		val headers = HttpHeaders()
		headers.contentType = MediaType.APPLICATION_JSON
		return HttpEntity(body, headers)
	}

	@Test
	fun `creates a user profile and reads it back`() {
		val body = """
			{
			  "sessionId": "session-${UUID.randomUUID()}",
			  "categoryCode": "sports-shoes",
			  "activityCode": "running",
			  "attributes": {"experienceLevel": "beginner", "terrain": "road"},
			  "freeText": "I have shin pain and want cushioned shoes"
			}
		""".trimIndent()

		val createResponse = restTemplate.postForEntity("/api/profiles", jsonRequest(body), String::class.java)
		assertThat(createResponse.statusCode).isEqualTo(HttpStatus.CREATED)
		assertThat(createResponse.body).contains("\"categoryCode\":\"sports-shoes\"")

		val idMatch = Regex("\"id\":\"([0-9a-fA-F-]{36})\"").find(createResponse.body!!)
		val id = idMatch!!.groupValues[1]

		val getResponse = restTemplate.getForEntity("/api/profiles/$id", String::class.java)
		assertThat(getResponse.statusCode).isEqualTo(HttpStatus.OK)
		assertThat(getResponse.body).contains("shin pain")

		assertThat(userProfileRepository.findById(UUID.fromString(id))).isPresent
	}

	@Test
	fun `rejects a profile without a category code`() {
		val body = """
			{
			  "sessionId": "session-${UUID.randomUUID()}",
			  "categoryCode": ""
			}
		""".trimIndent()

		val response = restTemplate.postForEntity("/api/profiles", jsonRequest(body), String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
		assertThat(response.body).contains("\"fieldErrors\"")
//		assertThat(response.body).contains("categoryCode")
	}

	@Test
	fun `returns 404 for unknown profile`() {
		val response = restTemplate.getForEntity("/api/profiles/${UUID.randomUUID()}", String::class.java)

		assertThat(response.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
	}
}
