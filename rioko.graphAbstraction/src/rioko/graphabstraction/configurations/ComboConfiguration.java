package rioko.graphabstraction.configurations;

import java.util.Collection;

public interface ComboConfiguration extends Configuration {
	public Collection<? extends Object> getPossibleOptions();
	public Collection<String> getPossibleOptionNames();
	
	public Class<?> classOfOptions();
}
