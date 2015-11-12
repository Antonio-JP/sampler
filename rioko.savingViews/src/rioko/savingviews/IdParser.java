package rioko.savingviews;

import rioko.graphabstraction.diagram.DiagramNode;

public interface IdParser<K> {
	public K getFromString(String str) throws IllegalArgumentException;
	public K getKey(DiagramNode node);
}
