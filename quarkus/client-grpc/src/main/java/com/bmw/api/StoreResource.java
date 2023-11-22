package com.bmw.api;

import com.bmw.model.BubbleTea;
import com.bmw.model.Store;
import com.bmw.service.StoreGrpcApi;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/grpc")
@ApplicationScoped
public class StoreResource {

    private final StoreGrpcApi storeGrpcApi;

    public StoreResource(StoreGrpcApi storeGrpcApi) {
        this.storeGrpcApi = storeGrpcApi;
    }

    @GET
    @Path("/bubble-tea")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<BubbleTea> getAllBubbleTeas() {
        return this.storeGrpcApi.findAllBubbleTea();
    }

    @GET
    @Path("/bubble-tea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<BubbleTea> getBubbleTeaById(int id) {
        return this.storeGrpcApi.findBubbleTeaById(id);
    }

    @GET
    @Path("/bubble-tea/{id}/store/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<BubbleTea> getBubbleTeaInStore(int storeId, int bubbleTeaId) {
        return this.storeGrpcApi.findBubbleTeaByIdAndStoreId(storeId, bubbleTeaId);
    }

    @GET
    @Path("/bubble-tea/store/{storeId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<BubbleTea> getAllBubbleTeaInStore(int storeId) {
        return this.storeGrpcApi.findAllBubbleTeaInStore(storeId);
    }

    @GET
    @Path("/store")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<Store> getAllStores() {
        return this.storeGrpcApi.findAllStores();
    }

    @GET
    @Path("/store/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Store> getStore(int id) {
        return this.storeGrpcApi.findStoreById(id);
    }

    @GET
    @Path("/store/bubble-tea/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<BubbleTea> getAllStores(int bubbleTeaId) {
        return this.storeGrpcApi.findAllStoresWithBubbleTea(bubbleTeaId);
    }
}
