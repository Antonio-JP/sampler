package rioko.gmldrawer.gmlDiagram.factories;

import java.util.Collection;

import rioko.gmldrawer.gmlDiagram.ComposeGMLDiagramNode;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;

public class ComposeGMLDiagramNodeFactory implements VertexFactory<ComposeGMLDiagramNode> {

	@SuppressWarnings("unchecked")
	@Override
	public ComposeGMLDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new ComposeGMLDiagramNode();
		} else if(arg0[0] instanceof Collection<?>){
			return new ComposeGMLDiagramNode((Collection<? extends DiagramNode>) arg0[0]);
		}
		
		return null;
	}

}
