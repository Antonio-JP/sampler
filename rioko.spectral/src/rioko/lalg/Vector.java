package rioko.lalg;

public interface Vector<T extends Vector<T>> {
	//Modifying interface
	public double getElement(int pos);
	public void setElement(int pos, double value) throws IllegalArgumentException; 
	
	public int size();
	
	//Basic Vector Operations
	public T add(Vector<T> toAdd) throws ArithmeticException;
	public T opposite();
	public default T sub(Vector<T> toSub) throws ArithmeticException {
		return this.add(toSub.opposite());
	}
	
	public T scalar(double scalar);
	
	public default double norm() {
		return Math.sqrt(this.dot(this));
	}
	
	public default double dot(Vector<T> toDot) throws ArithmeticException{
		if(this.size() != toDot.size()) {
			throw new ArithmeticException("Different size to operate Vectors");
		}
		
		double res = 0;
		for(int i = 0; i < this.size(); i++) {
			res += this.getElement(i) * toDot.getElement(i);
		}
		
		return res;
	}
	
	//Other methods
	public T copy();
}
