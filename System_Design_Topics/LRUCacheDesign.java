public interface Cache<K, V> {
    void put(K key, V value);
    V get(K key);
    void remove(K key);
    int size();
    boolean isEmpty();
}

public class CacheNode<K, V> {
    K key;
    V value;
    CacheNode<K, V> prev;
    CacheNode<K, V> next;
    
    public CacheNode(K key, V value) {
        this.key = key;
        this.value = value;
    }
}


import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> implements Cache<K, V> {
    private final int capacity;
    private final Map<K, CacheNode<K, V>> map;
    private final CacheNode<K, V> head;
    private final CacheNode<K, V> tail;
    
    public LRUCache(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
        this.head = new CacheNode<>(null, null);
        this.tail = new CacheNode<>(null, null);
        this.head.next = this.tail;
        this.tail.prev = this.head;
    }
    
    @Override
    public void put(K key, V value) {
        CacheNode<K, V> node = map.get(key);
        if(node == null) {
            if(map.size() >= this.capacity) {
                remove(node.key);
            }
            node = new CacheNode<>(key, value);
            map.put(key, node);
            addToFront(node);
        } else {
            node.value = value;
            moveToFront(node);
        }
    }
    
    @Override
    public V get(K key) {
        CacheNode<K, V> node = map.get(key);
        if(node == null) {
            return null;
        }
        moveToFront(node);
        return node.value;
    }
    
    @Override
    public void remove(K key) {
        CacheNode<K, V> node = map.get(key);
        if(node != null) {
            removeNode(node);
        }
        
    }
    
    @Override
    public int size() {
        return map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }
    
    private void addToFront(CacheNode<K, V> node) {
        node.next = this.head.next;
        node.prev = this.head;
        this.head.next.prev = node;
        this.head.next = node;
    }
    
    private void moveToFront(CacheNode<K, V> node) {
        removeNode(node);
        addToFront(node);
    }
    
    private void removeNode(CacheNode<K, V> node) {
        node.prev.next = node.next;
        node.next.prev = node.prev;
    }
}



public class Main {
	public static void main(String[] args) throws Exception {
	    LRUCache<String, String> cache = new LRUCache<>(3);
	    
	    cache.put("name", "Narayana");
	    cache.put("company", "AIRBUS");
	    
		System.out.println("Hello World" + cache.get("company"));
		
		cache.put("city", "Banglore");
		
		System.out.println(cache.get("name"));
	}
}