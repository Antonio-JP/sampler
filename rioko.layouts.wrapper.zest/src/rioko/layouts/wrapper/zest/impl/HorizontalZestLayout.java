package rioko.layouts.wrapper.zest.impl;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalLayoutAlgorithm;

import rioko.layouts.wrapper.zest.ZestLayoutWrapper;

public class HorizontalZestLayout extends ZestLayoutWrapper {

	@Override
	protected LayoutAlgorithm getAlgorithm() {
		return new HorizontalLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

}
