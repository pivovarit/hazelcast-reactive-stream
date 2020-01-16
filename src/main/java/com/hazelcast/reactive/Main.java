package com.hazelcast.reactive;

import com.devskiller.jfairy.Fairy;
import com.devskiller.jfairy.producer.person.Person;
import com.devskiller.jfairy.producer.person.PersonProperties;
import com.hazelcast.core.IMap;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

class Main {

    public static final ExecutorService E = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        Fairy fairy = Fairy.create();

        ReactiveHazelcastInstance reactiveInstance = ReactiveHazelcast.newHazelcastInstance();
        IMap<String, Person> map = reactiveInstance.instance().getMap("map");

        E.submit(() ->{
            while (true) {
                Thread.sleep(1000);
                Person person = fairy.person();
                map.putIfAbsent(person.getNationalIdentificationNumber(), person);
            }
        });

        System.out.println("subscribing...");
        reactiveInstance.getCDCStreamForMap(map)
          .log()
          .blockLast();
    }
}
