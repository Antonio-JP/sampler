package rioko.graphabstraction.configurations;

public abstract class TextConfiguration implements Configuration {
	
	private String value;
	
	public TextConfiguration() {}
	
	public TextConfiguration(String value) {
		try {
			this.setValueConfiguration(value);
		} catch (BadArgumentException | BadConfigurationException e) {
			// Impossible exception
			e.printStackTrace();
		}
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.TEXT_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		this.value = (String)value;
	}

	@Override
	public String getConfiguration() {
		return value;
	}

	@Override
	public String getTextConfiguration() {
		return this.getConfiguration();
	}

}
