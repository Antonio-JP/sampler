package rioko.emfdrawer.filters;

import java.util.ArrayList;
import java.util.Collection;

import rioko.emfdrawer.configurations.EClassConfiguration;
import rioko.emfdrawer.xmiDiagram.XMIDiagramNode;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.utilities.Pair;

public class ByEClass extends FilterOfVertex {
	
	EClassConfiguration conf = new EClassConfiguration();

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		if(conf == null || conf.getConfiguration() == null) {
			return true;
		}
		
		if(node instanceof XMIDiagramNode) {
			if(conf.getConfiguration().equals(((XMIDiagramNode)node).getEClass())) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public Collection<Pair<String,Configuration>> getConfiguration() {
		Collection<Pair<String,Configuration>> config = new ArrayList<>();
		
		config.add(new Pair<String, Configuration>("EClass",conf));
		
		return config;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException, BadArgumentException {
		if(newConf.size() != 1) {
			throw new BadConfigurationException("This needs exactly 1 configuration");
		}
		
		Configuration configuration = newConf.iterator().next();
		if(!(configuration instanceof EClassConfiguration)) {
			throw new BadArgumentException(EClassConfiguration.class, configuration.getClass());
		}
		
		this.conf = (EClassConfiguration)configuration;
	}

}
