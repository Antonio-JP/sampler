package rioko.graphabstraction.algorithms;

import rioko.graphabstraction.display.global.ConnectedComponentsBuilder;

public class ConnectedComponentsAlgorithm extends NestedBuilderAlgorithm {
	public ConnectedComponentsAlgorithm() {
		this.addGlobalCriteria(new ConnectedComponentsBuilder());
	}
}
