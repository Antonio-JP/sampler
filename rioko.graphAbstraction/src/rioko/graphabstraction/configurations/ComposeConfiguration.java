package rioko.graphabstraction.configurations;

import java.util.Collection;
import java.util.HashMap;

public abstract class ComposeConfiguration implements Configuration {
	
	HashMap<Class<? extends Configuration>, Configuration> configurations = new HashMap<>();

	public ComposeConfiguration() { }
	
	public ComposeConfiguration(Collection<Class<? extends Configuration>> configurations) {
		for(Class<? extends Configuration> confClass : configurations) {
			this.configurations.put(confClass, null);
		}
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.COMPOSE_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		
	}

	@Override
	public Object getConfiguration() {
		return configurations;
	}

	@Override
	public String getTextConfiguration() {
		HashMap<String, String> map = new HashMap<>();
		
		for(Class<? extends Configuration> confClass : this.configurations.keySet()) {
			map.put(confClass.getSimpleName(), this.configurations.get(confClass).getTextConfiguration());
		}
		
		return map.toString();
	}

}
