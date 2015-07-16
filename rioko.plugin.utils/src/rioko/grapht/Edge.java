package rioko.grapht;

import rioko.grapht.EdgeFactory;

import rioko.utilities.Typable;

/**
 * Interface that gives the basic methods to manage a edge of a graph. This interface represent 
 * "directed" edges.
 * 
 * @author Antonio
 *
 * @param <V> The class of {@link rioko.grapht.Vertex} that join the Edge.
 */
public interface Edge<V extends Vertex> extends Typable {
	
	//Basic Edge Methods
	/**
	 * Getter method to know the source of the edge.
	 * 
	 * @return a {@link rioko.grapht.Vertex} of the class V from the edge go.
	 */
	public V getSource();
	/**
	 * Getter method to know the target of the edge.
	 * 
	 * @return a {@link rioko.grapht.Vertex} of the class V where the edge go.
	 */
	public V getTarget();

	//Factory method
	/**
	 * Method to get the Factory class to create new Edges of the same class.
	 * 
	 * @return a {@link rioko.grapht.EdgeFactory} which create edges of the same class of this edge.
	 */
	public EdgeFactory<V, ? extends Edge<V>> getEdgeFactory();
}
