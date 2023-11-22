package com.bmw.repo;

import com.bmw.model.BubbleTea;
import com.bmw.model.Store;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;


public interface Repo {

    Multi<Store> findAllStores();
    Uni<Store> findStoreById(long storeId);
    Multi<BubbleTea> findAllBubbleTea();
    Uni<BubbleTea> findBubbleTeaById(long bubbleTeaId);
    Uni<BubbleTea> findBubbleTeaByIdAndStoreId(long storeId, long bubbleTeaId);
    Multi<BubbleTea> findAllByStoreId(long storeId);
}
