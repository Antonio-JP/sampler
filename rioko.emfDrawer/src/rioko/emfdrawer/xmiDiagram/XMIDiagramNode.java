package rioko.emfdrawer.xmiDiagram;

import java.util.ArrayList;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;

import rioko.emfdrawer.xmiDiagram.factories.XMIDiagramNodeFactory;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.StringAttribute;

public class XMIDiagramNode extends DiagramNode {
	
	private EObject object = null;
	
	//Builders	
	public XMIDiagramNode()
	{
		super();
	}
	
	public XMIDiagramNode(String label)
	{
		super(label);
	}
	
	public XMIDiagramNode(String label, EObject eObject)
	{
		this(label);
		
		this.object = eObject;
	}
	
	public XMIDiagramNode(EObject eObject)
	{
		if(eObject == null) {
			this.error("Null Argument recieved.");
		}
		
		this.setLabel(eObject.eClass().getName());
		this.object = eObject;
	}
	
	//Getters & Setters
	public AbstractAttribute[] getDrawableData()
	{
		this.checkEObject();
		
		EList<EAttribute> eAllAttributes = this.getEClass().getEAllAttributes();
		ArrayList<AbstractAttribute> attrs = new ArrayList<>(eAllAttributes.size());
		for(EAttribute attr : eAllAttributes) {
			attrs.add(new StringAttribute(attr.getName(), this.getStringFromData(this.object.eGet(attr))));
		}
		
		return attrs.toArray(new AbstractAttribute[0]);
	}

	@Override
	protected AbstractAttribute[] getNonDrawableData() {
		AbstractAttribute[] attrs = new AbstractAttribute[2];
		
		attrs[0] = new StringAttribute("ID", ""+this.getId());
		attrs[1] = new StringAttribute("EClass", this.object.eClass().getName());
		
		return attrs;
	}
	
	public EClass getEClass()
	{
		this.checkEObject();
		
		return this.object.eClass();
	}
	
	public void setEObject(EObject eObject) {
		this.object = eObject;
		
		this.setLabel(this.getEClass().getName());
	}
	
	protected EObject getEObject() {
		return this.object;
	}
	
	//Overriden methods
	@Override
	public DiagramNode copy() {
		XMIDiagramNode copy = new XMIDiagramNode(this.object);
		
		copy.setId(this.getId());
		
		copy.dataFigure = null;
		
		return copy;
	}
	
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof XMIDiagramNode) {
			URI here = EcoreUtil.getURI(this.object);
			URI obURI = EcoreUtil.getURI(((XMIDiagramNode) ob).getEObject());
			
			return here.equals(obURI);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		if(this.object == null) {
			return super.hashCode();
		}
		return EcoreUtil.getURI(this.object).hashCode();
	}

	@Override
	public XMIDiagramNodeFactory getVertexFactory() {
		return new XMIDiagramNodeFactory();
	}

	//Private methods
	private void error(String message) {
		throw new RuntimeException(message);
	}
	
	protected void checkEObject() {
		if(this.object == null) {
			this.error("XMI Node not initialized");
		}
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private String getStringFromData(Object data)
	{
		if(data == null)
		{
			return "null";
		}
		
		return data.toString();
	}
}
