package rioko.savingviews.setting;

import java.util.Map;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.layouts.geometry.Point;

public interface SettingBridge<K> {

	Map<K, Point> getCoordinates();

	K getId(DiagramNode v);
	boolean setIdGen(IdGen<K> generator);
	
	void run();
	boolean setInput(Object object);

}
