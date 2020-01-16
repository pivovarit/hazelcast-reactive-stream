package com.hazelcast.reactive;

import com.hazelcast.cardinality.CardinalityEstimator;
import com.hazelcast.config.Config;
import com.hazelcast.core.ClientService;
import com.hazelcast.core.Cluster;
import com.hazelcast.core.DistributedObject;
import com.hazelcast.core.DistributedObjectListener;
import com.hazelcast.core.Endpoint;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IAtomicLong;
import com.hazelcast.core.IAtomicReference;
import com.hazelcast.core.ICacheManager;
import com.hazelcast.core.ICountDownLatch;
import com.hazelcast.core.IExecutorService;
import com.hazelcast.core.IList;
import com.hazelcast.core.ILock;
import com.hazelcast.core.IMap;
import com.hazelcast.core.IQueue;
import com.hazelcast.core.ISemaphore;
import com.hazelcast.core.ISet;
import com.hazelcast.core.ITopic;
import com.hazelcast.core.IdGenerator;
import com.hazelcast.core.LifecycleService;
import com.hazelcast.core.MultiMap;
import com.hazelcast.core.PartitionService;
import com.hazelcast.core.ReplicatedMap;
import com.hazelcast.cp.CPSubsystem;
import com.hazelcast.crdt.pncounter.PNCounter;
import com.hazelcast.durableexecutor.DurableExecutorService;
import com.hazelcast.flakeidgen.FlakeIdGenerator;
import com.hazelcast.logging.LoggingService;
import com.hazelcast.mapreduce.JobTracker;
import com.hazelcast.quorum.QuorumService;
import com.hazelcast.ringbuffer.Ringbuffer;
import com.hazelcast.scheduledexecutor.IScheduledExecutorService;
import com.hazelcast.transaction.HazelcastXAResource;
import com.hazelcast.transaction.TransactionContext;
import com.hazelcast.transaction.TransactionException;
import com.hazelcast.transaction.TransactionOptions;
import com.hazelcast.transaction.TransactionalTask;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.concurrent.ConcurrentMap;

public class ReactiveHazelcastInstance implements HazelcastInstance {
    private final HazelcastInstance instance;

    ReactiveHazelcastInstance(HazelcastInstance instance) {
        this.instance = instance;
    }

    public <K, V> Flux<EntryEvent<K, V>> getCDCStreamForMap(String name) {
        return getCDCStreamForMap(instance.getMap(name));
    }

    public <K, V> Flux<EntryEvent<K, V>> getCDCStreamForMap(IMap<K, V> map) {
        ReactiveCDCListener<K, V> listener = new ReactiveCDCListener<>();
        map.addEntryListener(listener, true);
        return Flux.create(listener::register);
    }

    @Override
    public String getName() {
        return instance.getName();
    }

    @Override
    public <E> IQueue<E> getQueue(String name) {
        return instance.getQueue(name);
    }

    @Override
    public <E> ITopic<E> getTopic(String name) {
        return instance.getTopic(name);
    }

    @Override
    public <E> ISet<E> getSet(String name) {
        return instance.getSet(name);
    }

    @Override
    public <E> IList<E> getList(String name) {
        return instance.getList(name);
    }

    @Override
    public <K, V> IMap<K, V> getMap(String name) {
        return instance.getMap(name);
    }

    @Override
    public <K, V> ReplicatedMap<K, V> getReplicatedMap(String name) {
        return instance.getReplicatedMap(name);
    }

    @Override
    public JobTracker getJobTracker(String name) {
        return instance.getJobTracker(name);
    }

    @Override
    public <K, V> MultiMap<K, V> getMultiMap(String name) {
        return instance.getMultiMap(name);
    }

    @Override
    @Deprecated
    public ILock getLock(String key) {
        return instance.getLock(key);
    }

    @Override
    public <E> Ringbuffer<E> getRingbuffer(String name) {
        return instance.getRingbuffer(name);
    }

