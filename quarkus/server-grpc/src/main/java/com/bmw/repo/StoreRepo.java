package com.bmw.repo;

import com.bmw.model.BubbleTea;
import com.bmw.model.Store;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@ApplicationScoped
public class StoreRepo implements Repo {

    private final LongFunction<Store> storeGenerator = id -> new Store(
            id,
            "Store " + id,
            "Address " + id,
            "Phone " + id,
            new ArrayList<>()
    );

    private final BiFunction<Long, Long, BubbleTea> bubbleTeaGenerator = (storeId, bubbleTeaId) -> new BubbleTea(
            bubbleTeaId,
            "Bubble Tea " + bubbleTeaId,
            "Description " + bubbleTeaId,
            6.0,
            storeId
    );

    private final Map<Long, Store> stores = Stream.iterate(0L, i -> i + 1)
            .limit(100)
            .map(storeGenerator::apply)
            .collect(Collectors.toMap(Store::id, Function.identity()));

    //Http cannot handle more than 500 because of max bytes (need to configure that and i'm lazy)
    private final Map<Long, BubbleTea> bubbleTeas = stores.keySet().stream()
            .flatMap(store -> Stream.iterate(1L, i -> i + 1)
                    .limit(10)
                    .map(i -> bubbleTeaGenerator.apply(store, i)))
            .collect(Collectors.toMap(BubbleTea::id, Function.identity(),
                    (bubbleTea, bubbleTea2) -> bubbleTea));

    @Override
    public Multi<Store> findAllStores() {
        return Multi.createFrom().emitter(emitter -> {
            for (Store value : stores.values()) {
                this.findAllByStoreId(value.id())
                        .collect()
                        .asList()
                        .subscribe()
                        .with(teas -> emitter.emit(new Store(
                                value.id(),
                                value.name(),
                                value.address(),
                                value.phone(),
                                teas
                        )));
            }
        });
    }

    @Override
    public Uni<Store> findStoreById(long storeId) {
        return Uni.createFrom().optional(() -> Optional.ofNullable(stores.get(storeId)));
    }

    @Override
    public Multi<BubbleTea> findAllBubbleTea() {
        return Multi.createFrom().iterable(bubbleTeas.values())
                .emitOn(Executors.newSingleThreadExecutor());
    }

    @Override
    public Uni<BubbleTea> findBubbleTeaById(long bubbleTeaId) {
        return Uni.createFrom().optional(() -> Optional.ofNullable(bubbleTeas.get(bubbleTeaId)));
    }

    @Override
    public Uni<BubbleTea> findBubbleTeaByIdAndStoreId(long storeId, long bubbleTeaId) {
        var store = this.findStoreById(storeId);
        return store.map(s -> s.bubbleTeas().stream()
                .filter(b -> b.id() == bubbleTeaId)
                .findFirst()
                .orElseThrow());
    }

    @Override
    public Multi<BubbleTea> findAllByStoreId(long storeId) {
        return Multi.createFrom().iterable(bubbleTeas.entrySet())
                .filter(entry -> entry.getValue().storeId() == storeId)
                .map(Map.Entry::getValue)
                .emitOn(Executors.newSingleThreadExecutor());
    }
}
