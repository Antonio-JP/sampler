package rioko.layouts.bridge;

import java.util.Collection;

import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.geometry.Rectangle;
import rioko.layouts.graphImpl.LayoutVertex;

public interface LayoutBridge<T, V extends LayoutVertex> {
	/* Basic methods to manage the graph */
	public boolean isReady();
	public void createBridge(Collection<T> previousVertices, Collection<Object> previousEdges);
	
	public boolean isFinished();
	public void applyLayout(LayoutAlgorithm layoutToApply, Rectangle layoutArea);
	public default void applyLayout(LayoutAlgorithm currentAlgorithm, double x, double y, double width, double height) {
		this.applyLayout(currentAlgorithm, new Rectangle(new Point(x,y),width, height));
	}
	
	/* Getting position methods */
	public double getX(T vertex);
	public double getY(T vertex);
	
	public Point getPosition(T vertex);
}
