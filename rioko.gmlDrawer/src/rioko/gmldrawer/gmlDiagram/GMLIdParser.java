package rioko.gmldrawer.gmlDiagram;

import rioko.drawmodels.diagram.IdParser;
import rioko.graphabstraction.diagram.DiagramNode;

public class GMLIdParser implements IdParser {

	@Override
	public Object getFromString(String str) throws IllegalArgumentException {
		return str;
	}

	@Override
	public Object getKey(DiagramNode node) {
		if(node instanceof GMLDiagramNode) {
			GMLDiagramNode gmlNode = (GMLDiagramNode)node;
			
			return gmlNode.getRawData().getAttribute("id");
		}
		
		return null;
	}

}
