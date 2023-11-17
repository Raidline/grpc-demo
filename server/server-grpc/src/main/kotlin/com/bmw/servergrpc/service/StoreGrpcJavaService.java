package com.bmw.servergrpc.service;

import com.bmw.servergrpc.data.BubbleTea;
import com.bmw.servergrpc.data.Store;
import com.bmw.servergrpc.data.StoreJavaRepository;
import com.bmw.servergrpc.store.BubbleTeaReply;
import com.bmw.servergrpc.store.EntityRequest;
import com.bmw.servergrpc.store.MultEntityRequest;
import com.bmw.servergrpc.store.StoreGrpc;
import com.bmw.servergrpc.store.StoreReply;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


//TODO:HERE
@Service
public class StoreGrpcJavaService extends StoreGrpc.StoreImplBase {

    private final StoreJavaRepository repo;

    @Autowired
    public StoreGrpcJavaService(StoreJavaRepository repo) {
        this.repo = repo;
    }


    // They all need to be overridden, or else it would have thrown an exception on the client side
    @Override
    public void findAllBubbleTea(Empty request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findAllBubbleTea()
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findAllStores(Empty request, StreamObserver<StoreReply> responseObserver) {
        this.repo.findAllStores()
                .map(this::mapStoreToStoreReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findBubbleTea(EntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findBubbleTeaById(request.getId())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findStore(EntityRequest request, StreamObserver<StoreReply> responseObserver) {
        this.repo.findStoreById(request.getId())
                .map(this::mapStoreToStoreReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findBubbleTeaByStore(MultEntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findBubbleTeaByIdAndStoreId(request.getId(), request.getDependant())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }

    @Override
    public void findAllBubbleTeaInStore(EntityRequest request, StreamObserver<BubbleTeaReply> responseObserver) {
        this.repo.findAllByStoreId(request.getId())
                .map(this::mapBubbleTeaToBubbleTeaReply)
                .subscribe(responseObserver::onNext,
                        responseObserver::onError,
                        responseObserver::onCompleted);
    }


    private StoreReply mapStoreToStoreReply(Store store) {
        return StoreReply.newBuilder()
                .setId(store.getId())
                .setName(store.getName())
                .setAddress(store.getAddress())
                .setPhone(store.getPhone())
                .build();
    }

    private BubbleTeaReply mapBubbleTeaToBubbleTeaReply(BubbleTea bubbleTea) {
        return BubbleTeaReply.newBuilder()
                .setId(bubbleTea.getId())
                .setName(bubbleTea.getName())
                .setDescription(bubbleTea.getDescription())
                .setPrice(bubbleTea.getPrice())
                .setStoreId((int) bubbleTea.getStoreId())
                .build();
    }
}
