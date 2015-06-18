package rioko.lalg.ejml;

import org.ejml.simple.SimpleMatrix;

import rioko.lalg.EigDecomposition;
import rioko.lalg.Matrix;
import rioko.lalg.VectorImpl;

public class EJMLSimpleMatrixWrapper implements Matrix<EJMLSimpleMatrixWrapper, VectorImpl> {

	private SimpleMatrix wrap = null;
	
	//Builders
	public EJMLSimpleMatrixWrapper() {
		this.wrap = new SimpleMatrix();
	}
	
	public EJMLSimpleMatrixWrapper(int rows, int cols) {
		this.wrap = new SimpleMatrix(rows, cols);
	}
	
	private EJMLSimpleMatrixWrapper(SimpleMatrix matrix) {
		this.wrap = matrix;
	}
	
	//Getters & Setters
	SimpleMatrix getMatrix() {
		return this.wrap;
	}

	//Matrix methods
	@Override
	public EJMLSimpleMatrixWrapper getNewMatrix(int rows, int cols) {
		return new EJMLSimpleMatrixWrapper(rows, cols);
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
		return this.wrap.numRows();
	}

	@Override
	public int cols() {
		return this.wrap.numCols();
	}

	@Override
	public EJMLSimpleMatrixWrapper add(EJMLSimpleMatrixWrapper toAdd) throws ArithmeticException {
		if(this.cols() != toAdd.cols() || this.rows() != toAdd.cols()) {
			throw new ArithmeticException("Dimension error to add two matrices");
		}
		
		return new EJMLSimpleMatrixWrapper(this.wrap.plus(toAdd.getMatrix()));
	}

	@Override
	public EJMLSimpleMatrixWrapper opposite() {
		return this.scalar(-1);
	}

	@Override
	public EJMLSimpleMatrixWrapper mult(EJMLSimpleMatrixWrapper toMult) throws ArithmeticException {
		if(this.cols() != toMult.rows()) {
			throw new ArithmeticException("Dimension error to multiply matrix");
		}

		return new EJMLSimpleMatrixWrapper(this.wrap.mult(toMult.getMatrix()));
	}

	@Override
	public EJMLSimpleMatrixWrapper scalar(double scalar) {
		return new EJMLSimpleMatrixWrapper(this.wrap.scale(scalar));
	}

	@Override
	public EJMLSimpleMatrixWrapper transpose() {
		return new EJMLSimpleMatrixWrapper(this.wrap.transpose());
	}

	@Override
	public EJMLSimpleMatrixWrapper inverse() throws ArithmeticException {
		if(!this.hasInverse()) {
			throw new ArithmeticException("Matrix inverse does not exist"); 
		}
		
		return new EJMLSimpleMatrixWrapper(this.wrap.invert());
	}

	@Override
	public EigDecomposition<EJMLSimpleMatrixWrapper, VectorImpl> getEigenvalueDecomposition() {
		return new EJMLEDWrapper(this);
	}

	@Override
	public double determinant() {
		return this.wrap.determinant();
	}
}
