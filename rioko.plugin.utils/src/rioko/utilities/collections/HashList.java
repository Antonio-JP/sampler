package rioko.utilities.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class HashList<E> implements List<E> {

	private HashMap<E, Integer> map = new HashMap<>();
	private ArrayList<E> list = new ArrayList<>();
	
	@Override
	public int size() {
		return this.list.size();
	}

	@Override
	public boolean isEmpty() {
		return this.list.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return this.map.containsKey(o);
	}

	@Override
	public Iterator<E> iterator() {
		return this.list.iterator();
	}

	@Override
	public Object[] toArray() {
		return this.list.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return this.list.toArray(a);
	}

	@Override
	public boolean add(E e) {
		if(this.map.containsKey(e)) {
			this.map.put(e, this.map.get(e)+1);
		} else {
			this.map.put(e, 1);
		}
		
		return this.list.add(e);
	}

	@Override
	public boolean remove(Object o) {
		Integer apparitions = this.map.get(o);
		if(apparitions != null) {
			if(apparitions == 1) {
				this.map.remove(o);
			} else {
				@SuppressWarnings("unchecked")
				E e = (E) o;
				this.map.put(e, apparitions - 1);
			}
			
			return this.list.remove(o);
		}
		
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return this.map.keySet().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		Boolean res = false;
		
		for(E e : c) {
			res |= this.add(e);
		}
		
		return res;
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		int i = 0;
		Iterator<? extends E> iterator = c.iterator();
		
		while(iterator.hasNext()) {
			this.add(index+i, iterator.next());
			i++;
		}

		return i > 0;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		Boolean res = false;
		for(Object o : c) {
			res |= this.remove(o);
		}
		
		return res;
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		for(E e : this.list) {
			if(!c.contains(e)) {
				this.map.remove(e);
			}
		}
		
		return this.list.retainAll(c);
	}

	@Override
	public void clear() {
		this.map.clear();
		this.list.clear();
	}

	@Override
	public E get(int index) {
		return this.list.get(index);
	}

	@Override
	public E set(int index, E element) {
		E aux = this.list.set(index, element);
		
		if(this.map.get(aux) == 1) {
			this.map.remove(aux);
		} else {
			this.map.put(aux, this.map.get(aux)-1);
		}
		
		if(this.map.get(element) == null) {
			this.map.put(element, 1);
		} else {
			this.map.put(element, this.map.get(element)+1);
		}
		
		return aux;		
	}

	@Override
	public void add(int index, E element) {
		if(this.map.containsKey(element)) {
			this.map.put(element, this.map.get(element)+1);
		} else {
			this.map.put(element, 1);
		}
		
		this.list.add(index, element);
	}

	@Override
	public E remove(int index) {
		E aux = this.list.remove(index);
		
		if(this.map.get(aux) == 1) {
			this.map.remove(aux);
		} else {
			this.map.put(aux, this.map.get(aux)-1);
		}
		
		return aux;
	}

	@Override
	public int indexOf(Object o) {
		return this.list.indexOf(o);
	}

	@Override
	public int lastIndexOf(Object o) {
		return this.list.lastIndexOf(o);
	}

	@Override
	public ListIterator<E> listIterator() {
		return this.list.listIterator();
	}

	@Override
	public ListIterator<E> listIterator(int index) {
		return this.list.listIterator(index);
	}

	@Override
	public List<E> subList(int fromIndex, int toIndex) {
		return this.list.subList(fromIndex, toIndex);
	}

}
