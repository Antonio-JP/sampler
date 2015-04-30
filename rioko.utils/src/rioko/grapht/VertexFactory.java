package rioko.grapht;

public interface VertexFactory<V> {
	public V createVertex(Object ... args);
}
