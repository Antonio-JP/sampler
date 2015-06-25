package rioko.spectral;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.lalg.EigDecomposition;
import rioko.lalg.Matrix;
import rioko.lalg.Vector;
import rioko.utilities.Pair;
import rioko.utilities.collections.ListSet;

public class SpectralAlgorithms {
	public static <V extends Vertex, T extends Matrix<T, R>, R extends Vector<R>> Set<Set<V>> getHierarchicalCluster(AbstractGraph<V, ?> graph, int k, Class<T> matrixClass) {
		ArrayList<Pair<Set<V>, EigDecomposition<T,R>>> clusters = new ArrayList<>();
		
		
		//We initiate the variables
		clusters.add(new Pair<Set<V>, EigDecomposition<T,R>>(graph.vertexSet(), 
				GraphMatrixUtil.getLaplacianMatrix(graph, matrixClass, false).getEigenvalueDecomposition()));
		
		while(clusters.size() < k &&(!clusters.get(clusters.size()-1).getLast().equals(Double.MAX_VALUE))) {
			/* Get the lowest energetic cluster (last index) */
			Pair<Set<V>, EigDecomposition<T,R>> current = clusters.get(clusters.size() - 1);
			
			Set<Set<V>> biClust = getBiCluster(current.getFirst(), current.getLast());
			Iterator<Set<V>> iterator = biClust.iterator();
			Set<V> first= iterator.next(), second = iterator.next();
			
			
			/* Remove the previous cluster */
			clusters.remove(clusters.size()-1);
			
			/* Add the new clusters */
			if(clusters.size() == k-1) {
				/* If this is the last iteration, we skip the eigenvalue calculus */
				addWithOrder(clusters, new Pair<>(first, null));
				addWithOrder(clusters, new Pair<>(second, null));
			} else {
				addWithOrder(clusters, 
						new Pair<>(first,GraphMatrixUtil.getLaplacianMatrix(graph.inducedSubgraph(first), matrixClass, false).getEigenvalueDecomposition()));
				addWithOrder(clusters, 
						new Pair<>(second,GraphMatrixUtil.getLaplacianMatrix(graph.inducedSubgraph(second), matrixClass, false).getEigenvalueDecomposition()));
			}
		}
		
		//Create the final clusters from currentClusters
		Set<Set<V>> res = new ListSet<>();
		for(Pair<Set<V>, EigDecomposition<T,R>> pair : clusters) {
			res.add(pair.getFirst());
		}
		
		return res;
	}
	
	public static <V extends Vertex, T extends Matrix<T, R>, R extends Vector<R>> Set<Set<V>> getIterativeCluster(AbstractGraph<V, ?> graph, int k, Class<T> matrixClass) {
		Set<Set<V>> res = new ListSet<>();
		
		/* Easy cases */
		if(k == 1) {
			res.add(graph.vertexSet());
			
			return res;
		} else if(k >= graph.vertexSet().size()) {
			/* There is no enough vertices to make the required clusters */
			for(V vertex : graph.vertexSet()) {
				Set<V> aux = new ListSet<>();
				aux.add(vertex);
				res.add(aux);
			}
			
			return res;
		} else if(k == 2) {
			return getBiCluster(graph, matrixClass);
		}
		
		/* Other cases */
		EigDecomposition<T,R> decomposition = GraphMatrixUtil.getLaplacianMatrix(graph, matrixClass, false).getEigenvalueDecomposition();
		if(decomposition != null) {
			if(decomposition.getMultiplicity(0) >= k) {
				/* We have more than k connected components, so the clusters seoarate them */ 
				Set<Set<V>> connectedComponents = graph.getConnectedComponents(false);
				ArrayList<Set<V>> listOfClusters = new ArrayList<>(k);
				for(int i = 0; i < k; i++) {
					listOfClusters.add(new ListSet<>());
				}
				
				int i = 0;
				for(Set<V> component : connectedComponents) {
					listOfClusters.get(i).addAll(component);
					i = (i+1)%k;
				}
				
				res.addAll(listOfClusters);
			} else {
				R vector = decomposition.getEigenvector(k-1);
				
				Set<V> positive = new ListSet<V>(), negative = new ListSet<V>();
				
				separateBetweenPosAndNeg(graph.vertexSet(), vector, positive, negative);
				
				int posClust = k/2, negClust = k/2;
				if(posClust + negClust < k) {
					posClust += 1;
				} else if(posClust + negClust > k) {
					negClust -= 1;
				}
				
				res.addAll(getIterativeCluster(graph.inducedSubgraph(positive), posClust, matrixClass));
				res.addAll(getIterativeCluster(graph.inducedSubgraph(negative), negClust, matrixClass));
			}
		} else {
			/* Error getting the eigenvalue decomposition -> Return the whole graph */
			res.add(graph.vertexSet());
		}
		
		return res;
	}

	public static <V extends Vertex, T extends Matrix<T,R>, R extends Vector<R>> Set<Set<V>> getBiCluster(AbstractGraph<V, ?> graph, Class<T> matrixClass) {
		T laplacian = GraphMatrixUtil.getLaplacianMatrix(graph, matrixClass, false);
		
		EigDecomposition<T, R> decomposition = laplacian.getEigenvalueDecomposition();

		return getBiCluster(graph.vertexSet(), decomposition);
	}
	
