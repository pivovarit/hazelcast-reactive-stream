package com.hazelcast.reactive;

import com.hazelcast.instance.HazelcastInstanceFactory;

public class ReactiveHazelcast {
    public static ReactiveHazelcastInstance newHazelcastInstance() {
        return new ReactiveHazelcastInstance(HazelcastInstanceFactory.newHazelcastInstance(null));
    }
}
