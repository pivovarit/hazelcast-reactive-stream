package com.hazelcast.reactive;

import com.hazelcast.core.Hazelcast;

/**
 * TODO one listener - many subscribers
 * TODO integration via EventJournal
 */
public class ReactiveHazelcast {
    public static ReactiveHazelcastInstance newHazelcastInstance() {
        return new ReactiveHazelcastInstance(Hazelcast.newHazelcastInstance());
    }
}
