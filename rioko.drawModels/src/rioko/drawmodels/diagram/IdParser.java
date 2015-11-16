package rioko.drawmodels.diagram;

import rioko.graphabstraction.diagram.DiagramNode;

public interface IdParser {
	public Object getFromString(String str) throws IllegalArgumentException;
	public Object getKey(DiagramNode node);
}
