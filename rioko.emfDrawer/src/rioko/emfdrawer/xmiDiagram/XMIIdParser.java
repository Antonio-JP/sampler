package rioko.emfdrawer.xmiDiagram;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import rioko.drawmodels.diagram.IdParser;
import rioko.graphabstraction.diagram.DiagramNode;

public class XMIIdParser implements IdParser {
	
	@Override
	public Object getFromString(String str) throws IllegalArgumentException {
		return str;
	}

	@Override
	public Object getKey(DiagramNode node) {
		if(node instanceof XMIDiagramNode) {
			XMIDiagramNode xmiNode = (XMIDiagramNode) node;
			
			EObject eObject = xmiNode.getEObject();
			
			return EcoreUtil.getURI(eObject);
		}
		
		return null;
	}
}
