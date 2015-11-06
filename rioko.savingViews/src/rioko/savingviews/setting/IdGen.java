package rioko.savingviews.setting;

import rioko.graphabstraction.diagram.DiagramNode;

public interface IdGen<K> {
	K getId(DiagramNode node);
}
