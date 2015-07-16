package rioko.grapht;

/**
 * Factory interface to create {@link rioko.grapht.Vertex}.
 * 
 * @author Antonio
 *
 * @param <V> The class of {@link rioko.grapht.Vertex} this factory will create.
 */
public interface VertexFactory<V> {
	/**
	 * Method to create a new Vertex of the class V
	 * 
	 * @param args Variable amount of parameters needed to create a Vertex.
	 * 
	 * @return A new V vertex using the parameters given or null if those parameters are incorrect.
	 */
	public V createVertex(Object ... args);
}
