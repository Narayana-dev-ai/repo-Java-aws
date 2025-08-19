import java.util.*;
import java.lang.*;
import java.io.*;
import java.util.concurrent.ConcurrentHashMap;

class Entry<V> {
    private final V value;
    private final long ttl;
    private final long currTime;
    
    public Entry(V value, long ttl) {
        this.value = value;
        this.currTime = System.currentTimeMillis();
        this.ttl = ttl;
    }
    
    public boolean isExpired() {
        if(this.ttl <= 0) return false;
        return System.currentTimeMillis() - this.currTime > this.ttl;
    }
    
    public V getValue() {
        return this.value;
    }
}

public interface KeyValueStore<K, V> {
    void put(K key, V value);
    void put(K key, V value, long ttl);
    V get(K key);
    void delete(K key);
    boolean contains(K key);
    int size();
}

class InMemoryStor<K, V> implements KeyValueStore<K, V> {
    private final Map<K, Entry<V>> store = new ConcurrentHashMap<>();
    
    @Override
    public void put(K key, V value) {
        store.put(key, new Entry<>(value, 0));
    }
    
    @Override
    public void put(K key, V value, long ttl) {
        store.put(key, new Entry<>(value, ttl));
    }
    
    @Override
    public V get(K key) {
        Entry<V> entry = store.get(key);
        if(entry == null) return null;
        if(entry.isExpired()) {
            store.remove(key);
            return null;
        }
        return entry.getValue();
    }
    
    @Override
    public void delete(K key) {
        store.remove(key);
    }
    
    @Override
    public boolean contains(K key) {
        Entry<V> entry = store.get(key);
        return entry != null && !entry.isExpired();
    }
    
    @Override
    public int size() {
        store.entrySet().removeIf((item) -> item.getValue().isExpired());
        return store.size();
    }
}

class Codechef {
	public static void main (String[] args) throws java.lang.Exception {
	    InMemoryStor<String, String> kvStore = new InMemoryStor<>();
	    
	    kvStore.put("userName","Narayana");
	    kvStore.put("session", "token123", 2000);
	    
	    System.out.println(kvStore.get("userName"));
	    System.out.println(kvStore.get("session"));
	    
	    Thread.sleep(2500);
	    System.out.println(kvStore.get("session"));
	}
}
