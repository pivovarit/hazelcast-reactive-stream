package com.hazelcast.reactive;

import com.hazelcast.core.EntryEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryLoadedListener;
import com.hazelcast.map.listener.EntryMergedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import reactor.core.publisher.FluxSink;

class ReactiveCDCListener<K, V>
  implements EntryAddedListener<K, V>,
  EntryRemovedListener<K, V>,
  EntryMergedListener<K, V>,
  EntryUpdatedListener<K, V>,
  EntryLoadedListener<K, V> {

    private volatile FluxSink<EntryEvent<K, V>> sink;

    public void register(FluxSink<EntryEvent<K, V>> sink) {
        this.sink = sink;
    }

    @Override
    public void entryAdded(EntryEvent<K, V> event) {
        if (sink != null) {
            sink.next(event);
        }
    }

    @Override
    public void entryLoaded(EntryEvent<K, V> event) {
        if (sink != null) {
            sink.next(event);
        }
    }

    @Override
    public void entryMerged(EntryEvent<K, V> event) {
        if (sink != null) {
            sink.next(event);
        }
    }

    @Override
    public void entryRemoved(EntryEvent<K, V> event) {
        if (sink != null) {
            sink.next(event);
        }
    }

    @Override
    public void entryUpdated(EntryEvent<K, V> event) {
        if (sink != null) {
            sink.next(event);
        }
    }
}
