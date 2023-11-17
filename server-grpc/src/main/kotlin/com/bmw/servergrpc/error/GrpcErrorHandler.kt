package com.bmw.servergrpc.error

import com.bmw.servergrpc.exception.BaseException
import com.google.protobuf.Any.*
import io.grpc.ForwardingServerCallListener
import io.grpc.Metadata
import io.grpc.ServerCall
import io.grpc.ServerCallHandler
import io.grpc.ServerInterceptor
import io.grpc.Status
import io.grpc.protobuf.StatusProto
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class GrpcErrorHandler : ServerInterceptor {

    override fun <ReqT : Any, RespT : Any> interceptCall(
        call: ServerCall<ReqT, RespT>,
        headers: Metadata,
        next: ServerCallHandler<ReqT, RespT>
    ): ServerCall.Listener<ReqT> = ExceptionHandler(next.startCall(call, headers), call, headers)


    inner class ExceptionHandler<T, R>(
        listener: ServerCall.Listener<T>,
        private val delegate: ServerCall<T, R>,
        private val headers: Metadata
    ) :
        ForwardingServerCallListener.SimpleForwardingServerCallListener<T>(listener) {

        private val log: Logger = LoggerFactory.getLogger(ExceptionHandler::class.java)

        override fun onHalfClose() {
            try {
                super.onHalfClose()
            } catch (e: Exception) {
                log.error("Error occurred while processing request", e)
                handleException(e)
            }
        }

        private fun handleException(e: Exception) {
            val error = when(e) {
                is BaseException -> e.toError()
                else -> e.defaultErrorResponse()
            }

            val errorInfo = com.google.rpc.ErrorInfo.newBuilder()
                .setReason(error.error)
                .setDomain("Store")
                .putAllMetadata(mapOf("code" to error.code.toString()))
                .build()

            val gprcStatus = com.google.rpc.Status.newBuilder()
                .setCode(error.code)
                .setMessage(error.error)
                .addDetails(pack(errorInfo))
                .build()

            val statusRuntimeException = StatusProto.toStatusRuntimeException(gprcStatus)

            delegate.close(Status.fromThrowable(statusRuntimeException), statusRuntimeException.trailers)
        }
    }
}
