package rioko.gmldrawer.gmlDiagram;

import java.util.Collection;
import rioko.gmldrawer.gmlDiagram.factories.ComposeGMLDiagramNodeFactory;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;

public class ComposeGMLDiagramNode extends ComposeDiagramNode {

	public ComposeGMLDiagramNode()
	{
		super();
	}
	
	public ComposeGMLDiagramNode(Collection<? extends DiagramNode> collection)
	{
		super(collection);
	}
	
	@Override
	public VertexFactory<?> getVertexFactory() {
		return new ComposeGMLDiagramNodeFactory();
	}
	
	@Override
	public void addDiagramNode(DiagramNode node)
	{
		if((node instanceof GMLDiagramNode) || (node instanceof ComposeGMLDiagramNode)) {
			this.inNodes.add(node);
		}
	}

	@Override
	public AbstractAttribute[] getDrawableData() {
		return new AbstractAttribute[0];
	}

	@Override
	protected AbstractAttribute[] getNonDrawableData() {
		return new AbstractAttribute[0];
	}

}
