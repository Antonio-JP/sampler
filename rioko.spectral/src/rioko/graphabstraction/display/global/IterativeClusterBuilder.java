package rioko.graphabstraction.display.global;

import java.util.ArrayList;
import java.util.Collection;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.lalg.jama.JAMAMatrixWrapper;
import rioko.spectral.SpectralAlgorithms;

public class IterativeClusterBuilder extends ClusterBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		Object conf = properties.getConfiguration(DisplayOptions.NUM_CLUSTERS.toString());
		if(!(conf instanceof Integer)) {
			//Unexpected error
			throw new Exception("Bad Configuration given in IterativeClusterBuilder");
		}
		Integer k = (Integer)conf;
		
		return this.getGraphFromClusters(data, SpectralAlgorithms.getIterativeCluster(data,k, JAMAMatrixWrapper.class));
	}

	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		ArrayList<DisplayOptions> res = new ArrayList<>();
		res.add(DisplayOptions.NUM_CLUSTERS);
		
		return res;
	} 
}
