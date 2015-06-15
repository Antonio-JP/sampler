package rioko.lalg;

import java.util.ArrayList;

import rioko.utilities.collections.ArrayMap;

public interface EigDecomposition<T extends Matrix<T,R>, R extends Vector<R>> {
	
	public int size();
	
	public double getEigenvalue(int element);
	public int getMultiplicity(int element);
	public R getEigenvector(int element, int multiplicity);
	
	public default ArrayList<R> getEinBase() {
		ArrayList<R> res = new ArrayList<>();
		
		for(int i = 0; i < this.size(); i++) {
			for(int j = 0; j < this.getMultiplicity(i); j ++) {
				res.add(this.getEigenvector(i, j));
			}
		}
		
		return res;
	}
	
	public default ArrayMap<Double, ArrayList<R>> getEigenvectors() {
		ArrayMap<Double,ArrayList<R>> res = new ArrayMap<>();
		
		for(int i = 0; i < this.size(); i++) {
			ArrayList<R> aux = new ArrayList<>();
			for(int j = 0; j < this.getMultiplicity(i); j ++) {
				aux.add(this.getEigenvector(i, j));
			}
			res.put(this.getEigenvalue(i),aux);
		}
		
		return res;
	}
}
