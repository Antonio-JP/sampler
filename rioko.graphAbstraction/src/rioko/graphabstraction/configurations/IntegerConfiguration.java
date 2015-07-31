package rioko.graphabstraction.configurations;


public abstract class IntegerConfiguration implements Configuration {

	private int value = 0;
	
	public IntegerConfiguration() {	}
	
	public IntegerConfiguration(int toShow) throws BadConfigurationException {
		this.setValueConfiguration(toShow);
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.NUMBER_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadConfigurationException {
		Integer num = Integer.class.cast(value);
		
		this.value = num.intValue();
	}

	@Override
	public Integer getConfiguration() {
		return this.value;
	}

	@Override
	public String getTextConfiguration() {
		return ""+this.value;
	}
}
