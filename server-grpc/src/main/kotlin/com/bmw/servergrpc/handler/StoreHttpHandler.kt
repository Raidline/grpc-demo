package com.bmw.servergrpc.handler

import com.bmw.servergrpc.service.presentation.StoreHttpService
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyAndAwait
import org.springframework.web.reactive.function.server.bodyValueAndAwait

@Component
class StoreHttpHandler(private val storeService: StoreHttpService) {

    suspend fun handleGetAllBubbleTea(request : ServerRequest) : ServerResponse {
        val allTeas = storeService.findAllBubbleTea()

        return ServerResponse.ok()
            .bodyAndAwait(allTeas)
    }

    suspend fun handleGetBubbleTea(request : ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val bubbleTea = storeService.findBubbleTea(bubbleTeaId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGetBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = storeService.findBubbleTeaByStore(bubbleTeaId, storeId)

        return ServerResponse.ok()
            .bodyValueAndAwait(bubbleTea)
    }

    suspend fun handleGetAllBubbleTeaInStore(request: ServerRequest) : ServerResponse {
        val storeId = request.extractPathVariable("storeId")

        val bubbleTea = storeService.findAllBubbleTeaByStore(storeId)

        return ServerResponse.ok()
            .bodyAndAwait(bubbleTea)
    }

    suspend fun handleGetAllStores(request: ServerRequest) : ServerResponse {
        val stores = storeService.findAllStores()

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }

    suspend fun handleGetStore(request: ServerRequest) : ServerResponse {
        val store = storeService.findStore(request.extractPathVariable("id"))

        return ServerResponse.ok()
            .bodyValueAndAwait(store)
    }

    suspend fun handleGetAllStoresWithBubbleTea(request: ServerRequest) : ServerResponse {
        val bubbleTeaId = request.extractPathVariable("id")

        val stores = storeService.findAllStoresWithBubbleTea(bubbleTeaId)

        return ServerResponse.ok()
            .bodyAndAwait(stores)
    }


    private fun ServerRequest.extractPathVariable(name : String) = this.pathVariable(name).toLong()
}
