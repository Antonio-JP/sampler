package rioko.sampler.emf_splitter.directorydrawer.step;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.sampler.emf_splitter.directorydrawer.search.EMFSplitterSystemFilter;

public class FilterEMFSplitterSystem extends FilterNestedBuilder {

	private FilterOfVertex search = new EMFSplitterSystemFilter();
	
	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		return new ArrayList<>();
	}

	@Override
	protected FilterOfVertex getFilter(DiagramGraph data, Configurable properties) {
		return this.search;
	}

}
