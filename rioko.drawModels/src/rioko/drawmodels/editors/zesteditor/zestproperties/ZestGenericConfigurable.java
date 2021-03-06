package rioko.drawmodels.editors.zesteditor.zestproperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.drawmodels.configurations.AggregationAlgorithmConfiguration;
import rioko.drawmodels.configurations.LayoutAlgorithmConfiguration;
import rioko.drawmodels.configurations.ShowAttrConfiguration;
import rioko.drawmodels.configurations.ShowConnConfiguration;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.BooleanConfiguration;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.events.ConfigurationChange;
import rioko.utilities.Pair;

public class ZestGenericConfigurable implements Configurable {
	
	private LayoutAlgorithmConfiguration layoutConf = new LayoutAlgorithmConfiguration();
	
	private AggregationAlgorithmConfiguration aggregConf = new AggregationAlgorithmConfiguration();
	
	private ShowAttrConfiguration showAttrConf = new ShowAttrConfiguration(); 
	private ShowConnConfiguration showConnConf = new ShowConnConfiguration();
	
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String,Configuration>> configuration = new ArrayList<>();
		
		configuration.add(new Pair<>(this.layoutConf.getNameOfConfiguration(), this.layoutConf));
		configuration.add(new Pair<>(this.aggregConf.getNameOfConfiguration(), this.aggregConf));
		configuration.add(new Pair<>(this.showAttrConf.getNameOfConfiguration(), this.showAttrConf));
		configuration.add(new Pair<>(this.showConnConf.getNameOfConfiguration(), this.showConnConf));
		
		return configuration;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		if(newConf.size() != 4) {
			throw new BadConfigurationException("Need exactly 4 configurations");
		} else {
			Iterator<Configuration> iterator = newConf.iterator();
			Configuration first = iterator.next(), second = iterator.next(), third = iterator.next(), fourth = iterator.next();
			
			if(!(first instanceof LayoutAlgorithmConfiguration)) {
				throw new BadArgumentException(LayoutAlgorithmConfiguration.class, first.getClass(), "The first must be a Layout Configuration");
			} else if(!(second instanceof AggregationAlgorithmConfiguration)) {
				throw new BadArgumentException(AggregationAlgorithmConfiguration.class, first.getClass(), "The second must be a Aggregate Configuration");
			} else if(!(third instanceof BooleanConfiguration)) {
				throw new BadArgumentException(BooleanConfiguration.class, first.getClass(), "The third must be a Layout Configuration");
			} else if(!(fourth instanceof BooleanConfiguration)) {
				throw new BadArgumentException(BooleanConfiguration.class, first.getClass(), "The fourth must be a Layout Configuration");
			} 
			
			this.layoutConf.setConfiguration(first.getConfiguration());
			this.aggregConf.setConfiguration(second.getConfiguration());
			this.showAttrConf.setConfiguration(third.getConfiguration());
			this.showConnConf.setConfiguration(fourth.getConfiguration());
		}
	}

	//Control methods
	public boolean addAggregationAlgorithm(NestedBuilderAlgorithm algorithm) {
		return this.aggregConf.add(algorithm);
	}
	
	public void setConfiguration(ZestGenericProperties option, Object value) throws BadConfigurationException, BadArgumentException {
		try {
			switch(option) {
				case AGGREGATION_ALG:
					this.aggregConf.setConfiguration(value);
					break;
				case LAYOUT_ALG:
					this.layoutConf.setConfiguration(value);
					break;
				case SHOW_ATTR:
					this.showAttrConf.setConfiguration(value);
					break;
				case SHOW_CON:
					this.showConnConf.setConfiguration(value);
					break;	
			}
	
			new ConfigurationChange(this);
		} catch(BadConfigurationException | BadArgumentException e) {
			throw e;
		}
	}
	
	public Object getConfiguration(ZestGenericProperties option) {
		switch(option) {
		case AGGREGATION_ALG:
			return this.aggregConf.getConfiguration();
		case LAYOUT_ALG:
			return this.layoutConf.getConfiguration();
		case SHOW_ATTR:
			return this.showAttrConf.getConfiguration();
		case SHOW_CON:
			return this.showConnConf.getConfiguration();
		}
		
		return null;
	}
}
