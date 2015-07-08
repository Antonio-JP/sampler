//package rioko.zest.layout.system;
//
//import org.eclipse.zest.layouts.LayoutAlgorithm;
//import org.eclipse.zest.layouts.LayoutStyles;
//import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
//import rioko.zest.layout.GeneralLayoutAlgorithm;
//
//public class SpringLayout extends GeneralLayoutAlgorithm {
//
//	//Builders
//	public SpringLayout() {
//		super();
//	}
//	
//	public SpringLayout(int style) {
//		super(style);
//	}
//
//	
//	//AuxZestLayoutAlgorithm methods
//	@Override
//	public void setEnable(boolean enable) {
//		return;
//	}
//	
//	@Override
//	public boolean isEnable() {
//		return true;
//	}
//
//	@Override
//	public SpringLayout copy() {
//		SpringLayout res = new SpringLayout(this.getStyle());
//		res.setName(this.getName());
//		
//		return res;
//	}
//
//	//GeneralLayoutAlgorithm methods
//	@Override
//	protected LayoutAlgorithm getLayoutAlgorithm() {
//		return new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
//	}
//
//	@Override
//	protected LayoutAlgorithm getLayoutAlgorithm(int style) {
//		return new SpringLayoutAlgorithm(style);
//	}
//
//}
