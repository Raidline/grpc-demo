package com.bmw.servergrpc.service.presentation

import com.bmw.servergrpc.data.presentation.StoreRepo
import com.bmw.servergrpc.exception.NotFoundException
import com.bmw.servergrpc.store.BubbleTeaReply
import com.bmw.servergrpc.store.EntityRequest
import com.bmw.servergrpc.store.MultEntityRequest
import com.bmw.servergrpc.store.StoreGrpcKt
import com.bmw.servergrpc.store.StoreNoBubbleReply
import com.bmw.servergrpc.store.StoreReply
import com.google.protobuf.Empty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import org.springframework.stereotype.Service

@Service
class StoreGrpcService(private val repo: StoreRepo) :
    StoreGrpcKt.StoreCoroutineImplBase(coroutineContext = Dispatchers.IO) {

    override fun findAllBubbleTea(request: Empty): Flow<BubbleTeaReply> {
        return repo.findAllBubbleTea()
            .map {
                BubbleTeaReply.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setDescription(it.description)
                    .setPrice(it.price)
                    .setStoreId(it.storeId.toInt())
                    .build()
            }
    }

    override fun findAllStores(request: Empty): Flow<StoreReply> {
        return repo.findAllStores()
            .map { store ->
                val builder = StoreReply.newBuilder()
                .setId(store.id)
                .setName(store.name)
                .setAddress(store.address)
                .setPhone(store.phone)


                val teas = store.teas.map {
                    BubbleTeaReply.newBuilder()
                        .setId(it.id)
                        .setName(it.name)
                        .setDescription(it.description)
                        .setPrice(it.price)
                        .setStoreId(it.storeId.toInt())
                        .build()
                }

                teas.forEach { tea ->
                    builder.addBubbleTeas(tea)
                }

                builder.build()
            }
    }

    override suspend fun findBubbleTea(request: EntityRequest): BubbleTeaReply {
        val tea =
            repo.findBubbleTeaById(request.id) ?: throw NotFoundException("Bubble Tea not found for id ${request.id}")

        return BubbleTeaReply.newBuilder()
            .setId(tea.id)
            .setName(tea.name)
            .setDescription(tea.description)
            .setPrice(tea.price)
            .setStoreId(tea.storeId.toInt())
            .build()
    }

    override suspend fun findStore(request: EntityRequest): StoreReply {
        val store = repo.findStoreById(request.id) ?: throw NotFoundException("Store not found for id ${request.id}")

        val builder = StoreReply.newBuilder()
            .setId(store.id)
            .setName(store.name)
            .setAddress(store.address)
            .setPhone(store.phone)

        val bubbleTeas = findAllBubbleTeaInStore(EntityRequest.newBuilder().setId(store.id).build())

        bubbleTeas.collect {
            builder.addBubbleTeas(it)
        }

        return builder.build()
    }

    override suspend fun findBubbleTeaByStore(request: MultEntityRequest): BubbleTeaReply {
        val tea = repo.findBubbleTeaByIdAndStoreId(request.id, request.dependant)
            ?: throw NotFoundException("Bubble Tea not found for store id ${request.id}")

        return BubbleTeaReply.newBuilder()
            .setId(tea.id)
            .setName(tea.name)
            .setDescription(tea.description)
            .setPrice(tea.price)
            .setStoreId(tea.storeId.toInt())
            .build()
    }

    override fun findAllBubbleTeaInStore(request: EntityRequest): Flow<BubbleTeaReply> {
        return repo.findAllByStoreId(request.id)
            .map {
                BubbleTeaReply.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setDescription(it.description)
                    .setPrice(it.price)
                    .setStoreId(it.storeId.toInt())
                    .build()
            }
    }

    override fun findAllStoresWithBubbleTea(request: EntityRequest): Flow<StoreNoBubbleReply> {
        return repo.findAllStores()
            .filter { store -> store.teas.any { it.id == request.id } }
            .map { findStore(EntityRequest.newBuilder().setId(it.id).build()) }
            .map {
                StoreNoBubbleReply.newBuilder()
                    .setId(it.id)
                    .setName(it.name)
                    .setAddress(it.address)
                    .build()
            }
    }
}
