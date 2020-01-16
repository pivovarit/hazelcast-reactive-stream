//package com.demo;
//
//import com.hazelcast.core.EntryEvent;
//import com.hazelcast.core.IMap;
//import com.hazelcast.reactive.ReactiveHazelcast;
//import com.hazelcast.reactive.ReactiveHazelcastInstance;
//import reactor.core.publisher.Flux;
//
//import java.time.Duration;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ThreadLocalRandom;
//
//class DemoMap {
//    public static void main(String[] args) throws Exception {
//        try (ReactiveHazelcastInstance instance = ReactiveHazelcast.newHazelcastInstance()) {
//            IMap<Long, String> map = instance.getMap("map");
//
//            startSimulation(map);
//
//            Flux<EntryEvent<Long, String>> eventStreamForMap = instance.getEventStreamForMap(map);
//
//            eventStreamForMap
//              .log()
//              .take(Duration.ofMinutes(1))
//              .blockLast();
//        }
//    }
//
//    private static void startSimulation(IMap<Long, String> map) {
//        Executors.newSingleThreadExecutor(r -> {
//            Thread thread = new Thread(r);
//            thread.setDaemon(true);
//            return thread;
//        }).submit(() -> {
//            while (true) {
//                Thread.sleep(1000 + ThreadLocalRandom.current().nextInt(300));
//                map.putIfAbsent()
//                transactions.put(ThreadLocalRandom.current().nextInt(100));
//            }
//        });
//
//        Executors.newSingleThreadExecutor(r -> {
//            Thread thread = new Thread(r);
//            thread.setDaemon(true);
//            return thread;
//        }).submit(() -> {
//            while (true) {
//                Thread.sleep(2000 + ThreadLocalRandom.current().nextInt(500));
//                transactions.take();
//            }
//        });
//    }
//}
