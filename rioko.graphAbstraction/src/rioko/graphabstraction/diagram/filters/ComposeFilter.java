package rioko.graphabstraction.diagram.filters;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configuration;
import rioko.utilities.Pair;

public abstract class ComposeFilter extends FilterOfVertex {
	
	protected ArrayList<FilterOfVertex> filters = new ArrayList<>();

	public ComposeFilter() {
		super();
	}
	
	public ComposeFilter(Collection<? extends FilterOfVertex> collection) {
		super();
		
		this.filters = new ArrayList<>(collection);
	}
	
	//Control methods
	public boolean addFilter(FilterOfVertex filter) {
		return this.filters.add(filter);
	}
	
	public boolean removeFilter(FilterOfVertex filter) {
		return this.filters.remove(filter);
	}
	
	public boolean addAllFilters(Collection<FilterOfVertex> collection) {
		return this.filters.addAll(collection);
	}
	
	public boolean removeAllFilters(Collection<FilterOfVertex> collection) {
		return this.filters.removeAll(collection);
	}
	
	public void removeAll() {
		this.filters.clear();
	}
	
	//Configurable Methods
	@Override
	@Deprecated
	public Collection<Pair<String,Configuration>> getConfiguration() {
		Collection<Pair<String,Configuration>> collection = new ArrayList<Pair<String,Configuration>>();
		
		for(FilterOfVertex filter : this.filters) {
			collection.addAll(filter.getConfiguration());
		}
		
		return collection;
	}

	@Override
	@Deprecated
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException, BadArgumentException {
		for(FilterOfVertex filter : this.filters) {
			filter.setConfiguration(newConf);
		}
	}
}
