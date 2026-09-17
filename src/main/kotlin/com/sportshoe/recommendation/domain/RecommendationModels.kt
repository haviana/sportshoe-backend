package com.sportshoe.recommendation.domain

import java.util.UUID

/**
 * Output contracts for the recommendation engine, matching DOMAIN_MODEL.md and
 * RECOMMENDATION_ENGINE.md.
 *
 * NOTE: This module intentionally contains data shapes ONLY. No scoring, filtering,
 * ranking, or explanation logic is implemented yet - that is a future increment.
 * No AI/ML integration exists at this stage either.
 */

data class Reason(
	val attribute: String,
	val ruleId: String,
	val explanation: String,
	val weightContribution: Double,
)

data class RecommendationItem(
	val productId: UUID,
	val score: Double,
	val confidence: Confidence,
	val reasons: List<Reason> = emptyList(),
)

enum class Confidence {
	HIGH,
	MEDIUM,
	LOW,
}

data class RecommendationResult(
	val userProfileId: UUID,
	val items: List<RecommendationItem>,
)
