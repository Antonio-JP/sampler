package rioko.grapht;

/**
 * Factory interface to create {@link rioko.grapht.Edge}.
 * 
 * @author Antonio
 *
 * @param <V> The class of {@link rioko.grapht.Vertex} that the new edges will join.
 * @param <E> The class of {@link rioko.grapht.Edge} that this factory will create.
 */
public interface EdgeFactory<V extends Vertex, E extends Edge<V>> {
	/**
	 * Method to create a new Edge of the class E&#60;V&#62;.
	 * 
	 * @param source V that will be the source vertex of the edge.
	 * @param target V that will be the source vertex of the edge.
	 * 
	 * @return the new edge.
	 */
	public E createEdge(V source, V target);
}
