package rioko.lalg;

public class MatrixImpl implements Matrix<MatrixImpl, VectorImpl> {
	
	int rows = 0, cols = 0;
	double elements[][];
	
	public MatrixImpl() { }
	
	public MatrixImpl(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		
		this.elements = new double[rows][cols];
	}

	@Override
	public MatrixImpl getNewMatrix(int rows, int cols) {
		return new MatrixImpl(rows,cols);
	}

	@Override
	public double getElement(int row, int column) throws IllegalArgumentException{
		if(row < 0 || column < 0 || row >= this.rows() || column >= this.cols()) {
			throw new IllegalArgumentException("Dimension error in the arguments");
		}
		
		return this.elements[row][column];
	}

	@Override
	public void setElement(int row, int column, double value) throws IllegalArgumentException{
		if(row < 0 || column < 0 || row >= this.rows() || column >= this.cols()) {
			throw new IllegalArgumentException("Dimension error in the arguments");
		}
		
		this.elements[row][column] = value;
	}

	@Override
	public int rows() {
		return this.rows;
	}

	@Override
	public int cols() {
		return this.cols;
	}

	@Override
	public MatrixImpl add(MatrixImpl toAdd) throws ArithmeticException {
		if(this.rows() != toAdd.rows() || this.cols() != toAdd.cols()) {
			throw new ArithmeticException("Different size between matrices");
		}
		
		MatrixImpl newMatrix = this.getNewMatrix(this.rows(), this.cols());
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				newMatrix.setElement(i, j, this.getElement(i, j)+toAdd.getElement(i, j));
			}
		}
		
		return newMatrix;
	}

	@Override
	public MatrixImpl opposite() {
		return this.scalar(-1);
	}

	@Override
	public MatrixImpl mult(MatrixImpl toMult) throws ArithmeticException {
		if(this.rows() != toMult.cols()) {
			throw new ArithmeticException("Different size between matrices");
		}
		
		MatrixImpl newMatrix = this.getNewMatrix(this.rows(), toMult.cols());
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < toMult.cols(); j++) {
				double value = 0;
				for(int k = 0; k < this.cols(); k++) {
					value += this.getElement(i, k)*toMult.getElement(k, j);
				}
				
				newMatrix.setElement(i, j, value);
			}
		}
		
		return newMatrix;
	}

	@Override
	public MatrixImpl scalar(double scalar) {
		MatrixImpl newMatrix = this.getNewMatrix(this.rows(), this.cols());
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				newMatrix.setElement(i, j, this.getElement(i, j)*scalar);
			}
		}
		
		return newMatrix;
	}

	@Override
	public MatrixImpl transpose() {
		MatrixImpl newMatrix = this.getNewMatrix(this.cols(), this.rows());
		for(int i = 0; i < this.rows(); i++) {
			for(int j = 0; j < this.cols(); j++) {
				newMatrix.setElement(i, j, this.getElement(j, i));
			}
		}
		
		return newMatrix;
	}

	@Override
	public MatrixImpl inverse() throws ArithmeticException {
		return null;
	}

	@Override
	public double determinant() {
		//Determinant not implemented
		return 0;
	}

	@Override
	public EigDecomposition<MatrixImpl, VectorImpl> getEigenvalueDecomposition() {
		return null;
	}
}
