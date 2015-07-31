package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.utilities.Pair;

public class AuxiliaryNestedGraphBuilder extends NestedGraphBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		return data;
	}

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
