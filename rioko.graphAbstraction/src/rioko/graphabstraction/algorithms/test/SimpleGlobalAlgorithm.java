package rioko.graphabstraction.algorithms.test;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.global.GlobalLeavesCompactificationNestedBuilder;

public class SimpleGlobalAlgorithm extends NestedBuilderAlgorithm {

	public SimpleGlobalAlgorithm() {
		super("Leaves compactification");
		
		this.addGlobalCriteria(new GlobalLeavesCompactificationNestedBuilder());
	}

}
