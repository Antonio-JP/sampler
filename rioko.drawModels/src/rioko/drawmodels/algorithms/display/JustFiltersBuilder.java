package rioko.drawmodels.algorithms.display;

import java.util.ArrayList;
import java.util.List;

import rioko.revent.REvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.events.ConfigurationChangeListener;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.graphabstraction.display.LocalNestedBuilder;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.utilities.Log;
import rioko.utilities.Pair;

public class JustFiltersBuilder extends NestedBuilderAlgorithm {
	
	private JustFiltersBuilder justMe = this;
	
	private ArrayList<Pair<FilterNestedBuilder, ConfigurationChangeListener>> listOfListeners = new ArrayList<>();
	
	public JustFiltersBuilder() {
		super("Just filters algorithm");
		
		this.searchRoots = false;
	}
	
	@Override
	public boolean addGlobalCriteria(GlobalNestedBuilder globalStep) {
		return false;
	}
	
	@Override
	public boolean addLocalCriteria(LocalNestedBuilder localStep) {
		return false;
	}
	
	@Override
	public boolean addOtherStep(NestedGraphBuilder step, int position) {
		return false;
	}
	
	//Crucial methods
	@Override
	public boolean addFilter(FilterNestedBuilder filter) {
		boolean res = super.addFilter(filter);
		if(res) {
			new DataChangeEvent(this);
			
			//Add the listener to that filter
			if(this.containsFilter(filter) == null) {
				try {
					this.listOfListeners.add(new Pair<>(filter,
							new ConfigurationChangeListener(filter, this) {
								@Override
								public void run(DataChangeEvent event) {
									new DataChangeEvent(justMe);
								}
							}));
				} catch (Exception e) {
					// Impossible Exception
					Log.exception(e);
				}
			}
		}
		
		return res;
	}
	
	@Override
	public boolean removeFilter(FilterNestedBuilder filter) {
		boolean res = super.removeFilter(filter);
		if(res) {
			new DataChangeEvent(this);
			
			//We dispose the listener for that Filter
			Pair<FilterNestedBuilder, ConfigurationChangeListener> pair = this.containsFilter(filter);
			if(pair != null) {
				REvent.removeListener(pair.getLast());
			}
			
			//Finally we delete the pair entrance of the list
			this.listOfListeners.remove(pair);
		}
		
		return res;
	}
	
	@Override
	public void removeAllFilters() {
		super.removeAllFilters();
		new DataChangeEvent(this);
	}
	
	private Pair<FilterNestedBuilder, ConfigurationChangeListener> containsFilter(FilterNestedBuilder filter) {
		for(Pair<FilterNestedBuilder, ConfigurationChangeListener> pair : this.listOfListeners) {
			if(pair.getFirst() == filter) {
				return pair;
			}
		}
		
		return null;
	}

	public List<FilterNestedBuilder> getFiltersList() {
		return this.getFilters();
	}
}
