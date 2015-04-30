package rioko.drawmodels.algorithms.display.test;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.drawmodels.display.filter.FilterEClass;

public class EClassFilterAlgorithm extends NestedBuilderAlgorithm {
	
	public EClassFilterAlgorithm() {
		super("EClass filter");
		
		this.addFilter(new FilterEClass());
		
	}
}
