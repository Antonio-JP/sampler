package rioko.runtime.registers.algorithm;

import java.util.Comparator;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.algorithms.TrivialBuilderAlgorithm;
import rioko.graphabstraction.algorithms.test.SimpleAlgorithm;

public class AlgorithmComparator implements Comparator<NestedBuilderAlgorithm> {

	@Override
	public int compare(NestedBuilderAlgorithm o1, NestedBuilderAlgorithm o2) {
		if(o1.getClass().equals(o2.getClass())) {
			return 0;
		} else if(o1 instanceof SimpleAlgorithm) {
			return -1;
		} else if(o2 instanceof SimpleAlgorithm) {
			return 1;
		} else if(o1 instanceof TrivialBuilderAlgorithm) {
			return -1;
		} else if(o2 instanceof TrivialBuilderAlgorithm) {
			return 1;
		} 

		return 0;
	}


}
