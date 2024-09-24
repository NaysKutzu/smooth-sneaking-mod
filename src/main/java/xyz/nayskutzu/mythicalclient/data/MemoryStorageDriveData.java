package xyz.nayskutzu.mythicalclient.data;

import java.util.HashMap;
import java.util.Map;

public class MemoryStorageDriveData {
    private Map<String, Object> storage;

    public MemoryStorageDriveData() {
        storage = new HashMap<>();
    }

    public void put(String key, Object value) {
        storage.put(key, value);
    }

    public Object get(String key) {
        return storage.get(key);
    }

    public void remove(String key) {
        storage.remove(key);
    }

    public boolean containsKey(String key) {
        return storage.containsKey(key);
    }

    public void clear() {
        storage.clear();
    }
}