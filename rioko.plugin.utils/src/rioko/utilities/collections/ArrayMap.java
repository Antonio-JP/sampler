package rioko.utilities.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Map implementation which preserves the order of the keys when it is needed loop over them.
 * 
 * @author Rioko
 *
 * @param <K> Class for the keys
 * @param <V> Class for the values
 */
public class ArrayMap<K, V> implements Map<K, V> {

	//Private Attributes
	private HashMap<K,V> map = new HashMap<>();
	private ListSet<K> keySet = new ListSet<K>();
	
	@Override
	public void clear() {
		this.map.clear();
		this.keySet.clear();
	}

	@Override
	public boolean containsKey(Object obj) {
		return this.keySet.contains(obj);
	}

	@Override
	public boolean containsValue(Object obj) {
		return this.map.containsValue(obj);
	}

	@Override
	public Set<Entry<K, V>> entrySet() {
		return this.map.entrySet();
	}

	@Override
	public V get(Object obj) {
		return this.map.get(obj);
	}

	@Override
	public boolean isEmpty() {
		return this.keySet.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return this.keySet;
	}

	@Override
	public V put(K key, V value) {
		if(key == null || this.containsKey(key)) {
			return null;
		}
		
		this.map.put(key, value);
		this.keySet.add(key);
		
		return value;
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> map) {
		for(K key : map.keySet()) {
			this.put(key, map.get(key));
		}
	}

	@Override
	public V remove(Object obj) {
		if(this.containsKey(obj)) {
			this.keySet.remove(obj);

			return this.map.remove(obj);
		}
		
		return null;
	}

	@Override
	public int size() {
		return this.keySet().size();
	}

	@Override
	public Collection<V> values() {
		return this.map.values();
	}

}
