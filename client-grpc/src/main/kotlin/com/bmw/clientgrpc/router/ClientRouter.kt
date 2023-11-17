package com.bmw.clientgrpc.router

import com.bmw.clientgrpc.exception.handle
import com.bmw.clientgrpc.handler.ClientHandler
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.coRouter

@Component
class ClientRouter {

    @Bean
    fun router(handler: ClientHandler) = coRouter {
        "/rest".nest {
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
        }

        "/grpc".nest {
            "/bubble-tea".nest {
                GET("", handler::handleGrpcGetAllBubbleTea)
                GET("/{id}", handler::handleGrpcGetBubbleTea)
                GET("/{id}/store/{storeId}", handler::handleGrpcGetBubbleTeaInStore)
                GET("/store/{storeId}", handler::handleGrpcGetAllBubbleTeaInStore)
            }
            "/store".nest {
                GET("", handler::handleGrpcGetAllStores)
                GET("/{id}", handler::handleGrpcGetStore)
                GET("/bubble-tea/{id}", handler::handleGrpcGetAllStoresWithBubbleTea)
            }
        }

        onError<Exception> {th, _ -> th.handle()}
    }
}
