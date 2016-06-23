package rioko.sampler.directoryDrawer.diagram.factory;

import java.util.Collection;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;
import rioko.sampler.directoryDrawer.diagram.ComposeFolderDiagramNode;

public class ComposeFolderDiagramNodeFactory implements VertexFactory<ComposeFolderDiagramNode> {

	@SuppressWarnings("unchecked")
	@Override
	public ComposeFolderDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new ComposeFolderDiagramNode();
		} else if(arg0[0] instanceof Collection<?>){
			return new ComposeFolderDiagramNode((Collection<? extends DiagramNode>) arg0[0]);
		}
		
		return null;
	}

}
