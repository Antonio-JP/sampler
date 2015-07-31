package rioko.drawmodels.editors.zesteditor.zestproperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.eclipse.registry.RegistryManagement;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.events.ConfigurationChange;
import rioko.utilities.Log;
import rioko.utilities.Pair;

public class ZestAlgorithmConfigurable implements Configurable {
	
	private NestedBuilderAlgorithm algorithm = null;
	
//	private UnsignedIntConfiguration toShow = new UnsignedIntConfiguration();
//	private ModelRootNodeConfiguration rootConf = new ModelRootNodeConfiguration();
//	private UnsignedIntConfiguration toExpand = new UnsignedIntConfiguration(); 
//	private EClassConfiguration toFilter = new EClassConfiguration();
//	private BooleanConfiguration strongCon = new BooleanConfiguration();
//	private UnsignedIntConfiguration clusterNum = new UnsignedIntConfiguration();
	private ArrayList<Configuration> currentConfiguration;
	
	
	public void setAlgorithm(NestedBuilderAlgorithm algorithm) {
		if(this.algorithm == null || !this.algorithm.equals(algorithm)) {
			NestedBuilderAlgorithm prev = this.algorithm;
			this.algorithm = algorithm;
			
			if(algorithm != null) {
				Collection<Class<? extends Configuration>> confNeeded = algorithm.getConfigurationNeeded();
				
				this.currentConfiguration = new ArrayList<>(confNeeded.size());
				
				for(Class<? extends Configuration> option : confNeeded) {
					Configuration newConf = RegistryManagement.getInstanceOf("rioko.graphabstraction.configurations", option);
					if(newConf == null) {
						Log.exception(new Exception("Error creating a " + option.getSimpleName()));
						this.algorithm = prev;
					} else {
						this.currentConfiguration.add(newConf);
					}
				}
			}
		
			new ConfigurationChange(this);
		}
	}
	
	public NestedBuilderAlgorithm getAlgorithm() {
		return this.algorithm;
	}

	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String, Configuration>> res = new ArrayList<>();
		
		if(this.currentConfiguration != null) {	
			for(Configuration option : this.currentConfiguration) {
				res.add(new Pair<>(option.getNameOfConfiguration(), option));
			}
		}
		
		return res;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {

		Iterator<Configuration> iterator = newConf.iterator();
		if(this.checkArgument(newConf)) {
			for(Configuration option : this.currentConfiguration) {
				Configuration current = iterator.next();
				
				option.setConfiguration(current.getConfiguration());
			}
		}
	}
	
	public void setConfiguration(Class<? extends Configuration> option, Object value) throws BadConfigurationException, BadArgumentException {
		if(this.currentConfiguration != null) {
			for(Configuration conf : this.currentConfiguration) {
				if(option.isInstance(conf)) {
					conf.setConfiguration(value);
					return;
				}
			}
		}
		
		throw new BadConfigurationException("That configuration does not exist in the current algorithm");
	}
	
	@Override
	public Object getConfiguration(Class<? extends Configuration> option) {
		if(this.currentConfiguration != null) { 
			for(Configuration conf : this.currentConfiguration) {
				if(option.isInstance(conf)) {
					return conf.getConfiguration();
				}
			}
		}
		
		return null;
	}

	//Private methods
	private Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		ArrayList<Class<? extends Configuration>> res = new ArrayList<>();
		
		if(this.algorithm != null) {
			res.addAll(this.algorithm.getConfigurationNeeded());
			
		}
		
		return res;
	}
	
	private boolean checkArgument(Collection<Configuration> newConf) throws BadArgumentException {
		Iterator<Configuration> iterator = newConf.iterator();
		for(Class<? extends Configuration> option : this.getConfigurationNeeded()) {
			Configuration current = iterator.next();
			if(!option.isInstance(current)) {
				throw new BadArgumentException(option, current.getClass());
			}
		}
		
		return true;
	}

	//Auxiliary methods
	public ZestAlgorithmConfigurable copy() {
		ZestAlgorithmConfigurable res = new ZestAlgorithmConfigurable();
		
		res.setAlgorithm(this.algorithm);
		
		if(this.currentConfiguration != null) {
			for(int i = 0; i < this.currentConfiguration.size(); i++) {
				try {
					res.currentConfiguration.get(i).setConfiguration(this.currentConfiguration.get(i).getConfiguration());
				} catch (BadConfigurationException | BadArgumentException e) {
					// Impossible Exception
					Log.exception(e);
				}
			}
		}
		
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
