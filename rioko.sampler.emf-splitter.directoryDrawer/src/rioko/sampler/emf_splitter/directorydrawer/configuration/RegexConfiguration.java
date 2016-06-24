package rioko.sampler.emf_splitter.directorydrawer.configuration;

import rioko.graphabstraction.configurations.TextConfiguration;

public class RegexConfiguration extends TextConfiguration {

	public RegexConfiguration() {
		super();
	}
	
	public RegexConfiguration(String configuration) {
		super(configuration);
	}

	@Override
	public String getNameOfConfiguration() {
		return "Regex";
	}

	@Override
	public RegexConfiguration copy() {
		return new RegexConfiguration(this.getConfiguration());
	}

}
