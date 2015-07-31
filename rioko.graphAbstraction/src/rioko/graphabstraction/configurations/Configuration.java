package rioko.graphabstraction.configurations;

import rioko.utilities.Copiable;

public interface Configuration extends Copiable{
	public TypeOfConfiguration getType();
	
	public default void setConfiguration(Object value) throws BadConfigurationException, BadArgumentException {
		if(value == null) {
			this.setValueConfiguration(null);
			
			return;
		}
		
		TypeOfConfiguration type = this.getType();
		if(type == null) {
			throw new BadConfigurationException("No type assigned");
		}
		
		if(type.getClassNeeded().isInstance(value)) {
			this.setValueConfiguration(type.getClassNeeded().cast(value));
		} else {
			throw new BadArgumentException(type.getClassNeeded(), value.getClass());
		}
	}

	@Deprecated
	void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException;
	
	public Object getConfiguration();
	
	public String getTextConfiguration();
	
	public String getNameOfConfiguration();
	
	public default boolean isValid() {
		return true;
	}
	
	@Override
	public Configuration copy();
}
