package rioko.runtime.registers;

public class RegisterRuntimeException extends Exception {

	private static final long serialVersionUID = -4945680907039468351L;

	private Class<?> tryToRegister;
	private Class<?> needToRegister;
	
	public RegisterRuntimeException(Class<?> tryToRegister, Class<?> needToRegister, String message) {
		super(message);
		
		this.tryToRegister = tryToRegister;
		this.needToRegister = needToRegister;
	}
	
	@Override
	public String getMessage() {
		return "Try to register the class " + tryToRegister.getName() + " and it was needed " + needToRegister.getName() + ". (" + super.getMessage() + ");";
	}
}
