package rioko.savingviews.setting;

import java.util.Map;

import org.eclipse.core.resources.IFile;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.layouts.geometry.Point;

public abstract class Loader<K> implements SettingBridge<K> {

	protected IFile file = null;
	private Map<K, Point> coordinates = null;
	
	private IdGen<K> generator;
	
	public Loader(IdGen<K> generator) {
		this.setIdGen(generator);
	}
	
	@Override
	public Map<K, Point> getCoordinates() {
		return this.coordinates;
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
		if(object instanceof IFile) {
			this.file = (IFile) object;
			return true;
		}
		
		return false;
	}

}
