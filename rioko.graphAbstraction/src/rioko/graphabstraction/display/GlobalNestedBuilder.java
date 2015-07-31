package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configuration;
import rioko.utilities.Pair;

public abstract class GlobalNestedBuilder extends NestedGraphBuilder {

	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		return new ArrayList<>();
	} 
	
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		return new ArrayList<>();
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		/* Do nothing */
	}
}
