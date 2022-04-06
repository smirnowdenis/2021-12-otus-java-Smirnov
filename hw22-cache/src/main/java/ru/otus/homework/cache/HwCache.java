package ru.otus.homework.cache;

public interface HwCache<K, V> {
    void put(K key, V value);

    void remove(K key);

    V get(K key);

    long size();

    void addListener(HwListener<K, V> listener);

    void removeListener(HwListener<K, V> listener);
}
