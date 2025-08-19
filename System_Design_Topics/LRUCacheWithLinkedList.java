import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class LRUCache<K, V> {
    private final int capacity;
    private final LinkedList<K> cacheList;
    private final Map<K, V> cacheMap;

    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.cacheList = new LinkedList<>();
        this.cacheMap = new HashMap<>();
    }

    public V get(K key) {
        if (!cacheMap.containsKey(key)) {
            return null;
        }
        // Move the key to the front of the list to mark it as recently used
        cacheList.remove(key);
        cacheList.addFirst(key);
        return cacheMap.get(key);
    }

    public void put(K key, V value) {
        if (cacheMap.containsKey(key)) {
            // If the key already exists, update its value and move it to the front
            cacheList.remove(key);
        } else if (cacheList.size() == capacity) {
            // If the cache is full, remove the least recently used item
            K lruKey = cacheList.removeLast();
            cacheMap.remove(lruKey);
        }
        // Add the new key to the front of the list and update the map
        cacheList.addFirst(key);
        cacheMap.put(key, value);
    }

    public static void main(String[] args) {
        LRUCache<Integer, String> cache = new LRUCache<>(3);
        cache.put(1, "One");
        cache.put(2, "Two");
        cache.put(3, "Three");

        System.out.println(cache.get(1)); // Output: One

        cache.put(4, "Four");

        System.out.println(cache.get(2)); // Output: null (2 was the least recently used and was evicted)
        System.out.println(cache.get(3)); // Output: Three
        System.out.println(cache.get(4)); // Output: Four
    }
}