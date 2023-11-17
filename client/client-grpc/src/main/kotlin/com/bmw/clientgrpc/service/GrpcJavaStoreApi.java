package com.bmw.clientgrpc.service;

import com.bmw.clientgrpc.BubbleTea;
import com.bmw.clientgrpc.Store;
import com.bmw.clientgrpc.service.observer.ReactiveStreamObserver;
import com.bmw.servergrpc.store.BubbleTeaReply;
import com.bmw.servergrpc.store.StoreGrpc;
import com.bmw.servergrpc.store.StoreReply;
import com.google.protobuf.Empty;
import io.grpc.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class GrpcJavaStoreApi {

    private final StoreGrpc.StoreStub api;

    @Autowired
    public GrpcJavaStoreApi(StoreGrpc.StoreStub api) {
        this.api = api;
    }

    public Flux<Store> findAllStores() {
        var observer = new ReactiveStreamObserver<Store, StoreReply>() {
            @Override
            public Store process(StoreReply value) {
                return mapStoreReplyToStore(value);
            }
        };

        return observer.observe(Empty.getDefaultInstance(), api::findAllStores);
    }

    public Mono<Store> findStoreById(long id) {

    }

    public Flux<BubbleTea> findAllBubbleTea() {

    }

    public Flux<BubbleTea> findAllBubbleTeaInStore(long storeId) {

    }

    public Mono<BubbleTea> findBubbleTeaByIdAndStoreId(long id, long storeId) {

    }

    public Flux<BubbleTea> findAllByStoreId(long storeId) {

    }

    public Mono<BubbleTea> findBubbleTeaById(long id) {

    }

    private Store mapStoreReplyToStore(StoreReply reply) {
        return new Store(
                reply.getId(),
                reply.getName(),
                reply.getAddress(),
                reply.getPhone(),
                reply.getBubbleTeasList().stream().map(this::mapBubbleTeaReplyToBubbleTea).toList()
        );
    }

    public BubbleTea mapBubbleTeaReplyToBubbleTea(BubbleTeaReply reply) {
        return new BubbleTea(
                reply.getId(),
                reply.getName(),
                reply.getDescription(),
                reply.getPrice(),
                reply.getStoreId()
        );
    }
}
