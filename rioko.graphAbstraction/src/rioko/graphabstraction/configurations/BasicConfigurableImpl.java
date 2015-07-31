package rioko.graphabstraction.configurations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import rioko.utilities.Pair;

public class BasicConfigurableImpl implements Configurable {

	HashMap<String, Configuration> configurations = new HashMap<>();
	
	//Builders
	public BasicConfigurableImpl() {}
	
	public BasicConfigurableImpl(Collection<Configuration> configurations) {
		this();
		
		try {
			this.setConfiguration(configurations);
		} catch (BadConfigurationException | BadArgumentException e) {
			// Impossible exception
			e.printStackTrace();
		}
	}
	
	public BasicConfigurableImpl(Configuration configuration) {
		this();
		
		try {
			this.setConfiguration(configuration);
		} catch (BadConfigurationException | BadArgumentException e) {
			// Impossible exception
			e.printStackTrace();
		}
	}
	
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String,Configuration>> res = new ArrayList<>();
		
		for(String key : this.configurations.keySet()) {
			res.add(new Pair<>(key, this.configurations.get(key)));
		}
		
		return res;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf)
			throws BadConfigurationException, BadArgumentException {
		for(Configuration conf : newConf) {
			this.configurations.put(conf.getTextConfiguration(), conf);
		}
	}
	
	public void addConfiguration(String key, Configuration conf) {
		this.configurations.put(key, conf);
	}

}
