package rioko.grapht;

/**
 * Abstract edge implementing the basic methods of the interface Edge. It allow different 
 * Vertex using the parameter V.
 * 
 * @author Antonio
 *
 * @param <V> The class of {@link rioko.grapht.Vertex Vertex} that join the AbstractEdge.
 */
public abstract class AbstractEdge<V extends Vertex> implements Edge<V> {

	private V source = null, target = null;
	
	public AbstractEdge() {}
	
	public AbstractEdge(V source, V target) {
		this.source = source;
		this.target = target;
	}
	
	@Override
	public void setType(Object obj) {}

	@Override
	public Object getType() {
		return null;
	}

	@Override
	public V getSource() {
		return this.source;
	}

	@Override
	public V getTarget() {
		return this.target;
	}
}
