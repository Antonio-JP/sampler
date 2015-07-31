package rioko.graphabstraction.diagram.filters;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Pair;

public class BasicFilterOfVertex extends FilterOfVertex {

	private boolean passAll = false;
	
	public BasicFilterOfVertex() { super(); }
	
	public BasicFilterOfVertex(boolean passAll) {
		this.passAll = passAll;
	}

	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		return new ArrayList<Pair<String, Configuration>>();
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException, BadArgumentException { }

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		return this.passAll;
	}

}
