package rioko.drawmodels.configurations;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.BooleanConfiguration;
import rioko.utilities.Log;

public class ShowAttrConfiguration extends BooleanConfiguration {

	@Override
	public String getNameOfConfiguration() {
		return "Show attributes";
	}

	@Override
	public ShowAttrConfiguration copy() {
		ShowAttrConfiguration res = new ShowAttrConfiguration();
		
		try {
			res.setValueConfiguration(this.getConfiguration());
		} catch (BadArgumentException | BadConfigurationException e) {
			// Impossible Exception
			Log.exception(e);
		}
		
		return res;
	}

}
