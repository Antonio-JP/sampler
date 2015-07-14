package rioko.graphabstraction.diagram;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import rioko.zest.Drawable;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.grapht.Vertex;

public abstract class DiagramNode implements Vertex, Drawable {
		
	//Class atributes
	private int id = -1;
	
	private String label = "";

	protected IFigure dataFigure;
	
	private IFigure figure = null;

	private boolean beRoot = false;
	
	private boolean marked = false;
	
	//Builders
	public DiagramNode() { }
	
	public DiagramNode(String label)
	{
		this();
		
		this.label = label;
	}
	
	//Getters y Setters

	public void setId(int id) {
		if(this.id == -1) {
			this.id = id;
		}
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	//Vertex methods
	@Override
	public boolean getMark() {
		return this.marked;
	}

	@Override
	public void setMark(boolean marked) {
		this.marked = marked;
	}
	
	//Special methods
	public String getTitle()
	{
		if(this.getId() != -1) {
			return this.getId()+": "+this.getLabel();
		} else {
			return "- : "+this.getLabel();
		}
	}
	
	public void clearFigure()
	{
		this.figure = null;
	}
	
	public IFigure getFigure(boolean showData, Color passiveColor) 
	{
		if(this.figure == null) {
			this.figure = new ModelNodeFigure(this, showData, passiveColor);
		}
		return this.figure;
	}
	
	public IFigure getFigure(boolean showData) 
	{
		if(this.figure == null) {
			this.figure = new ModelNodeFigure(this, showData);
		}
		return this.figure;
	}
	
	public IFigure getFigure()
	{
		return this.getFigure(true);
	}
	
	public IFigure getDataFigure()
	{
		if(this.dataFigure == null)
		{
			this.dataFigure = this.buildDataFigure();
		}
		
		return this.dataFigure;
	}
	
	public DiagramNode getRootNode()
	{
		return this;
	}
	
	public ArrayList<DiagramNode> getNodes() {
		return this.getFullListOfNodes();
	}
	
	public ArrayList<DiagramNode> getFullListOfNodes() {
		ArrayList<DiagramNode> list = new ArrayList<>();
		
		list.add(this);
		return list;
	}
	
	public boolean isRoot() {
		return this.beRoot;
	}
	
	public void setBeRoot(boolean beRoot) {
		if(this.beRoot != beRoot) {
			this.clearFigure();
			this.beRoot = beRoot;
		}
	}
	
	@Override
	public abstract DiagramNode copy();
	
	//Overriden methods
	@Override
	public boolean equals(Object ob)
	{
		if(ob instanceof DiagramNode) {
			DiagramNode node = (DiagramNode)ob;
			return this.getId() == node.getId();
		}
		
		return false;
	}
	
	public boolean areRelated(Object ob) {
		if(ob instanceof DiagramNode) {
			DiagramNode node = (DiagramNode)ob;
			if ((this instanceof ComposeDiagramNode) && (ob instanceof ComposeDiagramNode)) {
				ComposeDiagramNode thsCmNode = (ComposeDiagramNode)this;
				ComposeDiagramNode cmNode = (ComposeDiagramNode)node;
				
				ComposeDiagramNode bigger;
				ComposeDiagramNode smaller;
				
				if(thsCmNode.getFullListOfNodes().size() > cmNode.getFullListOfNodes().size()) {
					bigger = thsCmNode;
					smaller = cmNode;
				} else {
					bigger = cmNode;
					smaller = thsCmNode;
				}
				
				for(DiagramNode nd : bigger.getFullListOfNodes()) {
					if(!smaller.containsInnerNode(nd))
					{
						return false;
					}
				}
				
				return true;
			} else if (!(this instanceof ComposeDiagramNode) && (node instanceof ComposeDiagramNode)) {
				return ((ComposeDiagramNode)node).containsInnerNode(this);
			} else if ((this instanceof ComposeDiagramNode) && !(node instanceof ComposeDiagramNode)) {
				return ((ComposeDiagramNode)this).containsInnerNode(node);
			} else if (!(this instanceof ComposeDiagramNode) && !(node instanceof ComposeDiagramNode)){
				return this.equals(node);
			}
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	@Override
	public String toString()
	{
		return this.getClass().getName()+"/-/"+this.getTitle();
	}
	
	public static Class<? extends ComposeDiagramNode> getComposeClass() {
		return ComposeDiagramNode.class;
	}
	//Private methods
	protected abstract IFigure buildDataFigure();
	
	public abstract AbstractAttribute[] getAttributes();
}
