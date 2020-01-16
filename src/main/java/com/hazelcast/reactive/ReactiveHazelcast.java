package com.hazelcast.reactive;

import com.hazelcast.core.Hazelcast;

public class ReactiveHazelcast {
    public static ReactiveHazelcastInstance newHazelcastInstance() {
        return new ReactiveHazelcastInstance(Hazelcast.newHazelcastInstance());
    }
}
