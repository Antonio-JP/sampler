package rioko.layouts.wrapper.zest.impl;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalTreeLayoutAlgorithm;

import rioko.layouts.wrapper.zest.ZestLayoutWrapper;

public class HorizontalTreeZestLayout extends ZestLayoutWrapper {

	@Override
	protected LayoutAlgorithm getAlgorithm() {
		return new HorizontalTreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

}
