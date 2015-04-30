package rioko.graphabstraction.configurations;

public class BadArgumentException extends Exception {

	private static final long serialVersionUID = -9127254524733080559L;
	
	Class<?> classNeeded, classGiven;
	
	public BadArgumentException(Class<?> classNeeded, Class<?> classGiven) {
		// TODO Auto-generated constructor stub
	}

	public BadArgumentException(Class<?> classNeeded, Class<?> classGiven, String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public String getMessage() {
		if(this.classGiven != null && this.classNeeded != null) {
			return "Rioko ERROR: Class " + this.classGiven.getSimpleName() + " given when " + this.classNeeded.getSimpleName() + 
				" was required. (" + super.getMessage() + ")";
		} else {
			return "Rioko ERROR: not valid class given -> " + super.getMessage();
		}
	}

}
