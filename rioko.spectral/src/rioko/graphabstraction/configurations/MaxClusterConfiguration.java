package rioko.graphabstraction.configurations;

import rioko.utilities.Log;

public class MaxClusterConfiguration extends UnsignedIntConfiguration {

	@Override
	public String getNameOfConfiguration() {
		return "Clusters required";
	}

	@Override
	public Configuration copy() {
		MaxClusterConfiguration res = new MaxClusterConfiguration();
		
		try {
			res.setConfiguration(this.getConfiguration());
		} catch (BadConfigurationException | BadArgumentException e) {
			// Impossible Exception
			Log.exception(e);
		}
		
		return res;
	}

}
