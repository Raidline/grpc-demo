package com.bmw.clientgrpc.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

val logger: Logger = LoggerFactory.getLogger("Error Handler")

suspend fun Throwable.handle() = when (this) {
    is BaseException -> serverResponse(this.toError())
    else -> serverResponse(this.defaultErrorResponse())
}.also { logger.error(this.message) }

private suspend fun serverResponse(response: ErrorResponse) = ServerResponse
    .status(HttpStatus.valueOf(response.code))
    .bodyValueAndAwait(response)

private fun Throwable.defaultErrorResponse() = ErrorResponse(
    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    error = this.extractMessage()
)

private fun Throwable.extractMessage(): String = this.message ?: "An error has occurred"

private fun BaseException.toError() = ErrorResponse(
    code = this.code,
    error = this.message ?: "An error has occurred"
)

data class ErrorResponse(
    val code: Int,
    val error: String
)
