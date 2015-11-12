package rioko.graphabstraction.configurations.events;

import rioko.revent.datachange.DataChangeEvent;
import rioko.graphabstraction.configurations.Configurable;

public class ConfigurationChange extends DataChangeEvent {

	public ConfigurationChange(Configurable data) {
		super(data);
	}

}
