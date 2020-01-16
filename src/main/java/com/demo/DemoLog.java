package com.demo;

import com.hazelcast.core.IQueue;
import com.hazelcast.reactive.ReactiveHazelcast;
import com.hazelcast.reactive.ReactiveHazelcastInstance;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

class DemoLog {
    public static void main(String[] args) {
        ReactiveHazelcastInstance instance = ReactiveHazelcast.newHazelcastInstance();
        IQueue<Integer> transactions = instance.instance().getQueue("transactions");

        startSimulation(transactions);

        instance.getEventStreamForQueue(transactions)
          .log()
          .take(Duration.ofSeconds(120))
          .blockLast();

        instance.instance().shutdown();
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
