package rioko.graphabstraction.diagram;

import org.eclipse.zest.core.viewers.EntityConnectionData;

import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.VertexFactory;
import rioko.utilities.Log;

public class DiagramGraph extends AdjacencyListGraph<DiagramNode, DiagramEdge<DiagramNode>> {

	private Class<? extends ComposeDiagramNode> composeClass = ComposeDiagramNode.class;
	private VertexFactory<? extends ComposeDiagramNode> composeFactory = null;
	
	public DiagramGraph() {
		super(DiagramEdge.class, DiagramNode.class);
	}

	public DiagramGraph(Class<?> edgeClass, Class<?> vertexClass, Class<? extends ComposeDiagramNode> composeClass) {
		super(edgeClass, vertexClass);
		
		this.composeClass = composeClass;
	}
	
	public DiagramGraph(Class<?> edgeClass, Class<?> vertexClass) {
		super(edgeClass, vertexClass);
		
		this.composeClass = ComposeDiagramNode.class;
	}
	
	//Other methods	
	public DiagramEdge<DiagramNode> getEdge(EntityConnectionData element) {
		Object sObj = element.source, tObj = element.dest;
		
		if(((sObj instanceof DiagramNode)) && ((tObj instanceof DiagramNode))) {
			DiagramNode source = (DiagramNode)sObj;
			DiagramNode target = (DiagramNode)tObj;
			
			return this.getEdge(source, target);
		}
		
		return null;
	}
	
	public Class<? extends ComposeDiagramNode> getComposeClass() {
		return this.composeClass;
	}
	
	@SuppressWarnings("unchecked")
	public VertexFactory<? extends ComposeDiagramNode> getComposeVertexFactory() {
		if(this.composeFactory == null) {
			try {
				this.composeFactory = (VertexFactory<? extends ComposeDiagramNode>) this.composeClass.newInstance().getVertexFactory();
			} catch (InstantiationException | IllegalAccessException e) {
				Log.exception(e);
			}
		}
		
		return this.composeFactory;
	}
	
	@Override
	public DiagramGraph getSimilarType() {
		return new DiagramGraph(this.getEdgeClass(), this.getVertexClass(), this.getComposeClass());
	}
}
