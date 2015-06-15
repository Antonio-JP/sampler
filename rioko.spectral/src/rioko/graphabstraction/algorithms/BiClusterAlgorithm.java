package rioko.graphabstraction.algorithms;

import rioko.graphabstraction.display.global.BiClusterNestedBuilder;

public class BiClusterAlgorithm extends NestedBuilderAlgorithm {
	public BiClusterAlgorithm() {
		this.addGlobalCriteria(new BiClusterNestedBuilder());
	}
}
