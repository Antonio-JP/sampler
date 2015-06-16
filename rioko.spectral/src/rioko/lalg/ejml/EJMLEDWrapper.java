package rioko.lalg.ejml;

import java.util.ArrayList;

import org.ejml.simple.SimpleEVD;
import org.ejml.simple.SimpleMatrix;

import rioko.lalg.BasicEigDecompositionImpl;
import rioko.lalg.VectorImpl;

public class EJMLEDWrapper extends BasicEigDecompositionImpl<EJMLSimpleMatrixWrapper, VectorImpl> {

	 private SimpleEVD<SimpleMatrix> decomposition;
	
	//Builders	
	public EJMLEDWrapper(EJMLSimpleMatrixWrapper matrix) {
		this(matrix.getMatrix());
	}
		
	@SuppressWarnings("unchecked")
	private EJMLEDWrapper(SimpleMatrix matrix) {
		this.decomposition = matrix.eig();
	}
	
	@Override
	protected void runProccess() {
		if(!this.run) {
			this.run = true;
			
			//Get all the eigenvalues adn eigenvectors
			
			for(int i = 0; i < this.decomposition.getNumberOfEigenvalues(); i++) {
				double ein = this.decomposition.getEigenvalue(i).real;
				
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
				SimpleMatrix eigenVector = this.decomposition.getEigenVector(i);
				VectorImpl vector = new VectorImpl(eigenVector.getNumElements());
				for(int k = 0; k < eigenVector.getNumElements(); k++) {
					vector.setElement(k, eigenVector.get(k));
				}
				
				this.eigenvectors.get(ein).add(vector);
			}
		}
	}

}
