package com.sportshoe.profile.dto

import com.sportshoe.profile.domain.UserProfile
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import java.time.Instant
import java.util.UUID

/**
 * Request body for creating a user profile from structured question answers and
 * optional free text. No recommendation logic is triggered by this endpoint yet.
 */
data class CreateUserProfileRequest(

	@field:NotBlank(message = "sessionId must not be blank")
	@field:Size(max = 128, message = "sessionId must be at most 128 characters")
	val sessionId: String,

	@field:NotBlank(message = "categoryCode must not be blank")
	@field:Size(max = 64, message = "categoryCode must be at most 64 characters")
	val categoryCode: String,

	@field:Size(max = 64, message = "activityCode must be at most 64 characters")
	val activityCode: String? = null,

	val attributes: Map<String, Any?> = emptyMap(),

	@field:Size(max = 2000, message = "freeText must be at most 2000 characters")
	val freeText: String? = null,
)

data class UserProfileResponse(
	val id: UUID,
	val sessionId: String,
	val categoryCode: String,
	val activityCode: String?,
	val attributes: Map<String, Any?>,
	val freeText: String?,
	val extractedSignals: List<Map<String, Any?>>,
	val createdAt: Instant,
) {
	companion object {
		fun from(profile: UserProfile) = UserProfileResponse(
			id = profile.id!!,
			sessionId = profile.sessionId,
			categoryCode = profile.categoryCode,
			activityCode = profile.activityCode,
			attributes = profile.attributes,
			freeText = profile.freeText,
			extractedSignals = profile.extractedSignals,
			createdAt = profile.createdAt!!,
		)
	}
}
