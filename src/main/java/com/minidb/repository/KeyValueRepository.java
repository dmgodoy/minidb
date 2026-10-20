package com.minidb.repository;

import java.util.Optional;

public interface KeyValueRepository {

    /**
     * Stores a value for the given key.
     *
     * @param key the key to store
     * @param value the value to store
     * @return  true if the key was newly created
     *          false if an existing key was updated
     */
    boolean put(String key, byte[] value);

    /**
     * Retrieves the value associated with a key.
     *
     * @param key the key to retrieve
     * @return the value, or empty if the key does not exist
     */
    Optional<byte[]> get(String key);
}
