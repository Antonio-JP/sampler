package rioko.emfdrawer.configurations;

import rioko.graphabstraction.configurations.TextConfiguration;

public class AttributeNameConfiguration extends TextConfiguration {
	
	public AttributeNameConfiguration() {
		super();
	}
	
	public AttributeNameConfiguration(String text) {
		super(text);
	}

	@Override
	public String getNameOfConfiguration() {
		return "Attribute Name";
	}

	@Override
	public AttributeNameConfiguration copy() {
		return new AttributeNameConfiguration(this.getConfiguration());
	}

}
