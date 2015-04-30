package rioko.graphabstraction.configurations;

import java.util.Collection;

public enum TypeOfConfiguration {
	TEXT_CONFIGURATION, COMBO_CONFIGURATION, NUMBER_CONFIGURATION, COMPOSE_CONFIGURATION, DIALOG_CONFIGURATION;
	
	private static final String STR_TEXT_CONFIGURATION = "Text configuration";
	
	private static final String STR_COMBO_CONFIGURATION = "Multiple Options Configuration";
	
	private static final String STR_NUMBER_CONFIGURATION = "Number configuration";
	
	private static final String STR_COMPOSE_CONFIGURATION = "Compose configuration";
	
	private static final String STR_DIALOG_CONFIGURATION = "Dialog configuration";

	private static final String STR_ERR = "Rioko ERROR: not existing type";
	
	@Override
	public String toString()
	{
		switch(this) 
		{
			case TEXT_CONFIGURATION:
				return STR_TEXT_CONFIGURATION;
			case COMBO_CONFIGURATION:
				return STR_COMBO_CONFIGURATION;
			case NUMBER_CONFIGURATION:
				return STR_NUMBER_CONFIGURATION;
			case COMPOSE_CONFIGURATION:
				return STR_COMPOSE_CONFIGURATION;
			case DIALOG_CONFIGURATION:
				return STR_DIALOG_CONFIGURATION;
		}
		
		return STR_ERR;
	}
	
	public Class<?> getClassNeeded() {
		switch(this) 
		{
			case TEXT_CONFIGURATION:
				return String.class;
			case COMBO_CONFIGURATION:
				return Object.class;
			case NUMBER_CONFIGURATION:
				return Integer.class;
			case COMPOSE_CONFIGURATION:
				return Collection.class;
			case DIALOG_CONFIGURATION:
				return Object.class;
			default:
				break;
		}
		
		return null;
	}
}
