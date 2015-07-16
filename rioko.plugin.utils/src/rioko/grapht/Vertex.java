package rioko.grapht;

import rioko.utilities.Copiable;

/**
 * Interface that represent a Vertex to add in a Graph
 * 
 * @author Antonio
 */
public interface Vertex extends Copiable {
	/**
	 * Method to get the Facotry to create new Vertices. The parameter of the factory
	 * should be the same class as the vertex implementation.
	 * 
	 * @return {@link rioko.grapht.VertexFactory} that allow create more vertices of the same class.
	 */
	public VertexFactory<?> getVertexFactory();
	
	/**
	 * Method to know if the Vertex is "marked", whatever it could mean in different contexts.
	 * 
	 * @return A boolean value true if the vertex is marked and false otherwise.
	 */
	public boolean getMark();
	/**
	 * Setter method for the marked property of the vertex. If being marked means nothing for a 
	 * particular class of vertices, this methods could do nothing.
	 * 
	 * @param marked Boolean value.
	 */
	public void setMark(boolean marked);
}
