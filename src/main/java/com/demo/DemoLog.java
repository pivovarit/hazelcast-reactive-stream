package com.demo;

import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEvent;
import com.hazelcast.reactive.ReactiveHazelcast;
import com.hazelcast.reactive.ReactiveHazelcastInstance;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class DemoLog {
    public static void main(String[] args) throws Exception {
        try (ReactiveHazelcastInstance instance = ReactiveHazelcast.newHazelcastInstance()) {
            IQueue<Integer> transactions = instance.getQueue("transactions");

            startSimulation(transactions);

            Flux<ItemEvent<Integer>> eventStream = instance.getEventStreamForQueue(transactions);

            eventStream
              .log()
              .take(Duration.ofSeconds(120))
              .blockLast();
        }
    }

    private static void startSimulation(IQueue<Integer> transactions) {
        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(300));
                transactions.put(ThreadLocalRandom.current().nextInt(100));
            }
        });

        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(2000 + ThreadLocalRandom.current().nextInt(500));
                transactions.take();
            }
        });
    }
}
