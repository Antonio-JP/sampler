package rioko.graphabstraction.display.configurations;

import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.UnsignedIntConfiguration;
import rioko.utilities.Log;

public class MaxNodesConfiguration extends UnsignedIntConfiguration {
	
	public MaxNodesConfiguration() {
		super();
	}
	
	public MaxNodesConfiguration(int toShow) throws BadConfigurationException {
		super(toShow);
	}

	@Override
	public String getNameOfConfiguration() {
		return "Nodes to Show";
	}

	@Override
	public MaxNodesConfiguration copy() {
		try {
			return new MaxNodesConfiguration(this.getConfiguration());
		} catch (BadConfigurationException e) {
			// Impossible Exception
			Log.exception(e);
			return null;
		}
	}
	
}