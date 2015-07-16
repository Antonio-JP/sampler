package rioko.utilities.collections;

import java.util.Set;

/**
 * Class implementing different set operations.
 * 
 * @author Antonio
 *
 * @param <E> Class of the elements that will contain the sets this class will opperate.
 */
public class SetUtils<E> {
	
	/**
	 * method to calculate the union of two sets.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return A new Set&#60;E&#62; with the union of set1 and set2
	 */
	public Set<E> union(Set<E> set1, Set<E> set2) {
		ListSet<E> un = new ListSet<>(set1);
		
		for(E element : set2) {
			un.add(element);
		}
		
		return un;
	}
	
	/**
	 * Method to calculate the intersection of two sets.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return A new Set&#60;E&#62; with the intersection of set1 and set2
	 */
	public Set<E> intersection(Set<E> set1, Set<E> set2) {
		if(set1.size() > set2.size()) {
			return this.intersection(set2, set1);
		}
		
		ListSet<E> in = new ListSet<>();
		
		for(E el1 : set1) {
			if(set2.contains(el1)) {
				in.add(el1);
			}
		}
		
		return in;
	}
	
	/**
	 * Method to calculate the difference of two sets.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return A new Set&#60;E&#62; with the difference of set1 and set2
	 */
	public Set<E> minus(Set<E> set1, Set<E> set2) {
		ListSet<E> mi = new ListSet<>(set1);
		
		for(E element : set2) {
			mi.remove(element);
		}
		
		return mi;
	}
	
	/**
	 * Method to calculate the symmetric difference of two sets.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return A new Set&#60;E&#62; with the symmetric difference of set1 and set2
	 */
	public Set<E> symDiff(Set<E> set1, Set<E> set2) {
		return this.minus(this.union(set1,set2), this.intersection(set1,set2));
	}
	
	/**
	 * Method to calculate if a set is subset of another set.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return True if set1 is subset of set2 and false otherwise.
	 */
	public boolean isSubset(Set<E> set1, Set<E> set2) {
		for(E element : set1) {
			if(!set2.contains(element)) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Method to calculate if a set is superset of another set.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return True if set1 is superset of set2 and false otherwise.
	 */
	public boolean isSuperset(Set<E> set1, Set<E> set2) {
		return this.isSubset(set2, set1);
	}
	
	/**
	 * Method to calculate if a set is equal (as set) to another set.
	 * 
	 * @param set1 A set of elements.
	 * @param set2 Another set.
	 * 
	 * @return True if set1 has the same elements that set2 and false otherwise.
	 */
	public boolean areEquals(Set<E> set1, Set<E> set2) {
		return (this.isSubset(set1,set2)) && (set1.size() == set2.size());
	}
}
