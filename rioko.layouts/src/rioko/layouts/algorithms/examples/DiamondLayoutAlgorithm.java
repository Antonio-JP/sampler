package rioko.layouts.algorithms.examples;

import rioko.layouts.algorithms.AbstractLayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.graphImpl.LayoutVertex;

public class DiamondLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	private double maxWidth = 100, maxHeight = 30;

	//Copiable methods
	@Override
	public DiamondLayoutAlgorithm copy() {
		DiamondLayoutAlgorithm res = new DiamondLayoutAlgorithm();
		res.setName(this.getName());
		
		return res;
	}

	//CustomLayoutAlgorithmMethods
	@Override
	protected void preparation() {
		this.maxHeight = 30;
		this.maxWidth = 100;
	}
	
	@Override
	protected void running() {
		for(LayoutVertex node : this.graph.vertexSet()) {
			if(node.getWidth() > this.maxWidth) {
				this.maxWidth = node.getWidth();
			}
			
			if(node.getHeight() > this.maxHeight) {
				this.maxHeight = node.getHeight();
			}
		}
		
		int i = 0;
		for(LayoutVertex node : this.graph.vertexSet()) {
			this.setDVector(node, this.getVector(i).scale(Math.ceil(i/4.)));
			i++;
		}
	}

	private Point getVector(int i) {
		if(i%4 == 0) {
			return new Point(this.maxWidth,0);
		} else if(i%4 == 1) {
			return new Point(0,this.maxHeight);
		} else if (i%4 == 2) {
			return new Point(-this.maxWidth,0);
		} else {
			return new Point(0,-this.maxHeight);
		}
	}
}
