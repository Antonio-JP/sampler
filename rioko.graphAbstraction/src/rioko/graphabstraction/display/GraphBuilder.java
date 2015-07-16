package rioko.graphabstraction.display;

import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;

public interface GraphBuilder {
	/**
	 * Method that creates a new Graph using a DiagramGraph as data witho some configuration
	 * 
	 * @param data {@link rioko.graphabstraction.diagram.DiagramGraph} used to build a new Graph
	 * @param properties {@link rioko.graphabstraction.configurations.Configurable} of the execution
	 * 
	 * @return A new {@link rioko.graphabstraction.diagram.DiagramGraph}
	 */
	public DiagramGraph createNestedGraph(DiagramGraph data, Configurable properties);
		
	/**
	 * Public method to generate the new vertices of the graph
	 * 
	 * @param data {@link rioko.graphabstraction.diagram.DiagramGraph} used as data
	 * @param properties {@link rioko.graphabstraction.configurations.Configurable} used to configure the algorithm
	 * 
	 * @return {@link rioko.graphabstraction.diagram.DiagramGraph} with the printable nodes
	 * 
	 * @throws Exception if there is any error
	 */
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception;
	
	/**
	 * Method to check if the configuration is correct to build a Graph
	 * 
	 * @param properties {@link rioko.graphabstraction.configurations.Configurable} with the configuration
	 * 
	 * @return true if it is correct and false otherwise
	 */
	boolean checkProperties(Configurable properties);
	
	/**
	 * Method to check if the configuration is correct to build a Graph. It check if the builder needs 
	 * some configuration to work
	 * 
	 * @return true if it is correct and false otherwise
	 */
	default boolean checkProperties() {
		return this.getConfigurationNeeded().isEmpty();
	}
	
	/**
	 * Method to add to a target Graph the edges required to finish the new Graph created by this builder 
	 * with {@link rioko.graphabstraction.display.GraphBuilder#buildNodes(DiagramGraph, Configurable)}
	 * 
	 * @param target {@link rioko.graphabstraction.diagram.DiagramGraph DiagramGraph} where we will add the new edges
	 * @param data {@link rioko.graphabstraction.diagram.DiagramGraph DiagramGraph} from we will take information to 
	 * create the edges.
	 */
	public void buildEdges(DiagramGraph target, DiagramGraph data);
	
	/**
	 * Method that returns what configuration is needed by this builder
	 * 
	 * @return Collection with the options needed
	 */
	public Collection<DisplayOptions> getConfigurationNeeded();
}
