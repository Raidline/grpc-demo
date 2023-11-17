package com.bmw.servergrpc.router

import com.bmw.servergrpc.error.handle
import com.bmw.servergrpc.handler.StoreHttpHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class StoreHttpRouter {

    @Bean
    fun router(handler : StoreHttpHandler) = coRouter {
        "/bubble-tea".nest {
            GET("", handler::handleGetAllBubbleTea)
            GET("/{id}", handler::handleGetBubbleTea)
            GET("/{id}/store/{storeId}", handler::handleGetBubbleTeaInStore)
            GET("/store/{storeId}", handler::handleGetAllBubbleTeaInStore)
        }
        "/store".nest {
            GET("", handler::handleGetAllStores)
            GET("/{id}", handler::handleGetStore)
            GET("/bubble-tea/{id}", handler::handleGetAllStoresWithBubbleTea)
        }

        onError<Exception> {th, _ -> handle(th)}
    }
}
