package rioko.sampler.emf_splitter.directorydrawer.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.TextConfiguration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramNode;
import rioko.sampler.emf_splitter.directorydrawer.configuration.RegexConfiguration;
import rioko.utilities.Pair;

public class FileNameRegexFilter extends FilterOfVertex {

	private RegexConfiguration configuration = new RegexConfiguration("");
	
	public FileNameRegexFilter() {}
	
	protected FileNameRegexFilter(String regex) {
		try {
			this.configuration.setValueConfiguration(regex);
		} catch (BadArgumentException | BadConfigurationException e) {
			// Impossible Exception
			e.printStackTrace();
		}
	}
	
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String,Configuration>> conf = new ArrayList<>();
		
		conf.add(new Pair<String, Configuration>(configuration.getNameOfConfiguration(), configuration));
		return conf;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf)
			throws BadConfigurationException, BadArgumentException {
		Iterator<Configuration> iterator = newConf.iterator();
		if(!iterator.hasNext()) {
			throw new BadConfigurationException("A FileNameRegexFilter requieres at least 1 Configuration");
		}
		
		Configuration conf = iterator.next();
		if(!(conf instanceof TextConfiguration)) {
			throw new BadArgumentException(TextConfiguration.class, conf.getClass());
		}
		
		this.configuration.setValueConfiguration(((TextConfiguration)conf).getTextConfiguration());
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		String regex = this.configuration.getConfiguration();
		if(regex == null || !regex.equals("")) {
			if(node instanceof FolderDiagramNode) {
				return ((FolderDiagramNode)node).getResource().getName().matches(regex);
			}
		}
		
		return false;
	}

}
