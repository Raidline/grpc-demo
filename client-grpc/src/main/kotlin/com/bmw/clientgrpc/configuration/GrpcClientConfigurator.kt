package com.bmw.clientgrpc.configuration

import com.bmw.servergrpc.store.StoreGrpc
import com.bmw.servergrpc.store.StoreGrpcKt
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.Executors

@Configuration
class GrpcClientConfigurator {

    @Bean
    fun channel() : ManagedChannel {
        return ManagedChannelBuilder.forAddress(
            "localhost",
            8090
        ).usePlaintext()
            .maxInboundMessageSize(1024 * 1024 * 12)
            .directExecutor()
            .keepAliveTime(20, java.util.concurrent.TimeUnit.SECONDS)
            .build()
    }

    @Bean
    fun api(channel : ManagedChannel) : StoreGrpcKt.StoreCoroutineStub {
        return StoreGrpcKt.StoreCoroutineStub(channel)
    }

    @Bean
    fun javaApi(channel : ManagedChannel) : StoreGrpc.StoreStub {
        return StoreGrpc.newStub(channel)
    }
}
