package rioko.graphabstraction.diagram;

import rioko.grapht.Vertex;
import rioko.zest.Drawable;

public interface ProxyDiagramNode<T> extends Vertex, Drawable {
	public T getProxyObject();
}
