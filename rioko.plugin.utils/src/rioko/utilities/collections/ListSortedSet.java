package rioko.utilities.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.SortedSet;

public abstract class ListSortedSet<E> implements SortedSet<E> {

	private ArrayList<E> sortedElements = new ArrayList<>();
	private HashSet<E> set = new HashSet<>();
	
	@Override
	public boolean add(E el) {
		if(!set.contains(el)) {			
			Comparator<E> comparator = this.comparator();
			int i = 0;
			while(i < set.size() && comparator.compare(el, sortedElements.get(i)) > 0) {
				i++;
			}
			
			set.add(el);
			if(i == set.size()) {
				sortedElements.add(el);
			} else {
				sortedElements.add(i, el);
			}

			return true;
		}
		
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		boolean res = false;
		for(E el : collection) {
			res |= this.add(el);
		}
		
		return res;
	}

	@Override
	public void clear() {
		this.set.clear();
		this.sortedElements.clear();
	}

	@Override
	public boolean contains(Object obj) {
		return this.set.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return this.set.containsAll(collection);
	}

	@Override
	public boolean isEmpty() {
		return this.set.isEmpty();
	}

	@Override
	public Iterator<E> iterator() {
		return this.sortedElements.iterator();
	}

	@Override
	public boolean remove(Object obj) {
		if(set.remove(obj)) {
			this.sortedElements.remove(obj);
			return true;
		}
		
		return false;
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
		this.sortedElements.retainAll(collection);
		return this.set.retainAll(collection);
	}

	@Override
	public int size() {
		return this.set.size();
	}

	@Override
	public Object[] toArray() {
		return this.sortedElements.toArray();
	}

	@Override
	public <T> T[] toArray(T[] array) {
		return this.sortedElements.toArray(array);
	}

	@Override
	public abstract Comparator<E> comparator();

	@Override
	public E first() {
		if(this.size() > 0) {
			return this.sortedElements.get(0);
		} else {
			return null;
		}
	}

	@Override
	public SortedSet<E> headSet(E toElement) {
		return this.subSet(this.first(), toElement);
	}

	@Override
	public E last() {
		return sortedElements.get(this.size()-1);
	}

	@Override
	public SortedSet<E> subSet(E fromElement, E toElement) {
		Iterator<E> iterator = this.iterator();
		Comparator<E> comparator = this.comparator();
		
		SortedSet<E> subSet = this.getEmptySet();
		
		while(iterator.hasNext() && comparator.compare(fromElement, iterator.next()) > 0);
		
		while(iterator.hasNext()) {
			E element = iterator.next();
			
			if(comparator.compare(toElement, element) < 0) {
				break;
			}
			
			subSet.add(element);
		}
		
		return subSet;
	}

	@Override
	public SortedSet<E> tailSet(E fromElement) {
		return this.subSet(fromElement, this.last());
	}

	//Protected methods
	protected void reorder() {
		Comparator<E> comparator = this.comparator();
		
		ArrayList<E> newList = new ArrayList<>();
		
		for(E element : this.set) {
			int i = 0;
			while(i < newList.size() && comparator.compare(element, newList.get(i)) > 0) {
				i++;
			}
			
			if(i == newList.size()) {
				newList.add(element);
			} else {
				newList.add(i, element);
			}
		}
		
		this.sortedElements = newList;
	}
	
	//Abstract methods
	public abstract SortedSet<E> getEmptySet();
}
