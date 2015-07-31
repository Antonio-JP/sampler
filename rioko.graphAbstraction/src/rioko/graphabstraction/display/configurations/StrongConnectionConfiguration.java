package rioko.graphabstraction.display.configurations;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.BooleanConfiguration;
import rioko.graphabstraction.configurations.Configuration;
import rioko.utilities.Log;

public class StrongConnectionConfiguration extends BooleanConfiguration {

	public StrongConnectionConfiguration() {
		super();
	}
	
	@Override
	public String getNameOfConfiguration() {
		return "Strong Connection";
	}

	@Override
	public Configuration copy() {
		StrongConnectionConfiguration res = new StrongConnectionConfiguration();
		try {
			res.setValueConfiguration(this.getConfiguration());
		} catch (BadArgumentException | BadConfigurationException e) {
			// Impossible Exception
			Log.exception(e);
		}
		
		return res;
	}
	
}
