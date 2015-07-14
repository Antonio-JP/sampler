package rioko.drawmodels.filemanage;

import rioko.drawmodels.diagram.ModelDiagram;
//import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;

public interface Reader<T> {
	public String getFileName();
	public T resolve(ProxyDiagramNode<T> proxy);
	public ModelDiagram<T> getModel();
	public ModelDiagram<T> getModel(ProxyDiagramNode<T> proxy);
	public void processNode(DiagramNode node, T current);
}
