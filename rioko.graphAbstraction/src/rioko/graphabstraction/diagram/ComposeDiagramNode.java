package rioko.graphabstraction.diagram;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

import rioko.grapht.VertexFactory;

public class ComposeDiagramNode extends DiagramNode {
	
	protected ArrayList<DiagramNode> inNodes = new ArrayList<>();
	
	public ComposeDiagramNode()
	{
		super();
		this.inNodes = new ArrayList<>();
	}
	
	public ComposeDiagramNode(Collection<? extends DiagramNode> collection) {
		super();
		
		for(DiagramNode node : collection) {
			this.addDiagramNode(node);
		}
	}
	
	public void addDiagramNode(DiagramNode node)
	{
		this.inNodes.add(node);
	}
	
	@Override
	public IFigure getFigure(boolean showData)
	{
		if(this.inNodes.size() == 1) {
			return this.inNodes.get(0).getFigure(showData);
		} else {
			return super.getFigure(showData, new Color(null, 228,211,186));
		}
	}

	@Override
	public IFigure buildDataFigure() {
		if(this.inNodes.size() == 1) {
			return this.inNodes.get(0).buildDataFigure();
		} else {
			return new Label("Contiene "+this.inNodes.size()+" nodos");
		}
	}
	
	public boolean isCompose()
	{
		return this.inNodes.size() != 1;
	}
	
	public boolean isEmpty()
	{
		return this.inNodes.isEmpty();
	}

	public int size() {
		return this.inNodes.size();
	}
	
	@Override
	public VertexFactory<?> getVertexFactory() {
		return new ComposeDiagramNodeFactory();
	}
	
	@Override 
	public DiagramNode getRootNode()
	{
		if(!this.inNodes.isEmpty()) {
			return this.inNodes.get(0);
		}
		
		return null;
	}

	@Override
	public boolean equals(Object ob)
	{
		if(ob instanceof ComposeDiagramNode) {
			ComposeDiagramNode cmpNode = (ComposeDiagramNode)ob;
			
			if(cmpNode.inNodes.size() != this.inNodes.size()) {
				return false;
			}
			
			for(DiagramNode node : this.inNodes) {
				if(!cmpNode.containsProperNode(node)) {
					return false;
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		if(this.inNodes.size()==0) {
			return this.inNodes.hashCode();
		}
		
		int res = 0;
		
		for(DiagramNode node : this.inNodes) {
			res += node.hashCode();
		}
		
		return res/this.inNodes.size();
	}

	@Override
	public DiagramNode copy() {
		ComposeDiagramNode copy = null;
		
		try {
			copy = this.getClass().newInstance();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for(DiagramNode node : this.inNodes) {
			copy.addDiagramNode(node.copy());
		}
		
		copy.setLabel(this.getLabel());
		
		return copy;
	}

	public ArrayList<DiagramNode> getNodes() {
		return this.inNodes;
	}
	
	@Override
	public ArrayList<DiagramNode> getFullListOfNodes() {
		ArrayList<DiagramNode> list = new ArrayList<>();
		
		for(DiagramNode node : this.getNodes()) {
			if(node instanceof ComposeDiagramNode) {
				list.addAll(((ComposeDiagramNode)node).getNodes());
			} else {
				list.add(node);
			}
		}
		
		return list;
	}
	
	public boolean containsProperNode(DiagramNode node) {
		return this.inNodes.contains(node);
	}
	
	public boolean containsInnerNode(DiagramNode node) {
		return this.getFullListOfNodes().contains(node);
	}
}
