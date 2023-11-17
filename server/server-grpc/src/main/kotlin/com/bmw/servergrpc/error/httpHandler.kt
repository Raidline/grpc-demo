package com.bmw.servergrpc.error

import com.bmw.servergrpc.exception.BaseException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

val logger: Logger = LoggerFactory.getLogger("Error Handler")

suspend fun handle(error: Throwable) = when (error) {
    is BaseException -> serverResponse(error.toError())
    else -> serverResponse(error.defaultErrorResponse())
}.also { logger.error(error.message) }

private suspend fun serverResponse(response: ErrorResponse) = ServerResponse
    .status(HttpStatus.valueOf(response.code))
    .bodyValueAndAwait(response)

fun Throwable.defaultErrorResponse() = ErrorResponse(
    code = HttpStatus.INTERNAL_SERVER_ERROR.value(),
    error = this.extractMessage()
)

private fun Throwable.extractMessage(): String = this.message ?: "An error has occurred"

fun BaseException.toError() = ErrorResponse(
    code = this.code,
    error = this.message ?: "An error has occurred"
)

data class ErrorResponse(
    val code: Int,
    val error: String
)
