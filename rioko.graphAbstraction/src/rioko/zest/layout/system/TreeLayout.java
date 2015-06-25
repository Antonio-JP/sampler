package rioko.zest.layout.system;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import rioko.zest.layout.GeneralLayoutAlgorithm;

public class TreeLayout extends GeneralLayoutAlgorithm {
	
	//Builders
	public TreeLayout() {
		super();
	}
	
	public TreeLayout(int style) {
		super(style);
	}

	//LayoutAlgorithm methods
	@Override
	public void setEnable(boolean enable) {
		return;
	}
	
	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public SpringLayout copy() {
		SpringLayout res = new SpringLayout(this.getStyle());
		res.setName(this.getName());
		
		return res;
	}

	//GeneralLayoutAlgorithm methods
	@Override
	protected LayoutAlgorithm getLayoutAlgorithm() {
		return new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

	@Override
	protected LayoutAlgorithm getLayoutAlgorithm(int style) {
		return new TreeLayoutAlgorithm(style);
	}
}