    @Override
    public <E> ITopic<E> getReliableTopic(String name) {
        return instance.getReliableTopic(name);
    }

    @Override
    public Cluster getCluster() {
        return instance.getCluster();
    }

    @Override
    public Endpoint getLocalEndpoint() {
        return instance.getLocalEndpoint();
    }

    @Override
    public IExecutorService getExecutorService(String name) {
        return instance.getExecutorService(name);
    }

    @Override
    public DurableExecutorService getDurableExecutorService(String name) {
        return instance.getDurableExecutorService(name);
    }

    @Override
    public <T> T executeTransaction(TransactionalTask<T> task) throws TransactionException {
        return instance.executeTransaction(task);
    }

    @Override
    public <T> T executeTransaction(TransactionOptions options, TransactionalTask<T> task) throws TransactionException {
        return instance.executeTransaction(options, task);
    }

    @Override
    public TransactionContext newTransactionContext() {
        return instance.newTransactionContext();
    }

    @Override
    public TransactionContext newTransactionContext(TransactionOptions options) {
        return instance.newTransactionContext(options);
    }

    @Override
    @Deprecated
    public IdGenerator getIdGenerator(String name) {
        return instance.getIdGenerator(name);
    }

    @Override
    public FlakeIdGenerator getFlakeIdGenerator(String name) {
        return instance.getFlakeIdGenerator(name);
    }

    @Override
    @Deprecated
    public IAtomicLong getAtomicLong(String name) {
        return instance.getAtomicLong(name);
    }

    @Override
    @Deprecated
    public <E> IAtomicReference<E> getAtomicReference(String name) {
        return instance.getAtomicReference(name);
    }

    @Override
    @Deprecated
    public ICountDownLatch getCountDownLatch(String name) {
        return instance.getCountDownLatch(name);
    }

    @Override
    @Deprecated
    public ISemaphore getSemaphore(String name) {
        return instance.getSemaphore(name);
    }

    @Override
    public Collection<DistributedObject> getDistributedObjects() {
        return instance.getDistributedObjects();
    }

    @Override
    public String addDistributedObjectListener(DistributedObjectListener distributedObjectListener) {
        return instance.addDistributedObjectListener(distributedObjectListener);
    }

    @Override
    public boolean removeDistributedObjectListener(String registrationId) {
        return instance.removeDistributedObjectListener(registrationId);
    }

    @Override
    public Config getConfig() {
        return instance.getConfig();
    }

    @Override
    public PartitionService getPartitionService() {
        return instance.getPartitionService();
    }

    @Override
    public QuorumService getQuorumService() {
        return instance.getQuorumService();
    }

    @Override
    public ClientService getClientService() {
        return instance.getClientService();
    }

    @Override
    public LoggingService getLoggingService() {
        return instance.getLoggingService();
    }

    @Override
    public LifecycleService getLifecycleService() {
        return instance.getLifecycleService();
    }

    @Override
    public <T extends DistributedObject> T getDistributedObject(String serviceName, String name) {
        return instance.getDistributedObject(serviceName, name);
    }

    @Override
    public ConcurrentMap<String, Object> getUserContext() {
        return instance.getUserContext();
    }

    @Override
    public HazelcastXAResource getXAResource() {
        return instance.getXAResource();
    }

    @Override
    public ICacheManager getCacheManager() {
        return instance.getCacheManager();
    }

    @Override
    public CardinalityEstimator getCardinalityEstimator(String name) {
        return instance.getCardinalityEstimator(name);
    }

    @Override
    public PNCounter getPNCounter(String name) {
        return instance.getPNCounter(name);
    }

    @Override
    public IScheduledExecutorService getScheduledExecutorService(String name) {
        return instance.getScheduledExecutorService(name);
    }

    @Override
    public CPSubsystem getCPSubsystem() {
        return instance.getCPSubsystem();
    }

    @Override
    public void shutdown() {
        instance.shutdown();
    }
}
