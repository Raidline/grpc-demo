package com.bmw.servergrpc.data;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface StoreJavaRepository {

    Flux<Store> findAllStores();
    Mono<Store> findStoreById(long storeId);
    Flux<BubbleTea> findAllBubbleTea();
    Mono<BubbleTea> findBubbleTeaById(long bubbleTeaId);
    Mono<BubbleTea> findBubbleTeaByIdAndStoreId(long storeId, long bubbleTeaId);
    Flux<BubbleTea> findAllByStoreId(long storeId);
}
