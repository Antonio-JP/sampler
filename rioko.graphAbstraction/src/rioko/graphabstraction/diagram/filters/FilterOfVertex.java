package rioko.graphabstraction.diagram.filters;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Pair;

public abstract class FilterOfVertex implements Configurable{
	private DiagramGraph graph = null;
	private HashSet<DiagramNode> verticesFiltered = null;
	private boolean cache = false;
	
	public FilterOfVertex() { }
	
	private Set<DiagramNode> filterVertex(Configurable properties) {
		if(this.verticesFiltered == null) {
			this.verticesFiltered = new HashSet<>();
			
			for(DiagramNode vertex : this.graph.vertexSet()) {
				if(this.filter(vertex, properties, this.graph)) {
					this.verticesFiltered.add(vertex);
				}
			}
		}
		
		return this.verticesFiltered;
	}
	
	public Set<DiagramNode> filterVertex(Configurable properties, DiagramGraph graph) {
		if((!this.cache)|| (this.graph != graph && graph!= null)) {
			this.graph = graph;
			this.verticesFiltered = null;
		}
		
		return this.filterVertex(properties);
	}
	
	protected abstract boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph);
	
	protected void setCache(boolean cache) {
		this.cache = cache;
	}
	
	//Static methods
	public static FilterOfVertex getOppositeFilter(FilterOfVertex filterOfVertex) {
		return new FilterOfVertex() {

			private FilterOfVertex original = filterOfVertex;
			
			@Override
			protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
				return !(this.original.filter(node, properties, graph));
			}

			@Override
			public Collection<Pair<String,Configuration>> getConfiguration() {
				return this.original.getConfiguration();
			}

			@Override
			public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException, BadArgumentException {
				this.original.setConfiguration(newConf);
			}
			
		};
	}
}
