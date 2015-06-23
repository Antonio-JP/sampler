package rioko.graphabstraction.display.global;

import java.util.Set;

import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.GlobalNestedBuilder;

public abstract class ClusterBuilder extends GlobalNestedBuilder {

	protected DiagramGraph getGraphFromClusters(DiagramGraph data, Set<Set<DiagramNode>> clusters) {
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
