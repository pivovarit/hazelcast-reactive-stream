package com.demo;

import com.hazelcast.core.IQueue;
import com.hazelcast.core.ItemEventType;
import com.hazelcast.reactive.ReactiveHazelcast;
import com.hazelcast.reactive.ReactiveHazelcastInstance;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class DemoAggregation {
    public static void main(String[] args) throws Exception {
        try (ReactiveHazelcastInstance instance = ReactiveHazelcast.newHazelcastInstance()) {

            IQueue<Integer> transactions = instance.getQueue("transactions");

            startSimulation(transactions);

            instance.getEventStreamForQueue(transactions)
              .log()
              .filter(e -> e.getEventType() == ItemEventType.ADDED)
              .window(Duration.ofSeconds(1))
              .flatMap(agg -> agg.reduce(0, (sum, e) -> sum + e.getItem()))
              .filter(sum -> sum > 0)
              .doOnNext(s -> System.out.println("Sum of transactions in last second: " + s))
              .take(Duration.ofSeconds(30))
              .reduce(0, Integer::sum)
              .doOnNext(s -> System.out.println("Sum of transactions: " + s))
              .block();

        }
    }

    private static void startSimulation(IQueue<Integer> transactions) {
        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(100 + ThreadLocalRandom.current().nextInt(300));
                transactions.put(ThreadLocalRandom.current().nextInt(100));
            }
        });

        Executors.newSingleThreadExecutor(r -> {
            Thread thread = new Thread(r);
            thread.setDaemon(true);
            return thread;
        }).submit(() -> {
            while (true) {
                Thread.sleep(200 + ThreadLocalRandom.current().nextInt(500));
                transactions.take();
            }
        });
    }
}
