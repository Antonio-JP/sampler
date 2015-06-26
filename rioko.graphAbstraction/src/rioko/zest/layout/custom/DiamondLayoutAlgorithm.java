package rioko.zest.layout.custom;

import org.eclipse.zest.layouts.LayoutStyles;

import rioko.zest.layout.CustomLayoutAlgorithm;
import rioko.zest.layout.Point;

public class DiamondLayoutAlgorithm extends CustomLayoutAlgorithm {

	public DiamondLayoutAlgorithm() {
		super(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}
	
	public DiamondLayoutAlgorithm(int styles) {
		super(styles);
	}

	//Copiable methods
	@Override
	public DiamondLayoutAlgorithm copy() {
		DiamondLayoutAlgorithm res = new DiamondLayoutAlgorithm(this.getStyle());
		res.setName(this.getName());
		
		return res;
	}

	//CustomLayoutAlgorithmMethods
	@Override
	protected void running() {
		for(int i = 0; i < this.nodes.length; i++) {
			this.setDVector(this.nodes[i], this.getVector(i).scale(Math.ceil(i/4.)));
		}
	}

	private Point getVector(int i) {
		if(i%4 == 0) {
			return new Point(100,0);
		} else if(i%4 == 1) {
			return new Point(0,30);
		} else if (i%4 == 2) {
			return new Point(-100,0);
		} else {
			return new Point(0,-30);
		}
	}
}
