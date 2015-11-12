package rioko.graphabstraction.configurations.events;

import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;
import rioko.graphabstraction.configurations.Configurable;

public abstract class ConfigurationChangeListener extends DataChangeListener {

	public ConfigurationChangeListener(Configurable data, Object parent) throws Exception {
		super(parent, data);
	}
	
	@Override
	public Class<? extends DataChangeEvent> getClassForListener() {
		return ConfigurationChange.class;
	}

}
