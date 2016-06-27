package rioko.sampler.emf_splitter.directorydrawer.step;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.utilities.Pair;

public class AllDirectoryEMFSplitterSteps extends NestedGraphBuilder {

	private FilterEMFSplitterRepeated repeated = new FilterEMFSplitterRepeated();
	private FilterEMFSplitterSystem system = new FilterEMFSplitterSystem();
	
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		return system.buildNodes(repeated.buildNodes(data, properties), properties);
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
	public void setNewConfiguration(Collection<Configuration> newConf)
			throws BadConfigurationException, BadArgumentException {
		return;
	}

}
