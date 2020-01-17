Capture changes happening in the IMDG data structures and consume as Project Reactor's `Flux`:

```
    IMap<Long, String> map = instance.getMap("map");
    Flux<EntryEvent<Long, String>> eventStreamForMap = instance.getEventStreamForMap(map);

    eventStreamForMap
        .log()
        .doOnNext(logAssignments())
        .take(Duration.ofMinutes(1))
        .blockLast();
```