	// Auxiliary methods
	/**
	 * Method to get a BiCluster from a matrix decomposition
	 * 
	 * @param vertices Sorted vertices to clustering
	 * @param decomposition Matrix eigenvalue decomposition used to do clustering
	 * 
	 * @return Set of sets with the two clusters
	 */
	private static <V extends Vertex, T extends Matrix<T,R>, R extends Vector<R>> Set<Set<V>> getBiCluster(Set<V> vertices, EigDecomposition<T,R> decomposition) {
		Set<Set<V>> res = new ListSet<>();
		
		if(decomposition != null) {					
			if(decomposition.size() < 2) {
				/* Empty graph -> Create clusters with no criteria */
				Iterator<V> iterator = vertices.iterator();
				Set<V> first = new ListSet<>(), second = new ListSet<>();
				while(iterator.hasNext()) {
					first.add(iterator.next());
					
					if(iterator.hasNext()) {
						second.add(iterator.next());
					}
				}
				
				res.add(first);
				/* We check there were more than one element */
				if(!second.isEmpty()) {
					res.add(second);
				}
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
				for(V vertex : vertices) {
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
				Set<V> positive = new ListSet<V>(), negative = new ListSet<V>();
				
				separateBetweenPosAndNeg(vertices, vector, positive, negative);
				
				res.add(positive);
				res.add(negative);
			}
		} else {
			res.add(vertices);
		}

		return res;
	}
	
	/**
	 * Method that separates in positive and negatives of the vertices set using a vector. It modifies the Sets arguments positive and negative, but just read the vertices and vector arguments.
	 * 
	 * @param vertices Set of vertices to split (Just read)
	 * @param vector Vector used to split the vertices set (Just read)
	 * @param positive Empty set to save the positive vertices of the vector (Modified)
	 * @param negative Empty set to save the negative vertices of the vector (Modified)
	 */
	private static <V extends Vertex, R extends Vector<R>> void separateBetweenPosAndNeg(Set<V> vertices, R vector, Set<V> positive,
			Set<V> negative) {
		/* Create a new vector checking the "nearly zero" values */
		/* The next loop assures that the near zero values are set to zero */
		R aux = vector.copy();
		for(int i = 0; i < aux.size(); i++) {
			double currentValue = aux.getElement(i);
			double newValue = Math.signum(currentValue) * BigDecimal.valueOf(Math.abs(currentValue)).setScale(8, RoundingMode.DOWN).doubleValue();
			aux.setElement(i, newValue);
		}
		
		/* Check the special cases */
		boolean allPositive = true, allConstant = true;
		for(int i = 0; (i < aux.size()) && (allPositive || allConstant); i++) {
			allPositive &= (aux.getElement(i) >= 0);
			allConstant &= (aux.getElement(i) == aux.getElement(0));
		}
		
		if(allConstant) {
			/* Constant vector -> nothing to do */
			positive.addAll(vertices);
			return;
		}
		
		if(allPositive) {
			/* There should be zero values and positive ones. Scale the vector with -1 to distinguis those vertices */
			aux = aux.scalar(-1);
		} 
		
		/* We split now using the sign of the elements of aux vector */
		int pos = 0;
		for(V vertex : vertices) {
			if(aux.getElement(pos) >= 0) {
				positive.add(vertex);
			} else {
				negative.add(vertex);
			}
			
			pos++;
		}
		
		return;
	}
	
	//Methods to Hierachical Clustering
	/**
	 * Method to get the energy required to cut a graph using its eigenvalue decomposition.
	 * 
	 * @param decomposition Decomposition of the Laplacian matrix of the graph
	 * 
	 * @return Double with the value of the second eigenvalue. Double.MAX_VALUE if there is no decomposition
	 */
	private static <T extends Matrix<T,R>, R extends Vector<R>> Double getEnergyFromDecomposition(EigDecomposition<T, R> decomposition) {
		if(decomposition == null) {
			/* Error getting the decomposition */
			return Double.MAX_VALUE;
		} else if(decomposition.size()< 2) {
			/* All eigenvalues are the same */
			return decomposition.getEigenvalue(0);
		} else {
			/* There is a non-trivial eigenvalue */
			return decomposition.getEigenvalue(1);
		}
	}
	
	/**
	 * Method to add a new Cluster to a List keeping the list ordered. The first element has the greatest energy
	 * 
	 * @param list List to modify
	 * @param pairToAdd Pair with the Cluster and its decomposition.
	 */
	private static <V extends Vertex, T extends Matrix<T, R>, R extends Vector<R>> void addWithOrder(ArrayList<Pair<Set<V>, EigDecomposition<T,R>>> list, 
			Pair<Set<V>, EigDecomposition<T,R>> pairToAdd) {
		Double energy = getEnergyFromDecomposition(pairToAdd.getLast());
		
		boolean added = false;
		for(int i = 0; i < list.size(); i++) {
			if(energy > getEnergyFromDecomposition(list.get(i).getLast())) {
				added = true;
				list.add(i, pairToAdd);
				break;
			}
		}
		
		if(!added) {
			list.add(pairToAdd);
		}
	}
}
