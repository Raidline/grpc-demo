package com.bmw.clientgrpc.handler

import com.bmw.clientgrpc.service.presentation.ClientService
import com.bmw.clientgrpc.service.GrpcClientService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class ClientHandler(private val service : ClientService,
                    private val gprcService: GrpcClientService) {

    // ------------- HTTP METHODS -------------//
    suspend fun handleGetAllBubbleTea(request : ServerRequest) : ServerResponse {
        val allTeas = service.findAllBubbleTea()

        return ServerResponse.ok()
            .bodyAndAwait(allTeas)
    }

    suspend fun handleGetBubbleTea(request : ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val bubbleTea = service.findBubbleTeaById(bubbleTeaId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGetBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = service.findBubbleTeaByIdAndStoreId(bubbleTeaId, storeId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGetAllBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = service.findAllBubbleTeaInStore(storeId)

        return ServerResponse.ok()
            .bodyAndAwait(bubbleTea)
    }

    suspend fun handleGetAllStores(request: ServerRequest) : ServerResponse {
        val stores = service.findAllStores()

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }

    suspend fun handleGetStore(request: ServerRequest) : ServerResponse {
        val store = service.findStoreById(request.extractPathVariable("id"))

        return ServerResponse.ok()
            .bodyValueAndAwait(store)
    }

    suspend fun handleGetAllStoresWithBubbleTea(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")

        val stores = service.findAllStoresWithBubbleTea(bubbleTeaId)

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }


    // ----------- GRPC METHODS ------------ //

    suspend fun handleGrpcGetAllBubbleTea(request : ServerRequest) : ServerResponse {
        val allTeas = gprcService.findGrpcAllBubbleTea()

        return ServerResponse.ok()
            .bodyAndAwait(allTeas)
    }

    suspend fun handleGrpcGetBubbleTea(request : ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val bubbleTea = gprcService.findGrpcBubbleTeaById(bubbleTeaId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGrpcGetBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = gprcService.findGrpcBubbleTeaByIdAndStoreId(bubbleTeaId, storeId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGrpcGetAllBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = gprcService.findGrpcAllBubbleTeaInStore(storeId)

        return ServerResponse.ok()
            .bodyAndAwait(bubbleTea)
    }

    suspend fun handleGrpcGetAllStores(request: ServerRequest) : ServerResponse {
        val stores = gprcService.findGrpcAllStores()

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }

    suspend fun handleGrpcGetStore(request: ServerRequest) : ServerResponse {
        val store = gprcService.findGrpcStoreById(request.extractPathVariable("id"))

        return ServerResponse.ok()
            .bodyValueAndAwait(store)
    }

    suspend fun handleGrpcGetAllStoresWithBubbleTea(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")

        val stores = gprcService.findGrpcAllStoresWithBubbleTea(bubbleTeaId)

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }


    private fun ServerRequest.extractPathVariable(name : String) = this.pathVariable(name).toLong()
}
