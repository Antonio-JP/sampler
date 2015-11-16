package rioko.layouts.algorithms.basic;

import rioko.layouts.algorithms.AbstractLayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.graphImpl.LayoutVertex;

public abstract class CenterLayoutAlgorithm extends AbstractLayoutAlgorithm {

	@Override
	public void adjust() {
		super.adjust();
		
		double sumX = 0, sumY = 0;
		for(LayoutVertex vertex : this.graph.vertexSet()) {
			Point relative = this.getLayoutArea().getRelative(vertex.getPosition());
			sumX += relative.getX();
			sumY += relative.getY();
		}
		
		Point vectorMove = new Point(-sumX/this.graph.vertexSet().size(), -sumY/this.graph.vertexSet().size());
		
		for(LayoutVertex vertex : this.graph.vertexSet()) {
			this.addDVector(vertex, vectorMove);
		}
	}

}
