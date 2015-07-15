package rioko.gmldrawer;

import org.w3c.dom.Node;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;

public class GMLModelDiagram extends ModelDiagram<Node> {
	
	//Builders
	public GMLModelDiagram()
	{
		super();
	}
	
	public GMLModelDiagram(Class<? extends DiagramEdge<DiagramNode>> edgeClass, Class<? extends DiagramNode> vertexClass, Class<? extends ComposeDiagramNode> composeClass)
	{
		super(edgeClass, vertexClass, composeClass);
	}
	
	public GMLModelDiagram(DiagramGraph graph) 
	{
		super(graph);
	}

	@Override
	public void buildMetaData() { }

	@Override
	public Object getMetaData() {
		return null;
	}

}
