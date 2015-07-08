//package rioko.zest.layout.custom;
//
//import org.eclipse.zest.layouts.LayoutStyles;
//import org.eclipse.zest.layouts.dataStructures.InternalNode;
//
//import rioko.zest.layout.CustomLayoutAlgorithm;
//import rioko.zest.layout.Point;
//
//public class DiamondLayoutAlgorithm extends CustomLayoutAlgorithm {
//	
//	private double maxWidth = 100, maxHeight = 30;
//
//	public DiamondLayoutAlgorithm() {
//		super(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
//	}
//	
//	public DiamondLayoutAlgorithm(int styles) {
//		super(styles);
//	}
//
//	//Copiable methods
//	@Override
//	public DiamondLayoutAlgorithm copy() {
//		DiamondLayoutAlgorithm res = new DiamondLayoutAlgorithm(this.getStyle());
//		res.setName(this.getName());
//		
//		return res;
//	}
//
//	//CustomLayoutAlgorithmMethods
//	@Override
//	protected void running() {
//		for(InternalNode node : this.nodes) {
//			if(node.getWidthInLayout() > this.maxWidth) {
//				this.maxWidth = node.getWidthInLayout();
//			}
//			
//			if(node.getHeightInLayout() > this.maxHeight) {
//				this.maxHeight = node.getHeightInLayout();
//			}
//		}
//		
//		for(int i = 0; i < this.nodes.length; i++) {
//			this.setDVector(this.nodes[i], this.getVector(i).scale(Math.ceil(i/4.)));
//		}
//	}
//
//	private Point getVector(int i) {
//		if(i%4 == 0) {
//			return new Point(this.maxWidth,0);
//		} else if(i%4 == 1) {
//			return new Point(0,this.maxHeight);
//		} else if (i%4 == 2) {
//			return new Point(-this.maxWidth,0);
//		} else {
//			return new Point(0,-this.maxHeight);
//		}
//	}
//}
