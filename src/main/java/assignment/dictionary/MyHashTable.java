package assignment.dictionary;

/*

 */

//
import java.util.*;
import java.io.*;
import java.util.Dictionary;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.BiFunction;

/**

 */
public class MyHashTable<K,V>
{
    // You need to implement this class without using the
    // Hashtable class from Java (“java.util.Hashtable<K,V>”).

   class HashNode<K, V> {
      K key ;
      V value;
      HashNode<K, V> next;

      public HashNode(K key, V value) {
         this.key = key;
         this.value = value;
         this.next = null;
      }
   }

   private ArrayList<HashNode<K, V>> bucketArray;
   private int numBuckets;
   private int size;

   public MyHashTable() {
      bucketArray = new ArrayList<>();
      numBuckets = 20;
      size = 0;
      for (int i = 0; i < numBuckets; i++) {
         bucketArray.add(null);
      }
   }

   public int size(){
      return this.size;
   }

   private int hash(Object key) {
      int hashCode;
      if (key == null){
         hashCode = 0;
      } else {
         hashCode = key.hashCode();
      }

      return Math.abs(hashCode) % numBuckets;
   }


   public V put(K key, V value) {

      if ((1.0 * size) / numBuckets >= 0.7) {
         resize();
      }

      int bucketIndex = hash(key);
      HashNode<K, V> head = bucketArray.get(bucketIndex);
      HashNode<K, V> prev = null;

      // If the bucket is empty, insert the new node directly.
      if (head == null) {
         bucketArray.set(bucketIndex, new HashNode<>(key, value));
         size++;
         return null;
      }

      // Traverse the chain and check if the key already exists.
      boolean keyExists = false;
      while (head != null) {
         if (head.key.equals(key)) {
               keyExists = true;
               break;
         }
         prev = head;
         head = head.next;
      }

      // If the key already exists update its value.
      if (keyExists) {
         V oldValue = head.value;
         head.value = value;
         return oldValue;
      } else {
         // If the key does not exist add it to the end of the chain.
         prev.next = new HashNode<>(key, value);
         size++;
      }
      return null;
   }

   public V remove(K key) {
      int bucketIndex = hash(key);
      HashNode<K, V> head = bucketArray.get(bucketIndex);
      HashNode<K, V> prev = null;

      while (head != null) {
         if (head.key.equals(key)) {
               if (prev != null) {
                  prev.next = head.next;
               } else {
                  // Remove head
                  bucketArray.set(bucketIndex, head.next);
               }
               size--;
               return head.value;
         }
         prev = head;
         head = head.next;
      }

      return null;
   }

   public V get(K key) {
      int bucketIndex = hash(key);
      HashNode<K, V> head = bucketArray.get(bucketIndex);
      
      // find key and return its value
      while (head != null) {
         if (head.key.equals(key)) {
               return head.value;
         }
         head = head.next;
      }
       
      return null;
   }

   public boolean containsKey(Object key) {
      int bucketIndex = hash(key);
      HashNode<K, V> head = bucketArray.get(bucketIndex);

      // find the key
      while (head != null) {
         if (head.key.equals(key)) {
               return true;
         }
         head = head.next;
      }

      // if key not found
      return false;
   }

   public boolean isEmpty(){
      return size == 0;
   }

   public void clear(){
      bucketArray.clear();
      size = 0;

      // Reset to default size
      numBuckets = 20;
      for (int i = 0; i < numBuckets; i++) {
         bucketArray.add(null);
      }
   }

   public boolean equals(Object other) {
      return this == other;
      
   }

   public String toString() {
      String result = "";
      boolean isFirstEntry = true;

      for (K key : this.keySet()) {
         if (!isFirstEntry) {
               result += ", ";
         } else {
               isFirstEntry = false;
         }
         V value = this.get(key);
         // Concatenate key and value to the result string
         result += key + ": " + value; 
      }

      result += " ";
      return result;
   }

   public Set<K> keySet() {
      Set<K> keys = new HashSet<>();
      for (HashNode<K, V> headNode : bucketArray) {
         while (headNode != null) {
               keys.add(headNode.key);
               headNode = headNode.next;
         }
      }
      return keys;
   }

   public List<V> values() {
      List<V> values = new ArrayList<>();
      for (HashNode<K, V> headNode : bucketArray) {
         while (headNode != null) {
               values.add(headNode.value);
               headNode = headNode.next;
         }
      }
      return values;
   }

   private void resize() {
      ArrayList<HashNode<K, V>> temp = bucketArray;
      bucketArray = new ArrayList<>();
      numBuckets *= 2;
      size = 0;
      for (int i = 0; i < numBuckets; i++) {
         bucketArray.add(null);
      }
      for (HashNode<K, V> headNode : temp) {
         while (headNode != null) {
               put(headNode.key, headNode.value);
               headNode = headNode.next;
         }
      }
   }
}




