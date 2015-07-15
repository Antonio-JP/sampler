package rioko.emfdrawer.xmiDiagram;

import java.util.Collection;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;

public class ComposeXMIDiagramNodeFactory implements VertexFactory<ComposeXMIDiagramNode> {

	@SuppressWarnings("unchecked")
	@Override
	public ComposeXMIDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new ComposeXMIDiagramNode();
		} else if(arg0[0] instanceof Collection<?>){
			return new ComposeXMIDiagramNode((Collection<? extends DiagramNode>) arg0[0]);
		}
		
		return null;
	}

}
