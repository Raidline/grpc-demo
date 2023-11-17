package com.bmw.clientgrpc.data.presentation

import com.bmw.clientgrpc.BubbleTea
import com.bmw.clientgrpc.Store
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodyOrNull
import org.springframework.web.reactive.function.client.bodyToFlow

@Component
class HttpStoreApi(private val client : WebClient) {

    fun findAllStores(): Flow<Store> {
        return client
            .get()
            .uri("/store")
            .retrieve()
            .bodyToFlow<Store>()
    }

    suspend fun findStoreById(storeId: Long): Store? {
        return client.get()
            .uri {
                it.path("/store/{id}")
                    .build(storeId)
            }
            .retrieve()
            .awaitBodyOrNull()
    }

    fun findAllBubbleTea(): Flow<BubbleTea> {
        return client.get()
            .uri("/bubble-tea")
            .retrieve()
            .bodyToFlow<BubbleTea>()
    }

    suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea? {
        return client.get()
            .uri {
                it.path("/bubble-tea/{id}/store/{storeId}")
                    .build(bubbleTeaId, storeId)
            }
            .retrieve()
            .awaitBodyOrNull()
    }

    fun findAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea> {
        return client.get()
            .uri {
                it.path("/bubble-tea/store/{storeId}")
                    .build(storeId)
            }
            .retrieve()
            .bodyToFlow<BubbleTea>()
    }

    fun findAllByStoreId(storeId: Long): Flow<BubbleTea> {
        return client.get()
            .uri {
                it.path("/bubble-tea/store/{storeId}")
                    .build(storeId)
            }
            .retrieve()
            .bodyToFlow<BubbleTea>()
    }

    suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea? {
        return client.get()
            .uri {
                it.path("/bubble-tea/{id}")
                    .build(bubbleTeaId)
            }
            .retrieve()
            .awaitBodyOrNull()
    }

    fun findAllStoresWithBubbleTea(storeId: Long): Flow<Store> {
        return client.get()
            .uri {
                it.path("/store/bubble-tea/{id}")
                    .build(storeId)
            }
            .retrieve()
            .bodyToFlow<Store>()
    }
}
