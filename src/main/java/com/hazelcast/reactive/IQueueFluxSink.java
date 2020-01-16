package com.hazelcast.reactive;

import com.hazelcast.core.ItemEvent;
import com.hazelcast.core.ItemListener;
import reactor.core.publisher.FluxSink;

class IQueueFluxSink<E> implements ItemListener<E> {

    private volatile FluxSink<ItemEvent<E>> sink;

    public void register(FluxSink<ItemEvent<E>> sink) {
        this.sink = sink;
    }

    @Override
    public void itemAdded(ItemEvent<E> item) {
        if (sink != null) {
            sink.next(item);
        }
    }

    @Override
    public void itemRemoved(ItemEvent<E> item) {
        if (sink != null) {
            sink.next(item);
        }
    }
}
