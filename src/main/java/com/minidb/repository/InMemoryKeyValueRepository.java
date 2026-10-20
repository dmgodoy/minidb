package com.minidb.repository;

import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class InMemoryKeyValueRepository implements KeyValueRepository {

    private final ConcurrentMap<String, byte[]> data = new ConcurrentHashMap<>();

    @Override
    public boolean put(String key, byte[] value) {

        Objects.requireNonNull(key, "key must not be null");
        Objects.requireNonNull(value, "value must not be null");

        byte[] defensiveCopy = Arrays.copyOf(value, value.length);
        return data.put(key, defensiveCopy) == null;
    }

    @Override
    public Optional<byte[]> get(String key) {

        Objects.requireNonNull(key, "key must not be null");

        return Optional.ofNullable(data.get(key)).map(byte[]::clone);
    }
}
