package rioko.grapht;

import rioko.utilities.Copiable;

public interface Vertex extends Copiable {
	public VertexFactory<?> getVertexFactory();
}
