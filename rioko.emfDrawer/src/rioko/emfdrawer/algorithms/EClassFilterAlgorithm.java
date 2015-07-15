package rioko.emfdrawer.algorithms;

import rioko.emfdrawer.display.filter.FilterEClass;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;

public class EClassFilterAlgorithm extends NestedBuilderAlgorithm {
	
	public EClassFilterAlgorithm() {
		super("EClass filter");
		
		this.addFilter(new FilterEClass());
		
	}
}
