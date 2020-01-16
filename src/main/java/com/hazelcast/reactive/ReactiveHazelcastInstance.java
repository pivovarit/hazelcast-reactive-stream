package com.hazelcast.reactive;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import reactor.core.publisher.Flux;

public class ReactiveHazelcastInstance {
    private final HazelcastInstance instance;

    ReactiveHazelcastInstance(HazelcastInstance instance) {
        this.instance = instance;
    }

    public HazelcastInstance instance() {
        return instance;
    }

    public <K, V> Flux<EntryEvent<K, V>> getEventStreamForMap(String name) {
        return getEventStreamForMap(instance.getMap(name));
    }

    public <K, V> Flux<EntryEvent<K, V>> getEventStreamForMap(IMap<K, V> map) {
        return getEventStreamForMap(map, true);
    }

    public <K, V> Flux<EntryEvent<K, V>> getEventStreamForMap(IMap<K, V> map, boolean includeValue) {
        IMapFluxSink<K, V> listener = new IMapFluxSink<>();
        map.addEntryListener(listener, includeValue);
        return Flux.create(sink -> listener.register(sink));
    }

    public <E> Flux<ItemEvent<E>> getEventStreamForQueue(String name) {
        return getEventStreamForQueue(instance.getQueue(name));
    }

    public <E> Flux<ItemEvent<E>> getEventStreamForQueue(IQueue<E> queue) {
        return getEventStreamForQueue(queue, true);
    }

    public <E> Flux<ItemEvent<E>> getEventStreamForQueue(IQueue<E> queue, boolean includeValue) {
        IQueueFluxSink<E> listener = new IQueueFluxSink<>();
        queue.addItemListener(listener, includeValue);
        return Flux.create(listener::register);
    }
}
