package rioko.graphabstraction.runtime.registers.algorithm;

import java.util.Comparator;
import java.util.SortedSet;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.utilities.collections.ListSortedSet;

public class SortedAlgorithmSet extends ListSortedSet<NestedBuilderAlgorithm> {

	private AlgorithmComparator comparator = new AlgorithmComparator();
	
	//Builders
	public SortedAlgorithmSet() {
		super();
	}
	
	public SortedAlgorithmSet(AlgorithmComparator comparator) {
		this();
		
		this.comparator = comparator;
	}
	
	//Setters
	public void setComparator(AlgorithmComparator comparator) {
		this.comparator = comparator;
		
		this.reorder();
	}
	
	//ListSortedSet methods
	@Override
	public Comparator<NestedBuilderAlgorithm> comparator() {
		return this.comparator;
	}

	@Override
	public SortedSet<NestedBuilderAlgorithm> getEmptySet() {
		return new SortedAlgorithmSet();
	}

}
