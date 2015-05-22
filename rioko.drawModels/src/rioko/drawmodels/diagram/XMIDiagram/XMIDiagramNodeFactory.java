package rioko.drawmodels.diagram.XMIDiagram;

import org.eclipse.emf.ecore.EObject;

import rioko.grapht.VertexFactory;

public class XMIDiagramNodeFactory implements VertexFactory<XMIDiagramNode> {

	@Override
	public XMIDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new XMIDiagramNode();
		} else if(arg0.length == 1 && (arg0[0] instanceof String)){
			return new XMIDiagramNode((String)arg0[0]);
		} else if(arg0.length == 1 && (arg0[0] instanceof EObject)) {
			return new XMIDiagramNode((EObject)arg0[0]);
		} else if(arg0.length == 2 && (arg0[0] instanceof String) && (arg0[1] instanceof EObject)) {
			return new XMIDiagramNode((String)arg0[0], (EObject)arg0[1]);
		} 
		
		return null;
	}

}
