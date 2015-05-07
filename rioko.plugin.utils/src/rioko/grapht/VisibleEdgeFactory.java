package rioko.grapht;

import org.jgrapht.EdgeFactory;

public class VisibleEdgeFactory<V> implements EdgeFactory<V, VisibleEdge<V>> {

	@Override
	public VisibleEdge<V> createEdge(V arg0, V arg1) {
		return new VisibleEdge<V>(arg0, arg1);
	}

}
