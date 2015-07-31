package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.BasicConfigurableImpl;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.utilities.Pair;

public abstract class FilterNestedBuilder extends NestedGraphBuilder {

	protected FilterOfVertex filter = this.getFilter(null, null);
	
	//NestedGraphBuilder Implemented methods
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		
		FilterOfVertex filter = this.getFilter(data, properties);
		if(this.filter != null) {
			ArrayList<Configuration> newConf = new ArrayList<>();
			
			boolean notValid = false;
			for(Pair<String, Configuration> pair : this.filter.getConfiguration()) {
				if(pair.getLast().isValid()) {
					newConf.add(pair.getLast());
				} else {
					notValid = true;
				}
			}
			
			if(!notValid) {
				filter.setConfiguration(newConf);
			}
		}
		for(DiagramNode node : data.vertexSet()) {
			if(!filter.filterVertex(properties, data).contains(node)) {
				target.addVertex(node);
			}
		}
		
		return target;
	}	
	
	//Configurable methods
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		if(this.filter == null) {
			return new ArrayList<>();
		} else {
			return this.filter.getConfiguration();
		}
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		if(this.filter != null) {
			this.filter.setConfiguration(newConf);
		} else {
			this.getFilter(null, new BasicConfigurableImpl(newConf));
		}
	}

	//Abstract methods
	protected abstract FilterOfVertex getFilter(DiagramGraph data, Configurable properties);

}
