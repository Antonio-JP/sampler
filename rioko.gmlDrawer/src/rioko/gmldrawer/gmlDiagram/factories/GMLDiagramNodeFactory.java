package rioko.gmldrawer.gmlDiagram.factories;

import org.w3c.dom.Element;

import rioko.gmldrawer.gmlDiagram.GMLDiagramNode;
import rioko.grapht.VertexFactory;

public class GMLDiagramNodeFactory implements VertexFactory<GMLDiagramNode> {

	@Override
	public GMLDiagramNode createVertex(Object... arg0) {
		if(arg0.length == 0) {
			return new GMLDiagramNode();
		} else if(arg0.length == 1 && (arg0[0] instanceof String)){
			return new GMLDiagramNode((String)arg0[0]);
		} else if(arg0.length == 1 && (arg0[0] instanceof Element)) {
			return new GMLDiagramNode((Element)arg0[0]);
		} else if(arg0.length == 2 && (arg0[0] instanceof String) && (arg0[1] instanceof Element)) {
			return new GMLDiagramNode((String)arg0[0], (Element)arg0[1]);
		} 
		
		return null;
	}

}
