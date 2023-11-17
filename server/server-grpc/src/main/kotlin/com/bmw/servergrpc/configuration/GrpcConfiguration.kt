package com.bmw.servergrpc.configuration

import io.grpc.Server
import jakarta.annotation.PreDestroy
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration

@Configuration
class GrpcConfiguration(private val server: Server) {

    private companion object {
        val log: Logger = LoggerFactory.getLogger(GrpcConfiguration::class.java)
    }

    init {
        server.start()
        log.info("GRPC Server started, listening on 8090")
    }

    @PreDestroy
    fun stop() {
        server.shutdown()
        server.awaitTermination()
    }
}
