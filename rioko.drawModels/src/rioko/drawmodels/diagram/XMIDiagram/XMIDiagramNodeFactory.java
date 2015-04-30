package rioko.drawmodels.diagram.XMIDiagram;

import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import rioko.grapht.VertexFactory;

public class XMIDiagramNodeFactory implements VertexFactory<XMIDiagramNode> {

	@SuppressWarnings("unchecked")
	@Override
	public XMIDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new XMIDiagramNode();
		} else if(arg0.length == 1 && (arg0[0] instanceof String)){
			return new XMIDiagramNode((String)arg0[0]);
		} else if(arg0.length == 2 && (arg0[0] instanceof String) && (arg0[1] instanceof Collection<?>)) {
			return new XMIDiagramNode((String)arg0[0], (Collection<AbstractAttribute>)arg0[1]);
		} else if(arg0.length == 2 && (arg0[0] instanceof EClass) && (arg0[1] instanceof Collection<?>)) {
			return new XMIDiagramNode((EClass)arg0[0], (Collection<AbstractAttribute>)arg0[1]);
		}
		
		return null;
	}

}
