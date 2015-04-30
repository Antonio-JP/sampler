package rioko.drawmodels.algorithms.display.test;

import rioko.drawmodels.display.local.ModelTreeAlgorithm;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;

public class SimpleLocalAlgorithm extends NestedBuilderAlgorithm {

	public SimpleLocalAlgorithm() {
		super("Local criteria");
		
		this.addLocalCriteria(new ModelTreeAlgorithm());
	}

}
