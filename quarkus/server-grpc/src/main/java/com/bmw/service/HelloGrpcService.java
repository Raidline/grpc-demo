package com.bmw.service;


import com.bmw.model.BubbleTea;
import com.bmw.model.Store;
import com.bmw.repo.StoreRepo;
import com.bmw.servergrpc.store.BubbleTeaReply;
import com.bmw.servergrpc.store.EntityRequest;
import com.bmw.servergrpc.store.MultEntityRequest;
import com.bmw.servergrpc.store.StoreGrpc;
import com.bmw.servergrpc.store.StoreReply;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import io.quarkus.grpc.GrpcService;
import jakarta.enterprise.context.ApplicationScoped;

@GrpcService
@ApplicationScoped
public class HelloGrpcService implements StoreGrpc.AsyncService {

    private final StoreRepo repo;

    public HelloGrpcService(StoreRepo repo) {
        this.repo = repo;
    }

    // They all need to be overridden, or else it would have thrown an exception on the client side
    @Override
    public void findAllBubbleTea(Empty request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findAllBubbleTea()
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe()
                .with(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findAllStores(Empty request, StreamObserver<StoreReply> responseObserver) {
        this.repo.findAllStores()
                .map(this::mapStoreToStoreReply)
                .subscribe()
                .with(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findBubbleTea(EntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findBubbleTeaById(request.getId())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe()
                .with(v -> {
                            responseObserver.onNext(v);
                            responseObserver.onCompleted();
                        }, responseObserver::onError);
    }

    @Override
    public void findStore(EntityRequest request, StreamObserver<StoreReply> responseObserver) {
        this.repo.findStoreById(request.getId())
                .map(this::mapStoreToStoreReply)
                .subscribe()
                .with(v -> {
                            responseObserver.onNext(v);
                            responseObserver.onCompleted();
                        }, responseObserver::onError);
    }

    @Override
    public void findBubbleTeaByStore(MultEntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findBubbleTeaByIdAndStoreId(request.getId(), request.getDependant())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe()
                .with(v -> {
                            responseObserver.onNext(v);
                            responseObserver.onCompleted();
                        }, responseObserver::onError);
    }

    @Override
    public void findAllBubbleTeaInStore(EntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findAllByStoreId(request.getId())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe()
                .with(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    private BubbleTeaReply mapBubbleTeaToBubbleTeaReply(BubbleTea bubbleTea) {
        return BubbleTeaReply.newBuilder()
                .setId(bubbleTea.id())
                .setName(bubbleTea.name())
                .setDescription(bubbleTea.description())
                .setPrice(bubbleTea.price())
                .setStoreId((int) bubbleTea.storeId())
                .build();
    }

    private StoreReply mapStoreToStoreReply(Store store) {
        return StoreReply.newBuilder()
                .setId(store.id())
                .setName(store.name())
                .setAddress(store.address())
                .setPhone(store.phone())
                .build();
    }
}
