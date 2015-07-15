package rioko.emfdrawer.xmiDiagram;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import rioko.graphabstraction.diagram.AbstractAttribute;

public class XMINullDiagramNode extends XMIDiagramNode {
	public XMINullDiagramNode(String string) {
		super(string);
	}
	
	public XMINullDiagramNode() {
		super("Null");
	}
	
	@Override
	public AbstractAttribute[] getAttributes()
	{
		this.checkEObject();
		
		ArrayList<AbstractAttribute> attrs = new ArrayList<>();
		attrs.add(new StringAttribute("Null", "reference to somewhere"));
		
		return attrs.toArray(new AbstractAttribute[0]);
	} 
	
	@Override
	public IFigure getFigure(boolean showData) {
		return super.getFigure(showData, new Color(null, 185,255,174));
	}
	
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof XMINullDiagramNode) {
			return this == ob;
		}
		
		return false;
	}
}
