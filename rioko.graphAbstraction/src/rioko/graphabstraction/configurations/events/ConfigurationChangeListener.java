package rioko.graphabstraction.configurations.events;

import org.eclipse.swt.widgets.Event;

import rioko.events.listeners.AbstractDataChangeListener;
import rioko.graphabstraction.configurations.Configurable;

public abstract class ConfigurationChangeListener extends AbstractDataChangeListener {

	public ConfigurationChangeListener(Configurable data, Object parent) throws Exception {
		super(data, parent);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onDataChange(Event event) {
		if(event instanceof ConfigurationChange) {
			this.run((ConfigurationChange)event);
		}
	}
	
	protected abstract void run(ConfigurationChange event);

}
