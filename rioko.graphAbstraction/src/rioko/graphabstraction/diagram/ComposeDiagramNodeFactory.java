package rioko.graphabstraction.diagram;

import java.util.Collection;

import rioko.grapht.VertexFactory;

public class ComposeDiagramNodeFactory implements VertexFactory<ComposeDiagramNode> {

	@SuppressWarnings("unchecked")
	@Override
	public ComposeDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new ComposeDiagramNode();
		} else if(arg0[0] instanceof Collection<?>){
			return new ComposeDiagramNode((Collection<? extends DiagramNode>) arg0[0]);
		}
		
		return null;
	}

}
