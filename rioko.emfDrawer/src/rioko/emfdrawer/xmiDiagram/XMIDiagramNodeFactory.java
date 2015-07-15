package rioko.emfdrawer.xmiDiagram;

import org.eclipse.emf.ecore.EObject;

import rioko.grapht.VertexFactory;

public class XMIDiagramNodeFactory implements VertexFactory<XMIDiagramNode> {

	@Override
	public XMIDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new XMIDiagramNode();
		} else if(arg0.length == 1 && arg0[0] == null) {
			return new XMINullDiagramNode();
		} else if(arg0.length == 1 && (arg0[0] instanceof String)){
			return new XMIDiagramNode((String)arg0[0]);
		} else if(arg0.length == 1 && (arg0[0] instanceof EObject)) {
			if(((EObject)arg0[0]).eIsProxy()) {
				return new XMIProxyDiagramNode((EObject)arg0[0]);
			} else {
				return new XMIDiagramNode((EObject)arg0[0]);
			}
		} else if(arg0.length == 2 && (arg0[0] instanceof String) && (arg0[1] instanceof EObject)) {
			if(((EObject)arg0[0]).eIsProxy()) {
				return new XMIProxyDiagramNode((String)arg0[0], (EObject)arg0[1]);
			} else {
				return new XMIDiagramNode((String)arg0[0], (EObject)arg0[1]);
			}
		} 
		
		return null;
	}

}
