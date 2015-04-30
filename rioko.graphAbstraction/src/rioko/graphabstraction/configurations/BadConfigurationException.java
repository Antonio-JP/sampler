package rioko.graphabstraction.configurations;

public class BadConfigurationException extends Exception {

	private static final long serialVersionUID = -176399828058421749L;

	public BadConfigurationException() { super(); }

	public BadConfigurationException(String arg0) {	super(arg0); }
	
	@Override
	public String getMessage() {
		return "Rioko ERROR: Error while creating the Configuration -> " + super.getMessage();
	}

}
