package datastructures.hashing;

import java.util.Objects;

/** A separate-chaining hash table with automatic capacity growth. */
public final class HashTable<K, V> {
    private static final class Entry<A, B> {
        final A key; B value; Entry<A, B> next;
        Entry(A key, B value, Entry<A, B> next) {
            this.key = key; this.value = value; this.next = next;
        }
    }
    private Entry<K, V>[] buckets;
    private int size;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public HashTable() { buckets = (Entry<K, V>[]) new Entry[16]; }
    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    /** Associates a non-null key with a value, replacing an earlier value if present. */
    public V put(K key, V value) {
        Objects.requireNonNull(key, "key");
        int bucket = index(key, buckets.length);
        for (Entry<K, V> e = buckets[bucket]; e != null; e = e.next) {
            if (e.key.equals(key)) { V old = e.value; e.value = value; return old; }
        }
        buckets[bucket] = new Entry<>(key, value, buckets[bucket]);
        size++;
        if (size > buckets.length * 0.75) resize();
        return null;
    }
    public V get(K key) {
        Entry<K, V> entry = find(key);
        return entry == null ? null : entry.value;
    }
    public boolean containsKey(K key) { return find(key) != null; }
    private Entry<K, V> find(K key) {
        Objects.requireNonNull(key, "key");
        for (Entry<K, V> e = buckets[index(key, buckets.length)]; e != null; e = e.next)
            if (e.key.equals(key)) return e;
        return null;
    }
    public V remove(K key) {
        Objects.requireNonNull(key, "key");
        int bucket = index(key, buckets.length);
        Entry<K, V> previous = null;
        for (Entry<K, V> e = buckets[bucket]; e != null; e = e.next) {
            if (e.key.equals(key)) {
                if (previous == null) buckets[bucket] = e.next;
                else previous.next = e.next;
                size--; return e.value;
            }
            previous = e;
        }
        return null;
    }
    private int index(K key, int capacity) {
        int hash = key.hashCode();
        hash ^= (hash >>> 16); // Spread high hash bits before reducing modulo capacity.
        return hash & (capacity - 1); // Capacities are powers of two.
    }
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void resize() {
        Entry<K, V>[] old = buckets;
        buckets = (Entry<K, V>[]) new Entry[old.length * 2];
        for (Entry<K, V> chain : old) {
            while (chain != null) {
                Entry<K, V> next = chain.next;
                int bucket = index(chain.key, buckets.length);
                chain.next = buckets[bucket]; buckets[bucket] = chain;
                chain = next;
            }
        }
    }
}
