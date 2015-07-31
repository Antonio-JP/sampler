package rioko.graphabstraction.display.global;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.MaxClusterConfiguration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.lalg.jama.JAMAMatrixWrapper;
import rioko.spectral.SpectralAlgorithms;

public class HierarchicalClusterBuilder extends ClusterBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		Object conf = properties.getConfiguration(MaxClusterConfiguration.class);
		if(!(conf instanceof Integer)) {
			//Unexpected error
			throw new Exception("Bad Configuration given in IterativeClusterBuilder");
		}
		Integer k = (Integer)conf;
		
		return this.getGraphFromClusters(data, SpectralAlgorithms.getHierarchicalCluster(data,k, JAMAMatrixWrapper.class));
	}

	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		ArrayList<Class<? extends Configuration>> res = new ArrayList<>();
		res.add(MaxClusterConfiguration.class);
		
		return res;
	} 

}
