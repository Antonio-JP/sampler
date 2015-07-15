package rioko.emfdrawer.xmiDiagram;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.graphics.Color;

import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.ProxyDiagramNode;

public class XMIProxyDiagramNode extends XMIDiagramNode implements ProxyDiagramNode<EObject> {

	public XMIProxyDiagramNode(String string, EObject eObject) {
		super(string, eObject);
	}

	public XMIProxyDiagramNode(EObject eObject) {
		super(eObject);
	}
	
	@Override
	public AbstractAttribute[] getAttributes()
	{
		this.checkEObject();
		
		ArrayList<AbstractAttribute> attrs = new ArrayList<>();
		attrs.add(new StringAttribute("Is Proxy To", this.getEClass().getName()));
		
		return attrs.toArray(new AbstractAttribute[0]);
	} 
	
	@Override
	public IFigure getFigure(boolean showData) {
		return super.getFigure(showData, new Color(null, 232,159,255));
	}
	
	public final EObject getProxyObject() {
		return this.getEObject();
	}
}
