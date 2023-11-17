package com.bmw.clientgrpc.data.presentation

import com.bmw.clientgrpc.BubbleTea
import com.bmw.clientgrpc.Store
import com.bmw.servergrpc.store.BubbleTeaReply
import com.bmw.servergrpc.store.StoreGrpcKt
import com.bmw.servergrpc.store.StoreNoBubbleReply
import com.bmw.servergrpc.store.StoreReply
import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Component

//Exceptions are thrown inside the suspend methods (that is why we need to try catch them)

@Component
class GrpcStoreApi(val api: StoreGrpcKt.StoreCoroutineStub) {

    fun findAllStores(): Flow<Store> {
        return api.findAllStores(Empty.getDefaultInstance())
            .flowOn(Dispatchers.IO)
            .map { it.mapStoreReplyToStore() }
    }

    suspend fun findStoreById(storeId: Long): Store? {
        return null
    }

    fun findAllBubbleTea(): Flow<BubbleTea> {
        return emptyFlow()
    }

    fun findAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea> {
        return emptyFlow()
    }

    suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea? {
        return null
    }

    fun findAllByStoreId(storeId: Long): Flow<BubbleTea> {
        return emptyFlow()
    }

    suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea? {
        return null
    }
}

fun StoreReply.mapStoreReplyToStore() = this.let { storeReply ->
    Store(
        id = storeReply.id,
        name = storeReply.name,
        address = storeReply.address,
        phone = storeReply.phone,
        teas = storeReply.bubbleTeasList.map { it.mapToBubbleTea() }
    )
}

fun StoreNoBubbleReply.mapStoreReplyToStore() = this.let {
    Store(
        id = it.id,
        name = it.name,
        address = it.address,
        phone = it.phone
    )
}

fun BubbleTeaReply.mapToBubbleTea() = this.let {
    BubbleTea(
        id = it.id,
        name = it.name,
        description = it.description,
        price = it.price,
        storeId = it.storeId.toLong()
    )
}
