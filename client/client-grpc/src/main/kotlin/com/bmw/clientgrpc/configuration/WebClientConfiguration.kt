package com.bmw.clientgrpc.configuration

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.http.codec.ClientCodecConfigurer
import org.springframework.web.reactive.function.client.ExchangeStrategies
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration


const val timeout = 5

@Configuration
class WebClientConfiguration {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = ObjectMapper().registerKotlinModule().findAndRegisterModules()

    @Bean
    fun webClient() : WebClient {
        val strategies = ExchangeStrategies.builder()
            .codecs { codecs: ClientCodecConfigurer ->
                codecs.defaultCodecs().maxInMemorySize(1024 * 1024 * 12)
            }
            .build()
        return WebClient.builder()
            // to use the http part of the grpc server
            .baseUrl("http://localhost:8080/server")
            .exchangeStrategies(strategies)
            .clientConnector(
                ReactorClientHttpConnector(HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, timeout)
                    .responseTimeout(Duration.ofSeconds(timeout.toLong()))
                    .doOnConnected {
                        it
                            .addHandlerLast(ReadTimeoutHandler(timeout))
                            .addHandlerLast(WriteTimeoutHandler(timeout))
                    })
            )
            .build()
    }
}
