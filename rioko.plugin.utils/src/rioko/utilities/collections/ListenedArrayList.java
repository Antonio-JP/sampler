package rioko.utilities.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

import rioko.events.DataChangeEvent;

public class ListenedArrayList<E> extends ArrayList<E> {

	private static final long serialVersionUID = -2895438079627325877L;
	
	//Builders
	public ListenedArrayList() {
		super();
	}
	
	public ListenedArrayList(Collection<? extends E> c) {
		super(c);
	}
	
	public ListenedArrayList(int initialCapacity) {
		super(initialCapacity);
	}
	
	//Event throw method
	private void throwEvent() {
		new DataChangeEvent(this);
	}
	
	//Overriden methods
	@Override
	public boolean add(E e) {
		boolean res = super.add(e);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public void add(int index, E element) {
		super.add(index, element);
		
		this.throwEvent();
	}
	
	@Override
	public boolean addAll(Collection<? extends E> c) {
		boolean res = super.addAll(c);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		boolean res = super.addAll(index, c);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public void clear() {
		int s = this.size();
		
		super.clear();
		
		if(s != 0) {
			this.throwEvent();
		}
	}
	
	@Override
	public boolean remove(Object o) {
		boolean res = super.remove(o);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public E remove(int index) {
		E res = super.remove(index);
		
		if(res != null) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		boolean res = super.removeAll(c);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public boolean removeIf(Predicate<? super E> filter) {
		boolean res = super.removeIf(filter);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
	
	@Override
	public boolean retainAll(Collection<?> c) {
		boolean res = super.retainAll(c);
		
		if(res) {
			this.throwEvent();
		}
		return res;
	}
}
