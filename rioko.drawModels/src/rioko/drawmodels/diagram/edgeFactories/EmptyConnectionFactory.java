package rioko.drawmodels.diagram.edgeFactories;

import org.jgrapht.EdgeFactory;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.EmptyConnection;

public class EmptyConnectionFactory implements EdgeFactory<DiagramNode, EmptyConnection> {

	@Override
	public EmptyConnection createEdge(DiagramNode arg0, DiagramNode arg1) {
		return new EmptyConnection(arg0, arg1);
	}

}
