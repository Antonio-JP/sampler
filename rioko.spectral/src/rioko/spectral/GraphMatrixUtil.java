package rioko.spectral;

import java.util.HashMap;

import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.grapht.AbstractGraph;
import rioko.grapht.Vertex;
import rioko.grapht.VisibleEdge;
import rioko.lalg.Matrix;
import rioko.lalg.Vector;

public class GraphMatrixUtil {
	/* Value constants */
	private static final double SIMPLE_VALUE = 0;
	private static final double REFERENCE_VALUE = 0;
	private static final double CONTAINMENT_VALUE = 0;

	/* Wrapping methods */
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getAdjacencyMatrix(AbstractGraph<?,?> graph, Class<T> matrixClass) {
		try {
			T matrix = (matrixClass.newInstance()).getNewMatrix(graph.vertexSet().size(), graph.vertexSet().size());
			
			HashMap<Vertex, Integer> association = new HashMap<>();
			int i = 0;
			for(Vertex vertex : graph.vertexSet()) {
				association.put(vertex, i);
				i++;
			}
			
			int row, col;
			for(VisibleEdge<?> edge : graph.edgeSet()) {
				row = association.get(edge.getSource());
				col = association.get(edge.getTarget());
				
				double value = GraphMatrixUtil.getValue(edge.getType());
				
				matrix.setElement(row, col, matrix.getElement(row, col) + value);
			}
		} catch (InstantiationException | IllegalAccessException e) {
			// impossible Exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getDegreeMatrix(AbstractGraph<?,?> graph, Class<T> matrixClass) {
		T ad = getAdjacencyMatrix(graph, matrixClass);
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
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getLaplacianMatrix(AbstractGraph<?,?> graph, Class<T> matrixClass) {
		return getDegreeMatrix(graph,matrixClass).sub(getAdjacencyMatrix(graph, matrixClass));
	}
	
	public static <T extends Matrix<T, R>, R extends Vector<R>> T getSignlessLaplacianMatrix(AbstractGraph<?,?> graph, Class<T> matrixClass) {
		return getDegreeMatrix(graph,matrixClass).add(getAdjacencyMatrix(graph, matrixClass));
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
