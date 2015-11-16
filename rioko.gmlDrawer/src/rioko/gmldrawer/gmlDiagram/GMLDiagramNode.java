package rioko.gmldrawer.gmlDiagram;

import java.util.ArrayList;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rioko.gmldrawer.gmlDiagram.factories.GMLDiagramNodeFactory;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.StringAttribute;

public class GMLDiagramNode extends DiagramNode {
	
	private Element data = null;
	
	public GMLDiagramNode() {
		super();
	}
	
	public GMLDiagramNode(String label)
	{
		super(label);
	}
	
	public GMLDiagramNode(String label, Element data) {
		this(label);
		
		this.data = data;
	}
	
	public GMLDiagramNode(Element data) {
		this.data = data;
		
		this.setLabel(data.getNodeName());
	}

	@Override
	public GMLDiagramNodeFactory getVertexFactory() {
		return new GMLDiagramNodeFactory();
	}

	@Override
	public GMLDiagramNode copy() {
		GMLDiagramNode copy = new GMLDiagramNode(this.data);
		
		copy.setId(this.getId());
		
		copy.dataFigure = null;
		
		return copy;
	}

	@Override
	public AbstractAttribute[] getDrawableData() {
		ArrayList<AbstractAttribute> attrs = new ArrayList<>();
		
		String id = this.data.getAttribute("id");
		if(!id.isEmpty()) {
			attrs.add(new StringAttribute("Id", this.data.getAttribute("id")));
		}
		
		NodeList list = this.data.getElementsByTagName("data");
		for(int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE) {
				Element el = (Element)node;
				attrs.add(new StringAttribute(el.getAttribute("key"), this.proccessData(el)));
			}
		}

		return attrs.toArray(new AbstractAttribute[0]);
	}

	@Override
	protected AbstractAttribute[] getNonDrawableData() {
		return new AbstractAttribute[0];
	}
	
	protected Element getRawData() {
		return this.data;
	}

	//Other methods
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof GMLDiagramNode) {
			return this.data.equals(((GMLDiagramNode) ob).data);
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.data.hashCode();
	}
	
	//Private methods
	private String proccessData(Element el) {
		return el.getTextContent().replaceAll("\n", "").replaceAll(" ", "");
	}
}
