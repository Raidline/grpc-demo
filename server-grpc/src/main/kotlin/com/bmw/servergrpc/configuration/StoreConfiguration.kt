package com.bmw.servergrpc.configuration

import com.bmw.servergrpc.data.presentation.StoreRepo
import com.bmw.servergrpc.error.GrpcErrorHandler
import com.bmw.servergrpc.service.presentation.StoreGrpcService
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.grpc.Server
import io.grpc.ServerBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class StoreConfiguration {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule().findAndRegisterModules()

    @Bean
    fun grpcServer(repo : StoreRepo): Server = ServerBuilder.forPort(8090)
        .addService(StoreGrpcService(repo))
        .intercept(GrpcErrorHandler())
        .directExecutor()
        .build()
}
