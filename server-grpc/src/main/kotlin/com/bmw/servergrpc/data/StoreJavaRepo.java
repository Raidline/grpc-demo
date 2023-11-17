package com.bmw.servergrpc.data;

import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.LongFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class StoreJavaRepo implements StoreJavaRepository {

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

    private final Map<Long, Store> stores = Stream.iterate(1L, i -> i + 1)
            .limit(100)
            .map(storeGenerator::apply)
            .collect(Collectors.toMap(Store::getId, Function.identity()));

    //Http cannot handle more than 500 because of max bytes (need to configure that and i'm lazy)
    private final Map<Long, BubbleTea> bubbleTeas = stores.keySet().stream()
            .flatMap(store -> Stream.iterate(1L, i -> i + 1)
                    .limit(10)
                    .map(i -> bubbleTeaGenerator.apply(store, i)))
            .collect(Collectors.toMap(BubbleTea::getId, Function.identity()));

    @Override
    public Flux<Store> findAllStores() {
        return Flux.fromIterable(stores.values())
                .flatMap(store -> this.findAllByStoreId(store.getId())
                        .collectList()
                        .map(teas -> new Store(
                                store.getId(),
                                store.getName(),
                                store.getAddress(),
                                store.getPhone(),
                                teas
                        )))
                .publishOn(Schedulers.parallel());
    }

    @Override
    public Mono<Store> findStoreById(long storeId) {
        return Mono.justOrEmpty(stores.get(storeId));
    }

    @Override
    public Flux<BubbleTea> findAllBubbleTea() {
        return Flux.fromIterable(bubbleTeas.values())
                .publishOn(Schedulers.parallel());
    }

    @Override
    public Mono<BubbleTea> findBubbleTeaById(long bubbleTeaId) {
        return Mono.justOrEmpty(bubbleTeas.get(bubbleTeaId));
    }

    @Override
    public Mono<BubbleTea> findBubbleTeaByIdAndStoreId(long storeId, long bubbleTeaId) {
        var store = this.findStoreById(storeId);
        return store.map(s -> s.getTeas().stream()
                .filter(b -> b.getId() == bubbleTeaId)
                .findFirst()
                .orElseThrow());
    }

    @Override
    public Flux<BubbleTea> findAllByStoreId(long storeId) {
        return Flux.fromIterable(bubbleTeas.entrySet())
                .filter(entry -> entry.getValue().getStoreId() == storeId)
                .map(Map.Entry::getValue)
                .publishOn(Schedulers.parallel());
    }
}
