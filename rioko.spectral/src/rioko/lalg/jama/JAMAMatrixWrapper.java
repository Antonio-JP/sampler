package rioko.lalg.jama;

import rioko.lalg.EigDecomposition;
import rioko.lalg.Matrix;
import rioko.lalg.VectorImpl;

public class JAMAMatrixWrapper implements Matrix<JAMAMatrixWrapper, VectorImpl> {

	private Jama.Matrix wrap = null;
	
	//Builders
	public JAMAMatrixWrapper() {
		this.wrap = new Jama.Matrix(0, 0);
	}
	
	public JAMAMatrixWrapper(int rows, int cols) {
		this.wrap = new Jama.Matrix(rows, cols);
	}
	
	JAMAMatrixWrapper(Jama.Matrix matrix) {
		this.wrap = matrix;
	}
	
	//Getters & Setters
	Jama.Matrix getMatrix() {
		return this.wrap;
	}
	
	//Matrix methods
	@Override
	public JAMAMatrixWrapper getNewMatrix(int rows, int cols) {
		return new JAMAMatrixWrapper(rows, cols);
	}

	@Override
	public double getElement(int row, int column) throws IllegalArgumentException {
		if(row < 0 || column < 0 || row >= this.rows() || column >= this.cols()) {
			throw new IllegalArgumentException("Dimension error accesing the matrix");
		}
		
		return this.wrap.get(row, column);
	}

	@Override
	public void setElement(int row, int column, double value) throws IllegalArgumentException {
		if(row < 0 || column < 0 || row >= this.rows() || column >= this.cols()) {
			throw new IllegalArgumentException("Dimension error accesing the matrix");
		}
		
		this.wrap.set(row, column, value);
	}

	@Override
	public int rows() {
		return this.wrap.getRowDimension();
	}

	@Override
	public int cols() {
		return this.wrap.getColumnDimension();
	}

	@Override
	public JAMAMatrixWrapper add(JAMAMatrixWrapper toAdd) throws ArithmeticException {
		if(this.cols() != toAdd.cols() || this.rows() != toAdd.cols()) {
			throw new ArithmeticException("Dimension error to add two matrices");
		}
		
		return new JAMAMatrixWrapper(this.wrap.plus(toAdd.getMatrix()));
	}

	@Override
	public JAMAMatrixWrapper opposite() {
		return this.scalar(-1);
	}

	@Override
	public JAMAMatrixWrapper mult(JAMAMatrixWrapper toMult) throws ArithmeticException {
		if(this.cols() != toMult.rows()) {
			throw new ArithmeticException("Dimension error to multiply matrix");
		}

		return new JAMAMatrixWrapper(this.wrap.times(toMult.getMatrix()));
	}

	@Override
	public JAMAMatrixWrapper scalar(double scalar) {
		return new JAMAMatrixWrapper(this.wrap.times(scalar));
	}

	@Override
	public JAMAMatrixWrapper transpose() {
		return new JAMAMatrixWrapper(this.wrap.transpose());
	}

	@Override
	public JAMAMatrixWrapper inverse() throws ArithmeticException {
		if(!this.hasInverse()) {
			throw new ArithmeticException("Matrix inverse does not exist"); 
		}
		
		return new JAMAMatrixWrapper(this.wrap.inverse());
	}

	@Override
	public double determinant() {
		return this.wrap.det();
	}

	@Override
	public EigDecomposition<JAMAMatrixWrapper, VectorImpl> getEigenvalueDecomposition() {
		return new JAMAEDWrapper(this);
	}

}
