package rioko.layouts.wrapper.zest.impl;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import rioko.layouts.wrapper.zest.ZestLayoutWrapper;

public class TreeZestLayout extends ZestLayoutWrapper {

	@Override
	protected LayoutAlgorithm getAlgorithm() {
		return new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

}
