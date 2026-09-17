package com.sportshoe.profile.domain

import com.sportshoe.common.domain.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.type.SqlTypes

/**
 * Normalized, category-agnostic profile built from structured question answers plus any
 * signals extracted from free text. This is the single input contract the (future)
 * recommendation engine will consume. See DOMAIN_MODEL.md - UserProfile.
 */
@Entity
@Table(name = "user_profiles")
class UserProfile(

	@Column(name = "session_id", nullable = false, length = 128)
	var sessionId: String,

	@Column(name = "category_code", nullable = false, length = 64)
	var categoryCode: String,

	@Column(name = "activity_code", length = 64)
	var activityCode: String? = null,

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "attributes", nullable = false)
	var attributes: MutableMap<String, Any?> = mutableMapOf(),

	@Column(name = "free_text")
	var freeText: String? = null,

	@JdbcTypeCode(SqlTypes.JSON)
	@Column(name = "extracted_signals", nullable = false)
	var extractedSignals: MutableList<Map<String, Any?>> = mutableListOf(),

) : BaseEntity()
