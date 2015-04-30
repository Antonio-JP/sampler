package rioko.drawmodels.algorithms.display.test;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.drawmodels.display.filter.FilterEClass;
import rioko.drawmodels.display.local.ModelTreeAlgorithm;
import rioko.graphabstraction.display.global.GlobalLeavesCompactificationNestedBuilder;

public class FullStepsTestAlgorithm extends NestedBuilderAlgorithm {
	
	public FullStepsTestAlgorithm() {
		super("Filter-Leaves-Local");
		
		this.addFilter(new FilterEClass());
		this.addGlobalCriteria(new GlobalLeavesCompactificationNestedBuilder());
		this.addLocalCriteria(new ModelTreeAlgorithm());
	}

}
