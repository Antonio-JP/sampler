package rioko.spectral;

import java.util.Set;

import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.lalg.EigDecomposition;
import rioko.lalg.Matrix;
import rioko.lalg.Vector;
import rioko.utilities.collections.ListSet;

public class SpectralAlgorithms {
	public static <V extends Vertex, T extends Matrix<T,R>, R extends Vector<R>> Set<Set<V>> getBiCluster(AbstractGraph<V, ?> graph, Class<T> matrixClass) {
		T laplacian = GraphMatrixUtil.getLaplacianMatrix(graph, matrixClass);
		
		EigDecomposition<T, R> decomposition = laplacian.getEigenvalueDecomposition();
		Set<Set<V>> res = new ListSet<>();
		
		if(decomposition != null) {					
			if(decomposition.size() <= 2) {
				return null;	/* Empty Graph */
			} else if(decomposition.getMultiplicity(0) > 1) {
				/* Disconnected Graph */
				R vector = decomposition.getEigenvector(0, 0);
				
				/* We check if the vector is valid for clustering or is a constant vector */
				boolean isValid = true;
				for(int i = 0; i < vector.size() && isValid; i++) {
					isValid &= (vector.getElement(i) == vector.getElement(0));
				}
				
				if(!isValid) {
					vector = decomposition.getEigenvector(0, 1);
				}
				
				/* We made the clustering */
				int pos = 0;
				Set<V> c1 = new ListSet<V>(), c2 = new ListSet<V>();
				for(V vertex : graph.vertexSet()) {
					if(vector.getElement(pos) == vector.getElement(0)) {
						c1.add(vertex);
					} else {
						c2.add(vertex);
					}
					
					pos++;
				}
				
				res.add(c1);
				res.add(c2);
			} else {
				/* Standard case */
				R vector = decomposition.getEigenvector(1, 0);
				int pos = 0;
				Set<V> positive = new ListSet<V>(), negative = new ListSet<V>();
				
				for(V vertex : graph.vertexSet()) {
					if(vector.getElement(pos) >= 0) {
						positive.add(vertex);
					} else {
						negative.add(vertex);
					}
					
					pos++;
				}
				
				res.add(positive);
				res.add(negative);
			}
		} else {
			res.add(graph.vertexSet());
		}

		return res;
	}
}
