package rioko.grapht;

import org.jgrapht.EdgeFactory;
import org.jgrapht.graph.DefaultEdge;

import rioko.utilities.Typable;

public class VisibleEdge<V> extends DefaultEdge implements Typable {
	
	private static final long serialVersionUID = -8166341806841897284L;
	
	private V source;
	private V target;
	
	public VisibleEdge() {}
	
	public VisibleEdge(V source, V target) {
		this.source = source;
		this.target = target;
	}

	@Override
	public V getSource()
	{
		return this.source;
	}
	
	@Override
	public V getTarget()
	{
		return this.target;
	}
	
	//Other methods
	public EdgeFactory<V, ? extends VisibleEdge<V>> getEdgeFactory()
	{
		return new VisibleEdgeFactory<>();
	}

	@Override
	public void setType(Object obj) {}

	@Override
	public Object getType() {
		return null;
	}
}
