package com.sportshoe.common.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.UUID

/**
 * Logs every incoming HTTP request/response with a correlation id, method, path,
 * status code and duration. Correlation id is echoed back in the response headers
 * so clients/logs can be tied together across services.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class RequestLoggingFilter : OncePerRequestFilter() {

	private val log = LoggerFactory.getLogger(RequestLoggingFilter::class.java)

	companion object {
		const val CORRELATION_ID_HEADER = "X-Correlation-Id"
	}

	override fun doFilterInternal(
		request: HttpServletRequest,
		response: HttpServletResponse,
		filterChain: FilterChain,
	) {
		val correlationId = request.getHeader(CORRELATION_ID_HEADER) ?: UUID.randomUUID().toString()
		response.setHeader(CORRELATION_ID_HEADER, correlationId)
		val startedAt = System.currentTimeMillis()

		try {
			org.slf4j.MDC.put("correlationId", correlationId)
			filterChain.doFilter(request, response)
		} finally {
			val durationMs = System.currentTimeMillis() - startedAt
			log.info(
				"{} {} -> {} ({} ms)",
				request.method,
				request.requestURI,
				response.status,
				durationMs,
			)
			org.slf4j.MDC.remove("correlationId")
		}
	}
}
