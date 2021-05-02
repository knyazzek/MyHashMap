package map;

import com.google.common.math.IntMath;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

public class MyHashMap<K,V> {
    private int capacity;
    final float LOAD_FACTOR;
    private int size;
    private int threshold;

    static final int DEFAULT_INITIAL_CAPACITY = 16;
    static final float DEFAULT_LOAD_FACTOR = 0.7f;
    final int MAX_CAPACITY = 1 << 30;

    private Bucket<K,V>[] buckets;

    public MyHashMap() {
        this(DEFAULT_INITIAL_CAPACITY);
    }

    public MyHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public MyHashMap(int initialCapacity, float loadFactor) {
        if (initialCapacity <= 0 || loadFactor <= 0
                || initialCapacity > MAX_CAPACITY) {
            throw new IllegalArgumentException("Incorrect initialCapacity value.");
        }

        this.capacity = IntMath.ceilingPowerOfTwo(initialCapacity);
        this.LOAD_FACTOR = loadFactor;
        this.threshold = (int) (capacity * LOAD_FACTOR);
        buckets =  (Bucket<K,V>[])new Bucket[capacity];
    }

    public V put(K key, V value) {
        V res = put(key, value, buckets);
        if (res == null) {
            size++;
        }

        if (isThresholdExceeded()) {
            resize();
        }
        return res;
    }

    private V put(K key, V value, Bucket<K,V>[] buckets) {
        int index = hash(key);
        Node<K,V> nodeToAdd = new Node<>(key, value);

        if (buckets[index] == null) {
            buckets[index] = new Bucket<>(nodeToAdd);
            return null;
        }

        return buckets[index].addNode(nodeToAdd);
    }

    public V get(K key) {
        int index = hash(key);
        if (buckets[index] != null) {
            return buckets[index].getNode(key);
        }
        return null;
    }

    public V remove(K nodeToRemoveKey) {
        int index = hash(nodeToRemoveKey);
        if (buckets[index] != null) {
            size--;
            return buckets[index].removeNode(nodeToRemoveKey);
        }
        return null;
    }

    private boolean isThresholdExceeded() {
        if (size > MAX_CAPACITY) {
            throw new RuntimeException("HashMap overflow.");
        }

        return size > threshold;
    }

    private void resize() {
        capacity *= 2;
        threshold = (int) (capacity * LOAD_FACTOR);
        Bucket<K,V>[] bucketsTmp = (Bucket<K,V>[])new Bucket[capacity];

        for (Bucket<K,V> bucket : buckets) {
            if (bucket != null) {
                for (Node<K,V> node : bucket.nodes) {
                    put(node.key, node.value, bucketsTmp);
                }
            }
        }
        this.buckets = bucketsTmp;
    }

    private int hash(K key) {
        if (key == null) return 0;

        return key.hashCode() & (capacity-1);
    }

    private static class Bucket<K,V> {
        private final LinkedList<Node<K,V>> nodes;

        public Bucket(Node<K,V> node) {
            nodes = new LinkedList<>();
            nodes.add(node);
        }

        public V addNode(Node<K,V> nodeToAdd) {
            if (nodeToAdd.key == null) {
                for (Node<K,V> node : nodes) {
                    if (node.key == null) {
                        V value = node.value;
                        node.value = nodeToAdd.value;
                        return value;
                    }
                }
            } else {
                for (Node<K,V> node : nodes) {
                    if (node.key.equals(nodeToAdd.key)) {
                        V value = node.value;
                        node.value = nodeToAdd.value;
                        return value;
                    }
                }
            }
            nodes.add(nodeToAdd);
            return null;
        }

        public V getNode(K key) {
            for (Node<K,V> node : nodes) {
                if (node.key.equals(key)) {
                    return node.value;
                }
            }
            return null;
        }

        public V removeNode(K nodeToDeleteKey) {
            if (nodeToDeleteKey == null) {
                for (Node<K,V> node : nodes) {
                    if (node.key == null) {
                        nodes.remove(node);
                        return node.value;
                    }
                }
            } else {
                for (Node<K,V> node : nodes) {
                    if (node.key.equals(nodeToDeleteKey)){
                        nodes.remove(node);
                        return node.value;
                    }
                }
            }
            return null;
        }

        public int size() {
            return nodes.size();
        }

        @Override
        public String toString() {
            return "{" + nodes + '}';
        }
    }

    static class Node<K,V> {
        private final K key;
        private V value;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        @Override
        public String toString() {
            return "" + key + "=" + value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node<?, ?> node = (Node<?, ?>) o;
            return Objects.equals(key, node.key) &&
                    Objects.equals(value, node.value);
        }

        @Override
        public int hashCode() {
            return Objects.hash(key);
        }
    }

    public int size() {
        return size;
    }

    public boolean containKey(K key) {
        return keySet().contains(key);
    }

    public boolean containValue(V value) {
        return values().contains(value);
    }

    public Set<Node<K,V>> nodeSet() {
        Set<Node<K,V>> nodeSet = new HashSet<>();

        for (Bucket<K,V> bucket : buckets) {
            if (bucket != null) {
                nodeSet.addAll(bucket.nodes);
            }
        }
        return nodeSet;
    }

    public Set<K> keySet() {
        Set<K> keySet = new HashSet<>();

        for (Bucket<K,V> bucket : buckets) {
            if (bucket != null) {
                for (Node<K,V> node : bucket.nodes) {
                    keySet.add(node.key);
                }
            }
        }
        return keySet;
    }

    public Set<V> values() {
        Set<V> values = new HashSet<>();

        for (Bucket<K,V> bucket : buckets) {
            if (bucket != null) {
                for (Node<K,V> node : bucket.nodes) {
                    values.add(node.value);
                }
            }
        }
        return values;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();

        for (Bucket<K,V> bucket : buckets) {
            if (bucket != null && bucket.size() != 0) {
                stringBuilder.append(bucket);
            }
        }
        return stringBuilder.toString();
    }
}
