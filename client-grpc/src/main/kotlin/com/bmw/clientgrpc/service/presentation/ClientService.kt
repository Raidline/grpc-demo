package com.bmw.clientgrpc.service.presentation

import com.bmw.clientgrpc.BubbleTea
import com.bmw.clientgrpc.Store
import com.bmw.clientgrpc.data.presentation.HttpStoreApi
import com.bmw.clientgrpc.exception.NotFoundException
import com.bmw.clientgrpc.service.HttpClientService
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class ClientService(private val httpClient: HttpStoreApi) : HttpClientService {
        
    override fun findAllStores(): Flow<Store> {
        return this.httpClient.findAllStores()
    }

    override suspend fun findStoreById(storeId: Long): Store {
        return this.httpClient.findStoreById(storeId) ?: throw NotFoundException("Store not found for id $storeId")
    }

    override fun findAllBubbleTea(): Flow<BubbleTea> {
        return this.httpClient.findAllBubbleTea()
    }

    override suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea {
        return this.httpClient.findBubbleTeaByIdAndStoreId(bubbleTeaId, storeId)
            ?: throw NotFoundException("Bubble Tea not found for store $storeId")
    }

    override fun findAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea> {
        return this.httpClient.findAllBubbleTeaInStore(storeId)
    }

    override fun findAllByStoreId(storeId: Long): Flow<BubbleTea> {
        return this.httpClient.findAllByStoreId(storeId)
    }

    override suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea {
        return this.httpClient.findBubbleTeaById(bubbleTeaId)
            ?: throw NotFoundException("Bubble Tea not found for id $bubbleTeaId")
    }

    override fun findAllStoresWithBubbleTea(storeId: Long): Flow<Store> {
        return this.httpClient.findAllStoresWithBubbleTea(storeId)
    }
}
