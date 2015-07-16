package rioko.searchers;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Search iterator throught different elements. This abstract class gives an interface to 
 * code search algorithms.
 * 
 * @author Antonio
 *
 * @param <E> Class of the elements that are being searched.
 */
public abstract class SearchIterator<E> implements Iterator<E> {
	protected HashMap<E,E> parentOf;
	
	public SearchIterator()
	{
		this.parentOf = new HashMap<>();
	}
	
	//Search methods
	/**
	 * Public method that allows to know if an element has been found in the search. It is 
	 * necessary to use before getParent() method.
	 * 
	 * @param object Object to check
	 * 
	 * @return True if the iterator has pass through this object and false otherwise
	 */
	public boolean isFound(E object) {
		return this.parentOf.containsKey(object);
	}
	
	/**
	 * Public method to get the parent of an element that has been found in the search. Return 
	 * null if it is the root. If the object has not been yet found, this method will return 
	 * null too. ¡Be careful!
	 * 
	 * @param object Object to check
	 * 
	 * @return E with the parent of object. If it is the root of the search, returns null.
	 */
	public E getParent(E object) {
		return this.parentOf.get(object);
	}
	
	//Iterator methods
	@Override
	public void remove() { /* do nothing */ }
}
