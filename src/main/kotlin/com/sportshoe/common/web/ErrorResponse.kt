package com.sportshoe.common.web

import java.time.Instant

/**
 * Standard error response body returned by the API for all error cases.
 */
data class ErrorResponse(
	val timestamp: Instant = Instant.now(),
	val status: Int,
	val error: String,
	val message: String?,
	val path: String,
	val fieldErrors: List<FieldErrorDetail> = emptyList(),
)

data class FieldErrorDetail(
	val field: String,
	val message: String?,
)
