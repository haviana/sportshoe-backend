package com.sportshoe.common.web

import org.springframework.http.HttpStatus

/**
 * Base class for domain-level exceptions that should be translated to a specific HTTP status.
 */
abstract class ApiException(
	message: String,
	val status: HttpStatus,
) : RuntimeException(message)

/**
 * Thrown when a requested resource does not exist.
 */
class ResourceNotFoundException(message: String) : ApiException(message, HttpStatus.NOT_FOUND)

/**
 * Thrown when a request is well-formed but violates a business rule.
 */
class InvalidRequestException(message: String) : ApiException(message, HttpStatus.BAD_REQUEST)
