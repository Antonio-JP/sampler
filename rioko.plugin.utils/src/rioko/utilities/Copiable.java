package rioko.utilities;

public interface Copiable {
	/**
	 * Method to get a copy of the element. The returned object should return true to the call
	 * 	[this.equals(this.copy())]
	 * 
	 * @return A new Object that is "exactly" the same.
	 */
	public Copiable copy();
}
