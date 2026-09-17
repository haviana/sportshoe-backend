package com.sportshoe.common.web

import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

/**
 * Central error handling for the whole application. Ensures every error response
 * follows the same [ErrorResponse] shape and is logged consistently.
 */
@RestControllerAdvice
class GlobalExceptionHandler {

	private val log = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

	@ExceptionHandler(ApiException::class)
	fun handleApiException(ex: ApiException, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
		log.warn("API exception handled: {} - {}", ex.status, ex.message)
		return buildResponse(ex.status, ex.message, request.requestURI)
	}

	@ExceptionHandler(MethodArgumentNotValidException::class)
	fun handleValidationException(
		ex: MethodArgumentNotValidException,
		request: HttpServletRequest,
	): ResponseEntity<ErrorResponse> {
		val fieldErrors = ex.bindingResult.fieldErrors.map { FieldErrorDetail(it.field, it.defaultMessage) }
		log.info("Validation failed for request {}: {}", request.requestURI, fieldErrors)
		val body = ErrorResponse(
			status = HttpStatus.BAD_REQUEST.value(),
			error = HttpStatus.BAD_REQUEST.reasonPhrase,
			message = "Validation failed",
			path = request.requestURI,
			fieldErrors = fieldErrors,
		)
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body)
	}

	@ExceptionHandler(MethodArgumentTypeMismatchException::class)
	fun handleTypeMismatch(
		ex: MethodArgumentTypeMismatchException,
		request: HttpServletRequest,
	): ResponseEntity<ErrorResponse> {
		val message = "Parameter '${ex.name}' has invalid value '${ex.value}'"
		log.info("Type mismatch on request {}: {}", request.requestURI, message)
		return buildResponse(HttpStatus.BAD_REQUEST, message, request.requestURI)
	}

	@ExceptionHandler(HttpMessageNotReadableException::class)
	fun handleUnreadableMessage(
		ex: HttpMessageNotReadableException,
		request: HttpServletRequest,
	): ResponseEntity<ErrorResponse> {
		log.info("Malformed request body on {}: {}", request.requestURI, ex.message)
		return buildResponse(HttpStatus.BAD_REQUEST, "Malformed request body", request.requestURI)
	}

	@ExceptionHandler(Exception::class)
	fun handleUnexpectedException(ex: Exception, request: HttpServletRequest): ResponseEntity<ErrorResponse> {
		log.error("Unhandled exception on {}", request.requestURI, ex)
		return buildResponse(
			HttpStatus.INTERNAL_SERVER_ERROR,
			"An unexpected error occurred",
			request.requestURI,
		)
	}

	private fun buildResponse(status: HttpStatus, message: String?, path: String): ResponseEntity<ErrorResponse> {
		val body = ErrorResponse(
			status = status.value(),
			error = status.reasonPhrase,
			message = message,
			path = path,
		)
		return ResponseEntity.status(status).body(body)
	}
}
