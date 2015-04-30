package rioko.graphabstraction.algorithms.test;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.global.AllInOneNestedBuilder;

public class SimpleAlgorithm extends NestedBuilderAlgorithm {
	public SimpleAlgorithm() {
		super("Total Compactification");
		
		this.addGlobalCriteria(new AllInOneNestedBuilder());
	}
}
