package rioko.drawmodels.editors.zesteditor.zestproperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.drawmodels.configurations.EClassConfiguration;
import rioko.drawmodels.configurations.ModelRootNodeConfiguration;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.UnsignedIntConfiguration;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.utilities.Pair;

public class ZestAlgorithmConfigurable implements Configurable {
	
	private NestedBuilderAlgorithm algorithm = null;
	
	private UnsignedIntConfiguration toShow = new UnsignedIntConfiguration();
	private ModelRootNodeConfiguration rootConf = new ModelRootNodeConfiguration();
	private UnsignedIntConfiguration toExpand = new UnsignedIntConfiguration(); 
	private EClassConfiguration toFilter = new EClassConfiguration();
	
	
	public void setAlgorithm(NestedBuilderAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	public NestedBuilderAlgorithm getAlgorithm() {
		return this.algorithm;
	}

	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String, Configuration>> res = new ArrayList<>();
		
		
		for(DisplayOptions option : this.getConfigurationNeeded()) {
			res.add(new Pair<>(option.toString(), this.selectConfiguration(option)));
		}
		
		return res;
	}

	@Override
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {

		Iterator<Configuration> iterator = newConf.iterator();
		if(this.checkArgument(newConf)) {
			for(DisplayOptions option : this.getConfigurationNeeded()) {
				Configuration current = iterator.next();
				
				this.setConfiguration(option, current.getConfiguration());
			}
		}
	}
	
	public void setConfiguration(DisplayOptions option, Object value) throws BadConfigurationException, BadArgumentException {
		switch(option) {
			case ECLASS_FILTER:
				this.toFilter.setConfiguration(value);
				break;
			case LEVELS_TS:
				this.toExpand.setConfiguration(value);
				break;
			case MAX_NODES:
				this.toShow.setConfiguration(value);
				break;
			case ROOT_NODE:
				this.rootConf.setConfiguration(value);
				break;
			default:
				break;
		}
	}
	
	public Object getConfiguration(DisplayOptions option) {
		switch(option) {
			case ECLASS_FILTER:
				return this.toFilter.getConfiguration();
			case LEVELS_TS:
				return this.toExpand.getConfiguration();
			case MAX_NODES:
				return this.toShow.getConfiguration();
			case ROOT_NODE:
				return this.toFilter.getConfiguration();
			default:
				break;
		}
		
		return null;
	}

	//Private methods
	private Configuration selectConfiguration(DisplayOptions option) {
		switch(option) {
			case ECLASS_FILTER:
				return this.toFilter;
			case LEVELS_TS:
				return this.toExpand;
			case MAX_NODES:
				return this.toShow;
			case ROOT_NODE:
				return this.rootConf;
			default:
				return null;
		}
	}
	
	private Collection<DisplayOptions> getConfigurationNeeded() {
		ArrayList<DisplayOptions> res = new ArrayList<>();
		
		if(this.algorithm == null) {
			for(DisplayOptions option : DisplayOptions.values()) {
				res.add(option);
			}
		} else {
			for(DisplayOptions option: this.algorithm.getConfigurationNeeded()) {
				res.add(option);
			}
		}
		
		return res;
	}
	
	private boolean checkArgument(Collection<Configuration> newConf) throws BadArgumentException {
		Iterator<Configuration> iterator = newConf.iterator();
		for(DisplayOptions option : this.getConfigurationNeeded()) {
			Configuration current = iterator.next();
			
			switch(option) {
				case ECLASS_FILTER:
					if(!(current instanceof EClassConfiguration)){
						throw new BadArgumentException(EClassConfiguration.class, current.getClass());
					}
					break;
				case LEVELS_TS:
					if(!(current instanceof UnsignedIntConfiguration)){
						throw new BadArgumentException(UnsignedIntConfiguration.class, current.getClass());
					}
					break;
				case MAX_NODES:
					if(!(current instanceof UnsignedIntConfiguration)){
						throw new BadArgumentException(UnsignedIntConfiguration.class, current.getClass());
					}
					break;
				case ROOT_NODE:
					if(!(current instanceof RootNodeConfiguration)){
						throw new BadArgumentException(RootNodeConfiguration.class, current.getClass());
					}
					break;
				default:
					break;
			}
		}
		
		return true;
	}

	//Auxiliary methods
	public ZestAlgorithmConfigurable copy() {
		ZestAlgorithmConfigurable res = new ZestAlgorithmConfigurable();

		res.rootConf = this.rootConf.copy();
		res.toExpand = this.toExpand.copy();
		res.toFilter = this.toFilter.copy();
		res.toShow = this.toShow.copy();
		
		res.setAlgorithm(this.algorithm);
				
		return res;
	}

	public void put(ZestAlgorithmConfigurable last) {
		ArrayList<Configuration> conf = new ArrayList<>();
		
		for(Pair<String, Configuration> pair : last.getConfiguration()) {
			conf.add(pair.getLast());
		}
		
		this.setAlgorithm(last.algorithm);
		try {
			this.setConfiguration(conf);
		} catch (BadConfigurationException | BadArgumentException e) {
			// impossible Exception
			e.printStackTrace();
		}
	}
}
