package com.demo;

import com.devskiller.jfairy.Fairy;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.EntryEventType;
import com.hazelcast.core.IMap;
import com.hazelcast.reactive.ReactiveHazelcast;
import com.hazelcast.reactive.ReactiveHazelcastInstance;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

class DemoMap {
    public static void main(String[] args) throws Exception {
        try (ReactiveHazelcastInstance instance = ReactiveHazelcast.newHazelcastInstance()) {
            IMap<Long, String> map = instance.getMap("map");

            startSimulation(map);

            Flux<EntryEvent<Long, String>> eventStreamForMap = instance.getEventStreamForMap(map);

            eventStreamForMap
              .log()
              .doOnNext(logAssignments())
              .take(Duration.ofMinutes(1))
              .blockLast();
        }
    }

    private static Consumer<EntryEvent<Long, String>> logAssignments() {
        return entry -> {
            if (entry.getEventType() == EntryEventType.ADDED) {
                System.out.println("Assigned id: " + entry.getKey() + " to: " + entry.getValue());
            } else if (entry.getEventType() == EntryEventType.REMOVED) {
                System.out.println("Unassigned id: " + entry.getKey());
            }
        };
    }

    private static void startSimulation(IMap<Long, String> map) {
        Fairy fairy = Fairy.create();
        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(500 + ThreadLocalRandom.current().nextInt(300));
                map.putIfAbsent(ThreadLocalRandom.current().nextLong(20), fairy.person().getFullName());
            }
        });

        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(500));
                map.remove(ThreadLocalRandom.current().nextLong(20));
            }
        });
    }
}
