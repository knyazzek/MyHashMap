package map;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

class MyHashMapTest extends TestCase {
    MyHashMap<Integer, String> myHashMap;

    @BeforeEach
    public void setUp() {
        myHashMap = new MyHashMap<>();
        myHashMap.put(1,"one");
        myHashMap.put(2,"two");
        myHashMap.put(3,"three");
        myHashMap.put(4,"four");
        myHashMap.put(5,"five");
        myHashMap.put(6,"six");
    }

    @Test
    void put() {
        myHashMap.put(10, "ten");
        String expected = myHashMap.get(10);
        String actual = "ten";
        Assert.assertEquals(expected, actual);
    }

    @Test
    void putOverwrite() {
        String expected = myHashMap.put(1, "someValue");
        String actual = "one";

        if (myHashMap.get(1).equals(actual))
            Assert.fail("The value wasn't overwritten.");

        Assert.assertEquals("The put method returned did not return the old value",
                expected, actual);
    }

    @Test
    void putChangeSize() {
        myHashMap.put(7, "seven");
        int expected = myHashMap.size();
        int actual = 6 + 1;
        Assert.assertEquals("the method returned did not change the size", expected, actual);
    }

    @Test
    void remove() {
        myHashMap.remove(1);

        if (myHashMap.containKey(1)) {
            Assert.fail("Element wasn't remove.");
        }
    }

    @Test
    void size() {
        int expected = myHashMap.size();
        int actual = 6;
        Assert.assertEquals(expected, actual);
    }

    @Test
    void nodeSet() {
        Set<MyHashMap.Node<Integer, String>> expected = myHashMap.nodeSet();
        Set<MyHashMap.Node<Integer,String>> actual = new HashSet<>();
        actual.add(new MyHashMap.Node<>(1, "one"));
        actual.add(new MyHashMap.Node<>(2, "two"));
        actual.add(new MyHashMap.Node<>(3, "three"));
        actual.add(new MyHashMap.Node<>(4, "four"));
        actual.add(new MyHashMap.Node<>(5, "five"));
        actual.add(new MyHashMap.Node<>(6, "six"));
        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    void keySet() {
        Set<Integer> expected = myHashMap.keySet();
        Set<Integer> actual = new HashSet<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    void keySet_NOT_NULL() {
        myHashMap = new MyHashMap<>();
        Assert.assertNotNull(myHashMap.keySet());
    }

    @Test
    void values() {
        Set<String> expected = myHashMap.values();
        Set<String> actual = new HashSet<>(Arrays.asList("one", "two", "three", "four", "five", "six"));
        Assertions.assertIterableEquals(actual, expected);
    }

    @Test
    void values_NOT_NULL() {
        myHashMap = new MyHashMap<>();
        Assert.assertNotNull(myHashMap.values());
    }

    @Test
    void containKey() {
        myHashMap.put(10, "ten");
        Assert.assertTrue(myHashMap.containKey(10));
    }

    @Test
    void containValue() {
        myHashMap.put(10, "ten");
        Assert.assertTrue(myHashMap.containValue("ten"));
    }
}