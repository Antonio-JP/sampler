package rioko.utilities;

public class Pair<T,P> {
	private T first;
	private P last;
	
	public Pair() {
		this(null,null);
	}
	
	public Pair(T first, P last) {
		this.first = first;
		this.last = last;
	}
	
	//Getters & Setters
	public T getFirst() {
		return first;
	}

	public void setFirst(T first) {
		this.first = first;
	}

	public P getLast() {
		return last;
	}

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
