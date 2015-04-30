package rioko.runtime.registers;

public class NotRegisteredException extends Exception {

	private static final long serialVersionUID = 8082028703235948686L;

	private Class<?> classRequired;
	
	public NotRegisteredException(Class<?> classRequired, String message) {
		super(message);
		
		this.classRequired = classRequired;
	}
	
	@Override
	public String getMessage() {
		return "Try to get the class " + classRequired.getName() + " and it was not registered. (" + super.getMessage() + ");";
	}
}
