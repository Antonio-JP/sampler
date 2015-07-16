package rioko.spectral;

import java.util.HashMap;

import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.grapht.AdjacencyListGraph;
import rioko.grapht.Vertex;
import rioko.grapht.Edge;
import rioko.lalg.Matrix;
import rioko.lalg.Vector;

public class GraphMatrixUtil {
	/* Value constants */
	private static final double SIMPLE_VALUE = 1;
	private static final double REFERENCE_VALUE = 2;
	private static final double CONTAINMENT_VALUE = 4;

	/* Wrapping methods */
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getAdjacencyMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass) {
		return getAdjacencyMatrix(graph, matrixClass, true);
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getAdjacencyMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass, boolean directed) {
		try {
			T matrix = (matrixClass.newInstance()).getNewMatrix(graph.vertexSet().size(), graph.vertexSet().size());
			
			HashMap<Vertex, Integer> association = new HashMap<>();
			int i = 0;
			for(Vertex vertex : graph.vertexSet()) {
				association.put(vertex, i);
				i++;
			}
			
			int row, col;
			for(Edge<?> edge : graph.edgeSet()) {
				row = association.get(edge.getSource());
				col = association.get(edge.getTarget());
				
				double value = GraphMatrixUtil.getValue(edge.getType());
				
				matrix.setElement(row, col, matrix.getElement(row, col) + value);
				if(!directed) {
					matrix.setElement(col, row, matrix.getElement(col, row) + value);
				}
			}
			
			if(!directed) {
				return matrix.scalar(0.5);
			} else {
				return matrix;
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// impossible Exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getDegreeMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass) {
		return getDegreeMatrix(graph, matrixClass, true);
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getDegreeMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass, boolean directed) {
		T ad = getAdjacencyMatrix(graph, matrixClass, directed);
		if(ad != null) {
			T res = ad.getNewMatrix(ad.rows(), ad.cols());
				
			for(int i = 0; i < ad.rows(); i++) {
				double degree = 0;
				for(int j = 0; j < ad.cols(); j++) {
					degree += ad.getElement(i, j);
				}
				
				res.setElement(i, i, degree);
			}
			
			return res;
		}
		
		return null;
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getLaplacianMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass) {
		return getLaplacianMatrix(graph, matrixClass, true);
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getLaplacianMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass, boolean directed) {
		return getDegreeMatrix(graph,matrixClass,directed).sub(getAdjacencyMatrix(graph, matrixClass,directed));
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getSignlessLaplacianMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass) {
		return getSignlessLaplacianMatrix(graph, matrixClass, true);
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getSignlessLaplacianMatrix(AdjacencyListGraph<?,?> graph, Class<T> matrixClass, boolean directed) {
		return getDegreeMatrix(graph,matrixClass,directed).add(getAdjacencyMatrix(graph, matrixClass,directed));
	}

	/* Other methods */
	private static double getValue(Object type) {
		if(type instanceof typeOfConnection) {
			switch((typeOfConnection)type) {
				case CONTAINMENT:
					return CONTAINMENT_VALUE;
				case REFERENCE:
					return REFERENCE_VALUE;
				case SIMPLE:
					return SIMPLE_VALUE;
				default:
					return 1;
				}
		}
		
		return 1;
	}
}
