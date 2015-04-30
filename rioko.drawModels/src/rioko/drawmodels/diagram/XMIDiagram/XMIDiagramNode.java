package rioko.drawmodels.diagram.XMIDiagram;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.emf.ecore.EClass;

import rioko.draw2d.figures.VerticalFigure;
import rioko.graphabstraction.diagram.DiagramNode;

public class XMIDiagramNode extends DiagramNode {
	
	private ArrayList<AbstractAttribute> attrs;
	
	private EClass classNode;
	
	//Builders	
	public XMIDiagramNode()
	{
		super();
		this.attrs = new ArrayList<>();
	}
	
	public XMIDiagramNode(String label)
	{
		super(label);
		
		this.attrs = new ArrayList<>();
	}
	
	public XMIDiagramNode(String label, Collection<AbstractAttribute> attrs)
	{
		this(label);
		
		for(AbstractAttribute attr : attrs) {
			this.attrs.add(attr.copy());
		}
	}
	
	public XMIDiagramNode(EClass type, Collection<AbstractAttribute> attrs)
	{
		this(type.getName(), attrs);
		
		this.classNode = type;
	}
	
	//Getters & Setters
	public AbstractAttribute[] getAttributes()
	{
		return this.attrs.toArray(new AbstractAttribute[0]);
	}
	
	public void addAttribute(AbstractAttribute attr)
	{
		this.attrs.add(attr);
	}
	
	public EClass getEClass()
	{
		return this.classNode;
	}
	
	public void setEClass(EClass classNode)
	{
		this.classNode = classNode;
	}
	
	//Overriden methods
	@Override
	public IFigure buildDataFigure() 
	{
		this.dataFigure = new VerticalFigure();
			
		for(AbstractAttribute attr : this.attrs)
		{
			this.dataFigure.add(attr.getFigure());
		}
		
		return this.dataFigure;
	}

	@Override
	public DiagramNode copy() {
		XMIDiagramNode copy = new XMIDiagramNode(this.getLabel(), this.attrs);
		
		copy.setId(this.getId());
		
		copy.setEClass(this.getEClass());
		
		copy.dataFigure = null;
		
		return copy;
	}

	@Override
	public XMIDiagramNodeFactory getVertexFactory() {
		return new XMIDiagramNodeFactory();
	}

}
