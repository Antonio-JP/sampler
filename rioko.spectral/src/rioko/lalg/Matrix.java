package rioko.lalg;

public interface Matrix<T extends Matrix<T,R>, R extends Vector<R>> {	
	//Building interface
	public T getNewMatrix(int rows, int cols);
	public default T getIdentity(int size) {
		T newMatrix = this.getNewMatrix(size, size);
		
		for(int i = 0; i < size; i++) {
			newMatrix.setElement(i, i, 1);
		}
		
		return newMatrix;
	}
	
	//Modifying interface
	public double getElement(int row, int column) throws IllegalArgumentException;
	public void setElement(int row, int column, double value) throws IllegalArgumentException;
	
	public int rows();
	public int cols();
	
	//Basic matrix operations
	public T add(T toAdd) throws ArithmeticException;
	public T opposite();
	public default T sub(T toSub) throws ArithmeticException{
		return this.add(toSub.opposite());
	}
	
	public T mult(T toMult) throws ArithmeticException;
	public T scalar(double scalar);
	
	public T transpose();
	public T inverse() throws ArithmeticException;
	
	//Matrix invariants
	public double determinant();
	public default double trace() throws ArithmeticException {
		if(!this.isSquare()) {
			throw new ArithmeticException();
		}
		
		double res = 0;
		for(int i = 0; i < this.rows(); i++) {
			res += this.getElement(i, i);
		}
		
		return res;
	}
	
	//Boolean conditions
	public default boolean isSym() {
		if(!this.isSquare()) {
			return false;
		}
		
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				if(this.getElement(i, j) != this.getElement(j, i)) {
					return false;
				}
			}
		}
		
		return true;
	}
	
	public default boolean isSquare() {
		return this.rows() == this.cols();
	}
	
	public default boolean hasInverse() {
		return (this.determinant() != 0);
	}
	
	//Matrix Decompositions
	public EigDecomposition<T,R> getEigenvalueDecomposition();
	
	//Other methods
	public default T copy() {
		T copy = this.getNewMatrix(this.rows(), this.cols());
		
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				copy.setElement(i, j, this.getElement(i, j));
			}
		}
		
		return copy;
	}
}
