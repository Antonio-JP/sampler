package rioko.drawmodels.editors.zesteditor;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.zest.core.viewers.IGraphEntityContentProvider;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.ModelDiagram;

public class ZestEditorContentProvider extends ArrayContentProvider  implements IGraphEntityContentProvider {
	
	ModelDiagram<?> model;
	
	public ZestEditorContentProvider(ModelDiagram<?> model) {
		this.model = model;
	}
	
	@Override
	public Object[] getConnectedTo(Object entity) {
		if (entity instanceof DiagramNode) {
			DiagramNode node = (DiagramNode) entity;

	    	return this.model.getPrintDiagram().vertexFrom(node).toArray();
	    }
	  	throw new RuntimeException("Type not supported");
	}
}
