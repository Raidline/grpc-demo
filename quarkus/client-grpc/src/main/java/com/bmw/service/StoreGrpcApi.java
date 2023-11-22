package com.bmw.service;

import com.bmw.model.BubbleTea;
import com.bmw.model.Store;
import com.bmw.servergrpc.store.BubbleTeaReply;
import com.bmw.servergrpc.store.MutinyStoreGrpc;
import com.bmw.servergrpc.store.StoreReply;
import com.google.protobuf.Empty;
import io.quarkus.grpc.GrpcClient;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class StoreGrpcApi {

    @GrpcClient
    MutinyStoreGrpc.MutinyStoreStub mutinyStoreStub;

    public Multi<Store> findAllStores() {
        return this.mutinyStoreStub.findAllStores(Empty.newBuilder().build())
                .map(this::mapStoreReplyToStore);
    }

    public Uni<Store> findStoreById(long id) {
        return Uni.createFrom().nullItem();
    }

    public Multi<BubbleTea> findAllBubbleTea() {
        return Multi.createFrom().empty();
    }

    public Multi<BubbleTea> findAllBubbleTeaInStore(long storeId) {
        return Multi.createFrom().empty();
    }

    public Uni<BubbleTea> findBubbleTeaByIdAndStoreId(long id, long storeId) {
        return Uni.createFrom().nullItem();
    }

    public Multi<BubbleTea> findAllByStoreId(long storeId) {
        return Multi.createFrom().empty();
    }

    public Uni<BubbleTea> findBubbleTeaById(long id) {
        return Uni.createFrom().nullItem();
    }

    public Multi<BubbleTea> findAllStoresWithBubbleTea(int bubbleTeaId) {
        return Multi.createFrom().empty();
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
