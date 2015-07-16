package rioko.utilities;

/**
 * Class to manage pair of elements. Those elements are generic and can be configured when create the Pair object.
 * 
 * @author Antonio
 *
 * @param <T> Class for the First element of the Pair
 * @param <P> Class for the Last element of the Pair
 */
public class Pair<T,P> {
	/**
	 * First element of the Pair
	 */
	private T first;
	/**
	 * Last element of the Pair
	 */
	private P last;
	
	public Pair() {
		this(null,null);
	}
	
	public Pair(T first, P last) {
		this.first = first;
		this.last = last;
	}
	
	//Getters & Setters
	/**
	 * Getter method for the first element
	 * @return T with the first element of the Pair. It could be a null object.
	 */
	public T getFirst() {
		return first;
	}

	/**
	 * Setter method for the first element
	 * @param first T object which will be the First element of this Pair. It could be null.
	 */
	public void setFirst(T first) {
		this.first = first;
	}

	/**
	 * Getter method for the last element
	 * @return P with the last element of the Pair. It could be a null object.
	 */
	public P getLast() {
		return last;
	}

	/**
	 * Setter method for the last element
	 * @param last P object which will be the Last element of this Pair. It could be null.
	 */
	public void setLast(P last) {
		this.last = last;
	}
	
	//Overriden methods
	@Override
	public boolean equals(Object ob)
	{
		if(ob instanceof Pair<?,?>) {
			Pair<?,?> pair = (Pair<?,?>)ob;
			
			if(this.first != null && this.last != null) {
				return this.first.equals(pair.getFirst()) && this.last.equals(pair.getLast());
			} else if(this.first == null && this.last != null) {
				return (pair.getFirst() == null) && this.last.equals(pair.getLast());
			} else if(this.first != null && this.last == null) {
				return this.first.equals(pair.getFirst()) && (pair.getLast() == null);
			} else {
				return (pair.getFirst() == null) && (pair.getLast() == null);
			}
		}
		
		return false;
	}
	
	@Override
	public String toString()
	{
		return "[Pair: ("+this.first.toString()+", "+this.last.toString()+")];";
	}
	
	@Override
	public int hashCode()
	{
		return (this.first.hashCode() + this.last.hashCode())/2;
	}
}
