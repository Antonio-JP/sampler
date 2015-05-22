package rioko.graphabstraction.display.global;

import java.util.Set;

import rioko.utilities.Log;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.GlobalNestedBuilder;

public class AllInOneNestedBuilder extends GlobalNestedBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		
		ComposeDiagramNode compose = data.getComposeVertexFactory().createVertex();
		Set<DiagramNode> list = data.vertexSet();
		int nNodes = 1;
		for(DiagramNode node : list) {
			Log.xPrint("Working on node " + nNodes +"/"+list.size());
			nNodes++;
			
			compose.addDiagramNode(node);
		}
		
		target.addVertex(compose);
		
		return target;
	}

	@Override
	public void buildEdges(DiagramGraph target, DiagramGraph data) { }
}
