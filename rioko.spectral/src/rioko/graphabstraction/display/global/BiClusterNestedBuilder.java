package rioko.graphabstraction.display.global;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.lalg.jama.JAMAMatrixWrapper;
import rioko.spectral.SpectralAlgorithms;

public class BiClusterNestedBuilder extends ClusterBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		return this.getGraphFromClusters(data, SpectralAlgorithms.getBiCluster(data, JAMAMatrixWrapper.class));
	}

}
