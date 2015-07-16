package rioko.utilities;

/**
 * Interface that model an Object that have a Type associated. This Type is an undetermined Object.
 * 
 * @author Antonio
 */
public interface Typable {
	/**
	 * Setter method for the Type
	 * 
	 * @param obj Object that will be the Type of this object.
	 */
	public void setType(Object obj);
	
	/**
	 * Getter method for the Type.
	 * 
	 * @return Object with the Type information for this object.
	 */
	public Object getType();
}
