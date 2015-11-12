package rioko.utilities.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class DeepStack<E> extends Stack<Collection<E>> {

	private static final long serialVersionUID = 3732752593914748468L;

	private Map<E, Integer> mapOfElements = new HashMap<>();
	
	/* Public methods */
	public boolean containsDeepElement(E element) {
		return this.mapOfElements.containsKey(element);
	}
	
	public Iterator<E> getDeepIterator() {
		return new Iterator<E>() {
			
			private int nCol = this.getFirstCollectionNonEmpty(0);
			private Iterator<E> currentIterator = null;
			
			@Override
			public boolean hasNext() {
				return nCol < size();
			}

			@Override
			public E next() {
				if(this.currentIterator == null) {
					this.currentIterator = get(nCol).iterator();
				}
				
				E res = currentIterator.next();
				
				if(!currentIterator.hasNext()) {
					this.nCol = this.getFirstCollectionNonEmpty(this.nCol + 1);
					
					this.currentIterator = null;
				}
				
				return res;
			}
			
			private int getFirstCollectionNonEmpty(int nCol) {
				int aux = nCol;
				while(aux < size() && get(aux).isEmpty()) {
					aux++;
				}
				
				return aux;
			}
		};
	}
	
	/* Overriden methods */
	@Override
	public Collection<E> push(Collection<E> element) {
		for(E e : element) {
			this.addToMap(e);
		}
		
		return super.push(element);
	}
	
	@Override
	public Collection<E> pop() {
		Collection<E> res = super.pop();
		for(E e : res) {
			this.removeToMap(e);
		}
		
		return res;
	}

	/* Private methods */
	private void addToMap(E e) {
		if(!this.mapOfElements.containsKey(e)) {
			this.mapOfElements.put(e, 0);
		}
		
		this.mapOfElements.put(e, this.mapOfElements.get(e)+1);
	}
	
	private void removeToMap(E e) {
		Integer num = this.mapOfElements.get(e);
		if(num != null && num > 1) {
			this.mapOfElements.put(e, num-1);
		} else if(num != null) {
			this.mapOfElements.remove(e);
		}
	}
}
