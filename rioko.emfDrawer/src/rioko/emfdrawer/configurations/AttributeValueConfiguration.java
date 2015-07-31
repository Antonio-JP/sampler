package rioko.emfdrawer.configurations;

import rioko.graphabstraction.configurations.TextConfiguration;

public class AttributeValueConfiguration extends TextConfiguration {

	public AttributeValueConfiguration() {
		super();
	}
	
	public AttributeValueConfiguration(String text) {
		super(text);
	}

	@Override
	public String getNameOfConfiguration() {
		return "Attribute Value";
	}

	@Override
	public AttributeValueConfiguration copy() {
		return new AttributeValueConfiguration(this.getConfiguration());
	}

}
