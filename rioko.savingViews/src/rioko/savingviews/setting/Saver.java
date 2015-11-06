package rioko.savingviews.setting;

import java.util.HashMap;
import java.util.Map;


import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.layouts.geometry.Point;

public abstract class Saver<K> implements SettingBridge<K> {

	private ModelDiagram<?> diagram;
	private Map<K, Point> positions;
	
	private IdGen<K> generator;
	
	public Saver(IdGen<K> generator) {
		this.setIdGen(generator);
	}
	
	@Override
	public Map<K, Point> getCoordinates() {
		DiagramGraph printable = diagram.getPrintDiagram();
		
		this.positions = new HashMap<K, Point>();
		for(DiagramNode node : printable.vertexSet()) {
			Point pos = new Point(node.getFigure().getBounds().preciseX(), node.getFigure().getBounds().preciseY());
			this.positions.put(this.getId(node), pos);
		}

		return null;
	}
	
	@Override
	public K getId(DiagramNode node) {
		return this.generator.getId(node);
	}
	
	@Override
	public boolean setIdGen(IdGen<K> generator) {
		if(generator != null) {
			this.generator = generator;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean setInput(Object object) {
		if(object instanceof ModelDiagram) {
			this.diagram = (ModelDiagram<?>) object;
			return true;
		}
		
		return false;
	}

	
}
