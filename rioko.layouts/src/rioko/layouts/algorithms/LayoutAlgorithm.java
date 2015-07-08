package rioko.layouts.algorithms;

import rioko.layouts.geometry.Rectangle;
import rioko.layouts.graphImpl.LayoutGraph;
import rioko.utilities.Copiable;

public interface LayoutAlgorithm extends Copiable {
	//Getters & Setters methods
	public boolean setName(String name);
	public String getName();
	
	//Running methods
	public void applyLayout(LayoutGraph graphToLayout, Rectangle layoutArea);
	
	//Overriden methods
	@Override
	public LayoutAlgorithm copy();
}
