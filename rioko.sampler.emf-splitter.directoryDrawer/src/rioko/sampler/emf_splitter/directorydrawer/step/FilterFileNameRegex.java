package rioko.sampler.emf_splitter.directorydrawer.step;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.sampler.emf_splitter.directorydrawer.configuration.RegexConfiguration;
import rioko.sampler.emf_splitter.directorydrawer.search.FileNameRegexFilter;

public class FilterFileNameRegex extends FilterNestedBuilder {

	private FileNameRegexFilter search = (this.search == null) ? new FileNameRegexFilter() : this.search;
	
	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		ArrayList<Class<? extends Configuration>> res = new ArrayList<>();
		
		String conf = (String)search.getConfiguration("Regex");
		if(conf == null || conf.equals("")) {
			res.add(RegexConfiguration.class);
		}
		
		return res;
	}

	@Override
	protected FilterOfVertex getFilter(DiagramGraph data, Configurable properties) {
		if(data == null && properties == null && this.search == null) {
			this.search = new FileNameRegexFilter();
		}
		Object conf = (properties != null) ? properties.getConfiguration("Regex") : null;
		if(conf != null) {
			if(conf instanceof String) {
				try {
					search.setConfiguration(new RegexConfiguration((String)conf));
				} catch (BadConfigurationException | BadArgumentException e) {
					// Impossible Exception
					e.printStackTrace();
					return null;
				}
			}
		}
		
		return search;
	}

}
