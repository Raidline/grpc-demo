package com.bmw.clientgrpc.service.observer;

import io.grpc.Context;
import io.grpc.stub.StreamObserver;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.function.BiConsumer;

public abstract class ReactiveStreamObserver<T,S> implements StreamObserver<S> {

    private final Sinks.Many<T> sink;

    protected ReactiveStreamObserver() {
        this.sink = Sinks.many().unicast().onBackpressureBuffer();
    }

    @Override
    public void onNext(S value) {
        sink.tryEmitNext(process(value));
    }

    @Override
    public void onError(Throwable t) {
        sink.tryEmitError(t);
    }

    @Override
    public void onCompleted() {
        sink.tryEmitComplete();
    }

    public <R> Flux<T> observe(R request, BiConsumer<R, StreamObserver<S>> consumer) {
        var context = Context.current().fork().withCancellation();
        context.run(() -> consumer.accept(request, this));
        return sink.asFlux().doFinally(signalType -> context.cancel(new RuntimeException("Context closed by " + signalType.name())));
    }

    public abstract T process(S value);
}
