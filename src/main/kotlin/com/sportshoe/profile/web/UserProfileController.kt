package com.sportshoe.profile.web

import com.sportshoe.common.web.ResourceNotFoundException
import com.sportshoe.profile.domain.UserProfile
import com.sportshoe.profile.dto.CreateUserProfileRequest
import com.sportshoe.profile.dto.UserProfileResponse
import com.sportshoe.profile.repository.UserProfileRepository
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

/**
 * Collects user profiles (structured answers + optional free text). This module is
 * intentionally limited to persistence for now - no recommendation logic is invoked here.
 */
@RestController
class UserProfileController(
	private val userProfileRepository: UserProfileRepository,
) {

	@PostMapping("/api/profiles")
	fun createProfile(@Valid @RequestBody request: CreateUserProfileRequest): ResponseEntity<UserProfileResponse> {
		val profile = UserProfile(
			sessionId = request.sessionId,
			categoryCode = request.categoryCode,
			activityCode = request.activityCode,
			attributes = request.attributes.toMutableMap(),
			freeText = request.freeText,
		)
		val saved = userProfileRepository.save(profile)
		return ResponseEntity.status(HttpStatus.CREATED).body(UserProfileResponse.from(saved))
	}

	@GetMapping("/api/profiles/{id}")
	fun getProfile(@PathVariable id: UUID): UserProfileResponse {
		val profile = userProfileRepository.findById(id)
			.orElseThrow { ResourceNotFoundException("User profile $id not found") }
		return UserProfileResponse.from(profile)
	}
}
