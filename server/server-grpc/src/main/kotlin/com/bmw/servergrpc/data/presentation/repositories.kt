package com.bmw.servergrpc.data.presentation

import com.bmw.servergrpc.data.BubbleTea
import com.bmw.servergrpc.data.Store
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.springframework.stereotype.Repository

interface StoreRepo {
    fun findAllStores(): Flow<Store>
    suspend fun findStoreById(storeId: Long): Store?
    fun findAllBubbleTea(): Flow<BubbleTea>
    suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea?
    fun findAllByStoreId(storeId: Long): Flow<BubbleTea>
    suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea?
}

@Repository
class StoreRepoImpl : StoreRepo {
    private val storeGenerator = { id: Int ->
        Store(
            id = id.toLong(),
            name = "Store $id",
            address = "Address $id",
            phone = "Phone $id"
        )
    }

    private val teaGenerator: (id: Int, storeId: Long) -> BubbleTea =
        { id, storeId ->
            BubbleTea(
                id = id.toLong(),
                name = "Bubble Tea $id",
                description = "Bubble Tea $id description",
                price = 6.0,
                storeId = storeId
            )
        }

    private val stores = lazy {
        (1..100).map { storeGenerator(it) }
            .associateBy { it.id }
    }


    //Http cannot handle more than 500 because of max bytes (need to configure that and i'm lazy)
    private val bubbleTeas = lazy {
        stores.value.flatMap { store ->
            (1..10).map {
                teaGenerator(it, store.key)
            }
        }.associateBy { it.id }
    }


    //By using GRPC we need to be careful about concurrency, that is why we use had to create a new list instead of affecting a mutable one
    override fun findAllStores(): Flow<Store> {
        return flow {
            stores.value.forEach { entry ->
                val mappedTeas = bubbleTeas.value
                    .filter { it.value.storeId == it.key }
                    .map { it.value }
                emit(entry.value.copy(teas = mappedTeas))
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun findStoreById(storeId: Long): Store? {
        return stores.value[storeId]
    }

    override fun findAllBubbleTea(): Flow<BubbleTea> {
        return flow {
            bubbleTeas.value.forEach {
                emit(it.value)
            }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun findBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea? {
        val store = stores.value[storeId]
        return store?.teas?.firstOrNull { it.id == bubbleTeaId }
    }

    override fun findAllByStoreId(storeId: Long): Flow<BubbleTea> {
        return flow {
            bubbleTeas.value
                .filter { it.value.storeId == storeId }
                .forEach {
                    emit(it.value)
                }
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun findBubbleTeaById(bubbleTeaId: Long): BubbleTea? {
        return bubbleTeas.value[bubbleTeaId]
    }

}
