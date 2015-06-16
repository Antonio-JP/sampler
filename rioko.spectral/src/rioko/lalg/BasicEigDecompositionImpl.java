package rioko.lalg;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class BasicEigDecompositionImpl<T extends Matrix<T,R>, R extends Vector<R>> implements EigDecomposition<T, R> {

	protected boolean run = false;
	
	protected ArrayList<Double> eigenvalues = new ArrayList<>();
	protected HashMap<Double, ArrayList<R>> eigenvectors = new HashMap<>();
	
	//EigDecomposition methods
	@Override
	public int size() {
		this.runProccess();

		return this.eigenvalues.size();
	}

	@Override
	public double getEigenvalue(int element) {
		this.runProccess();
		
		if(element < 0 || element >= this.size()) {
			throw new IllegalArgumentException("Non existing eigenvalue");
		}

		return this.eigenvalues.get(element);
	}

	@Override
	public int getMultiplicity(int element) {
		this.runProccess();

		if(element < 0 || element >= this.size()) {
			throw new IllegalArgumentException("Non existing eigenvalue");
		}
		
		return this.eigenvectors.get(this.eigenvalues.get(element)).size();
	}

	@Override
	public R getEigenvector(int element, int multiplicity) {
		this.runProccess();
			
		if(element < 0 || element >= this.size() || multiplicity < 0 || multiplicity >= this.getMultiplicity(element)) {
			throw new IllegalArgumentException("Non existing eigenvalue");
		}

		return this.eigenvectors.get(this.eigenvalues.get(element)).get(multiplicity);
	}
	
	protected abstract void runProccess();

}
