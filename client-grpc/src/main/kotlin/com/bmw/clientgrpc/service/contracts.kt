package com.bmw.clientgrpc.service

import com.bmw.clientgrpc.BubbleTea
import com.bmw.clientgrpc.Store
import kotlinx.coroutines.flow.Flow

interface HttpClientService {
    fun findAllStores(): Flow<Store>
    suspend fun findStoreById(storeId: Long): Store
    fun findAllBubbleTea(): Flow<BubbleTea>
    suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea
    fun findAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea>
    fun findAllByStoreId(storeId: Long): Flow<BubbleTea>
    suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea
    fun findAllStoresWithBubbleTea(storeId : Long) : Flow<Store>
}

interface GrpcClientService {
    fun findGrpcAllStores(): Flow<Store>
    suspend fun findGrpcStoreById(storeId: Long): Store
    fun findGrpcAllBubbleTea(): Flow<BubbleTea>
    suspend fun findGrpcBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea
    fun findGrpcAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea>
    fun findGrpcAllByStoreId(storeId: Long): Flow<BubbleTea>
    suspend fun findGrpcBubbleTeaById(bubbleTeaId: Long): BubbleTea
    fun findGrpcAllStoresWithBubbleTea(storeId : Long) : Flow<Store>
}
