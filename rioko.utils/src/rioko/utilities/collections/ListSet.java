package rioko.utilities.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Set implementation which, when it is needed to iterate over it, preserves the order of the element in order of adding.
 * 
 * @author Rioukay
 *
 * @param <E> Class for the set elements
 */
public class ListSet<E> implements Set<E> {

	//Private Attributes
	private ArrayList<E> orderedSet = new ArrayList<>();
	private HashSet<E> quickSet = new HashSet<>();
	
	//Builders
	public ListSet() { }
	
	public ListSet(int capacity) {
		this.orderedSet = new ArrayList<E>(capacity);
	}
	
	public ListSet(Collection<? extends E> collection) {
		this.orderedSet = new ArrayList<E>(collection);
	}
	
	//Set and Collection Methods
	@Override
	public boolean add(E element) {
		if(!this.contains(element)) {
			this.orderedSet.add(element);
			this.quickSet.add(element);
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean res = false;
		
		for(E element : collection) {
			res |= this.add(element);
		}
		
		return res;
	}

	@Override
	public void clear() {
		this.orderedSet.clear();
		this.quickSet.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return this.quickSet.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		boolean res = true;
		
		for(Object obj : collection) {
			res &= this.contains(obj);
		}
		
		return res;
	}

	@Override
	public boolean isEmpty() {
		return this.orderedSet.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.orderedSet.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		this.quickSet.remove(obj);
		return this.orderedSet.remove(obj);
	}

	@Override
	public boolean removeAll(Collection<?> collection) {
		boolean res = false;
		
		for(Object obj : collection) {
			res |= this.remove(obj);
		}
		
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> collection) {
		this.quickSet.retainAll(collection);
		return this.orderedSet.retainAll(collection);
	}

	@Override
	public int size() {
		return this.orderedSet.size();
	}

	@Override
	public Object[] toArray() {
		return this.orderedSet.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return this.orderedSet.toArray(array);
	}

}
