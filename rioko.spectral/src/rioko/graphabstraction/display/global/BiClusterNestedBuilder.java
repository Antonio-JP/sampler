package rioko.graphabstraction.display.global;

import java.util.Set;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.lalg.jama.JAMAMatrixWrapper;
import rioko.spectral.SpectralAlgorithms;

public class BiClusterNestedBuilder extends GlobalNestedBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		Set<Set<DiagramNode>> clusters = SpectralAlgorithms.getBiCluster(data, JAMAMatrixWrapper.class);
		
		if(clusters != null && clusters.size() > 0) {
			DiagramGraph target = data.getSimilarType();
			
			for(Set<DiagramNode> set : clusters) {
				target.addVertex(target.getComposeVertexFactory().createVertex(set));
			}
			
			return target;
		}
		
		return data;
	}

}
