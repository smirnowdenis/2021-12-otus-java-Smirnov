package ru.otus.homework.cache;

import ru.otus.homework.cache.action.CacheAction;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
    private final Map<K, V> cache = new WeakHashMap<>();
    private final List<WeakReference<HwListener<K, V>>> listeners = new ArrayList<>();

    @Override
    public void put(K key, V value) {
        cache.put(key, value);

        notifyListeners(key, value, CacheAction.PUT);
    }

    @Override
    public void remove(K key) {
        V removedValue = cache.remove(key);

        notifyListeners(key, removedValue, CacheAction.REMOVE);
    }

    @Override
    public V get(K key) {
        V value = cache.get(key);

        notifyListeners(key, value, CacheAction.GET);

        return value;
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listeners.add(new WeakReference<>(listener));
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listeners.removeIf(hwListenerWeakReference -> {
            HwListener<K, V> listenerFromList = hwListenerWeakReference.get();
            return listenerFromList != null && listenerFromList.equals(listener);
        });
    }

    private void notifyListeners(K key, V value, CacheAction action) {
        listeners.forEach(hwListenerWeakReference -> {
            HwListener<K, V> listener = hwListenerWeakReference.get();
            if (listener != null) {
                listener.notify(key, value, action.toString());
            }
        });

        listeners.removeIf(hwListenerWeakReference -> hwListenerWeakReference.get() == null);
    }
}
