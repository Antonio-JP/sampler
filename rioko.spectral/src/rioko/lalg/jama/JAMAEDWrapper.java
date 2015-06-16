package rioko.lalg.jama;

import java.util.ArrayList;

import rioko.lalg.BasicEigDecompositionImpl;
import rioko.lalg.VectorImpl;

public class JAMAEDWrapper extends BasicEigDecompositionImpl<JAMAMatrixWrapper, VectorImpl> {
	
	private Jama.EigenvalueDecomposition decomposition = null;
	
	//Builders	
	public JAMAEDWrapper(JAMAMatrixWrapper matrix) {
		this(matrix.getMatrix());
	}
	
	private JAMAEDWrapper(Jama.Matrix matrix) {
		this.decomposition = matrix.eig();
	}

	
	
	//Other methods
	protected void runProccess() {
		if(!this.run) {
			this.run = true;
			
			//Get all the eigenvalues adn eigenvectors
			JAMAMatrixWrapper D = new JAMAMatrixWrapper(this.decomposition.getD());
			JAMAMatrixWrapper V = new JAMAMatrixWrapper(this.decomposition.getV());
			
			for(int i = 0; i < D.cols(); i++) {
				double ein = D.getElement(i, i);
				
				if(!this.eigenvectors.containsKey(ein)) {
					//The eigenvalues are sorted in the list from the minor to the mayor
					int j;
					for(j = 0; j < this.eigenvalues.size(); j++) {
					if(this.eigenvalues.get(j) > ein) {
							break;
						}
					}
				
					this.eigenvalues.add(j, ein);
					this.eigenvectors.put(ein, new ArrayList<>());
				}
				
				//Wrap the vector to VectorImpl
				VectorImpl vector = new VectorImpl(V.rows());
				for(int k = 0; k < V.rows(); k++) {
					vector.setElement(k, V.getElement(k, i));
				}
				
				this.eigenvectors.get(ein).add(vector);
			}
		}
	}

}
