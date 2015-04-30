package rioko.graphabstraction.display;

import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;

public interface GraphBuilder {
	/**
	 * Method that creates a new Graph using a DiagramGraph as data witho some configuration
	 * 
	 * @param data Graph to modify
	 * @param properties Configuration of the execution
	 * 
	 * @return A new Graph ready to draw
	 */
	public DiagramGraph createNestedGraph(DiagramGraph data, Configurable properties);
	
	//Generic methods	
	/**
	 * Public method to generate the news vertex of the graph to draw
	 * 
	 * @param target DiagramGraph object where insert the new vertex
	 * @param data DiagramGraph used as data
	 * @param properties DisplayProperties used to configure the algorithm
	 * 
	 * @return DiagramGraph with the printable graph
	 * 
	 * @throws Exception if there is any error
	 */
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception;
	
	/**
	 * Method to check in each algorithm if the configuration with properties is complete
	 * 
	 * @param properties DisplayProperties with the configuration
	 * 
	 * @return true if it is correct and false otherwise
	 */
	boolean checkProperties(Configurable properties);
	
	/**
	 * Method to check if the configuration is complete
	 * 
	 * @param properties DisplayProperties with the configuration
	 * 
	 * @return true if it is correct and false otherwise
	 */
	default boolean checkProperties() {
		return this.getConfigurationNeeded().isEmpty();
	}
	
	/**
	 * Se unen todos los vértices que dentro de sí mismos contengan vértices que en le grafo inicial ya estaban unidos.
	 * 
	 * @param target Grafo donde se van a generar las nuevas aristas
	 * @param source Grafo del que se van extraer las conexiones
	 */
	public void buildEdges(DiagramGraph target, DiagramGraph data);
	
	/**
	 * Methods that returns what configuration is needed by this algortihm
	 * 
	 * @return Collection with the options needed
	 */
	public Collection<DisplayOptions> getConfigurationNeeded();
}
