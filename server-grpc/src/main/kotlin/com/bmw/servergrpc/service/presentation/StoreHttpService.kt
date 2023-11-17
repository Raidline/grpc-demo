package com.bmw.servergrpc.service.presentation

import com.bmw.servergrpc.data.BubbleTea
import com.bmw.servergrpc.data.Store
import com.bmw.servergrpc.data.presentation.StoreRepo
import com.bmw.servergrpc.exception.NotFoundException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.springframework.stereotype.Service

@Service
class StoreHttpService(private val storeRepo: StoreRepo) {

    fun findAllBubbleTea() : Flow<BubbleTea> {
        return storeRepo.findAllBubbleTea()
    }

    suspend fun findBubbleTea(bubbleTeaId: Long) : BubbleTea {
        return storeRepo.findBubbleTeaById(bubbleTeaId) ?: throw NotFoundException("Bubble Tea not found for id $bubbleTeaId")
    }

    suspend fun findBubbleTeaByStore(bubbleTeaId : Long, storeId: Long) : BubbleTea {
        return storeRepo.findBubbleTeaByIdAndStoreId(bubbleTeaId, storeId)
            ?: throw NotFoundException("Bubble Tea not found for store $storeId")
    }

    fun findAllBubbleTeaByStore(storeId: Long) : Flow<BubbleTea> {
        return storeRepo.findAllByStoreId(storeId)
    }

    //------------- store methods -------------//

    suspend fun findStore(storeId: Long) : Store {
        return storeRepo.findStoreById(storeId) ?: throw NotFoundException("Store not found for id $storeId")
    }

    fun findAllStores() : Flow<Store> {
        return storeRepo.findAllStores()
    }

    fun findAllStoresWithBubbleTea(bubbleTeaId : Long) : Flow<Store> {
        return findAllStores()
            .filter { store -> store.teas.firstOrNull { it.id == bubbleTeaId } != null }
    }
}
