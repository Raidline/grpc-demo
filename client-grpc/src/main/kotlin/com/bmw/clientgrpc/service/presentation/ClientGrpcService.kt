package com.bmw.clientgrpc.service.presentation

import com.bmw.clientgrpc.BubbleTea
import com.bmw.clientgrpc.Store
import com.bmw.clientgrpc.data.presentation.GrpcStoreApi
import com.bmw.clientgrpc.service.GrpcClientService
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class ClientGrpcService(private val grpcClient: GrpcStoreApi) : GrpcClientService {

    override fun findGrpcAllStores(): Flow<Store> {
        return grpcClient.findAllStores()
    }

    override suspend fun findGrpcStoreById(storeId: Long): Store {
        TODO("Not yet implemented")
    }

    override fun findGrpcAllBubbleTea(): Flow<BubbleTea> {
        TODO("Not yet implemented")
    }

    override suspend fun findGrpcBubbleTeaByIdAndStoreId(bubbleTeaId: Long, storeId: Long): BubbleTea {
        TODO("Not yet implemented")
    }

    override fun findGrpcAllBubbleTeaInStore(storeId: Long): Flow<BubbleTea> {
        TODO("Not yet implemented")
    }

    override fun findGrpcAllByStoreId(storeId: Long): Flow<BubbleTea> {
        TODO("Not yet implemented")
    }

    override suspend fun findGrpcBubbleTeaById(bubbleTeaId: Long): BubbleTea {
        TODO("Not yet implemented")
    }

    override fun findGrpcAllStoresWithBubbleTea(storeId: Long): Flow<Store> {
        TODO("Not yet implemented")
    }
}
