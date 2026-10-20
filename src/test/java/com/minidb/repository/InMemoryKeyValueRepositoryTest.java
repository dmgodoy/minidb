package com.minidb.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryKeyValueRepositoryTest {

    private KeyValueRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryKeyValueRepository();
    }

    @Test
    void givenNullKey_whenPuttingItem_thenThrowsNullPointerException() {
        byte[] value = "Alice".getBytes(StandardCharsets.UTF_8);

        assertThrows(NullPointerException.class, () -> repository.put(null, value));
    }

    @Test
    void givenNullValue_whenPuttingItem_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.put("name", null));
    }

    @Test
    void givenNullKey_whenGettingItem_thenThrowsNullPointerException() {
        assertThrows(NullPointerException.class, () -> repository.get(null));
    }

    @Test
    void givenNewKey_whenPuttingItem_thenReturnsTrue() {
        byte[] value = "Alice".getBytes(StandardCharsets.UTF_8);

        assertTrue(repository.put("name", value));
    }

    @Test
    void givenExistingKey_whenPuttingItem_thenReturnsFalse() {
        repository.put("name", "Alice".getBytes(StandardCharsets.UTF_8));

        assertFalse(repository.put("name", "Bob".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void givenExistingKey_whenPuttingItem_thenOverwritesValue() {
        repository.put("name", "Alice".getBytes(StandardCharsets.UTF_8));
        repository.put("name", "Bob".getBytes(StandardCharsets.UTF_8));

        Optional<byte[]> result = repository.get("name");

        assertTrue(result.isPresent());
        assertArrayEquals("Bob".getBytes(StandardCharsets.UTF_8), result.get());
    }

    @Test
    void givenStoredItem_whenGettingItem_thenReturnsValue() {
        byte[] value = "Alice".getBytes(StandardCharsets.UTF_8);
        repository.put("name", value);

        Optional<byte[]> result = repository.get("name");

        assertTrue(result.isPresent());
        assertArrayEquals(value, result.get());
    }

    @Test
    void givenMissingKey_whenGettingItem_thenReturnsEmpty() {
        assertTrue(repository.get("missing").isEmpty());
    }

    @Test
    void givenMultipleKeys_whenGettingItems_thenReturnsCorrespondingValues() {
        repository.put("first", "Alice".getBytes(StandardCharsets.UTF_8));
        repository.put("second", "Bob".getBytes(StandardCharsets.UTF_8));

        assertArrayEquals("Alice".getBytes(StandardCharsets.UTF_8), repository.get("first").orElseThrow());
        assertArrayEquals("Bob".getBytes(StandardCharsets.UTF_8), repository.get("second").orElseThrow());
    }

    @Test
    void givenEmptyValue_whenPuttingItem_thenStoresEmptyArray() {
        repository.put("empty", new byte[0]);

        Optional<byte[]> result = repository.get("empty");

        assertTrue(result.isPresent());
        assertEquals(0, result.get().length);
    }

    @Test
    void givenEmptyKey_whenPuttingItem_thenStoresValue() {
        byte[] value = "Alice".getBytes(StandardCharsets.UTF_8);
        repository.put("", value);

        assertArrayEquals(value, repository.get("").orElseThrow());
    }

    @Test
    void givenValueMutatedAfterPut_whenGettingItem_thenReturnsOriginalValue() {
        byte[] value = "Alice".getBytes(StandardCharsets.UTF_8);
        repository.put("name", value);

        value[0] = 'X';

        assertArrayEquals("Alice".getBytes(StandardCharsets.UTF_8), repository.get("name").orElseThrow());
    }

    @Test
    void givenReturnedValueMutated_whenGettingItemAgain_thenReturnsOriginalValue() {
        repository.put("name", "Alice".getBytes(StandardCharsets.UTF_8));

        byte[] firstRead = repository.get("name").orElseThrow();
        firstRead[0] = 'X';

        assertArrayEquals("Alice".getBytes(StandardCharsets.UTF_8), repository.get("name").orElseThrow());
    }

    @Test
    void givenNullValue_whenPuttingItem_thenDoesNotStoreKey() {
        assertThrows(NullPointerException.class, () -> repository.put("name", null));

        assertTrue(repository.get("name").isEmpty());
    }

    @Test
    void givenExistingKey_whenPuttingNullValue_thenPreservesOriginalValue() {
        byte[] original = "Alice".getBytes(StandardCharsets.UTF_8);
        repository.put("name", original);

        assertThrows(NullPointerException.class, () -> repository.put("name", null));

        assertArrayEquals(original, repository.get("name").orElseThrow());
    }
}
