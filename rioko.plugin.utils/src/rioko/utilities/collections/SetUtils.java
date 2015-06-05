package rioko.utilities.collections;

import java.util.Set;

public class SetUtils<E> {
	
	public Set<E> union(Set<E> set1, Set<E> set2) {
		ListSet<E> un = new ListSet<>(set1);
		
		for(E element : set2) {
			un.add(element);
		}
		
		return un;
	}
	
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
	
	public Set<E> minus(Set<E> set1, Set<E> set2) {
		ListSet<E> mi = new ListSet<>(set1);
		
		for(E element : set2) {
			mi.remove(element);
		}
		
		return mi;
	}
	
	public Set<E> symDiff(Set<E> set1, Set<E> set2) {
		return this.minus(this.union(set1,set2), this.intersection(set1,set2));
	}
	
	public boolean isSubset(Set<E> set1, Set<E> set2) {
		for(E element : set1) {
			if(!set2.contains(element)) {
				return false;
			}
		}
		
		return true;
	}
	
	public boolean isSuperset(Set<E> set1, Set<E> set2) {
		return this.isSubset(set2, set1);
	}
	
	public boolean areEquals(Set<E> set1, Set<E> set2) {
		return (this.isSubset(set1,set2)) && (set1.size() == set2.size());
	}
}
