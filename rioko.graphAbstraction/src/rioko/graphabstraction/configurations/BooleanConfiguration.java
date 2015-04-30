package rioko.graphabstraction.configurations;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;

public class BooleanConfiguration implements ComboConfiguration {

	private static final String TRUE_STR = "Yes";
	private static final String FALSE_STR = "No";
	
	private boolean value = false;
	
	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.COMBO_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		if(value instanceof Boolean) {
			this.value = (Boolean) value;
		} else if(value instanceof String) {
			if(((String) value).equalsIgnoreCase(TRUE_STR)) {
				this.value = true;
			} else if(((String) value).equalsIgnoreCase(FALSE_STR)) {
				this.value = false;
			} else {
				throw new BadArgumentException(Boolean.class, value.getClass(), "No boolean given");
			}
		} else {
			throw new BadArgumentException(Boolean.class, value.getClass(), "No boolean given");
		}
	}

	@Override
	public Boolean getConfiguration() {
		return this.value;
	}

	@Override
	public String getTextConfiguration() {
		if(value) {
			return TRUE_STR;
		} else {
			return FALSE_STR;
		}
	}

	@Override
	public Collection<? extends Object> getPossibleOptions() {
		ArrayList<Boolean> options = new ArrayList<>();
		
		options.add(true);
		options.add(false);
		
		return options;
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		ArrayList<String> names = new ArrayList<>();
		
		names.add(TRUE_STR);
		names.add(FALSE_STR);
		
		return names;
	}

	@Override
	public Class<?> classOfOptions() {
		return Boolean.class;
	}

	@Override
	public BooleanConfiguration copy() {
		BooleanConfiguration res = new BooleanConfiguration();
		
		res.value = this.value;
		
		return res;
	}

}
