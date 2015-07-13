package rioko.layouts.wrapper.zest.impl;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.RadialLayoutAlgorithm;

import rioko.layouts.wrapper.zest.ZestLayoutWrapper;

public class RadialZestLayout extends ZestLayoutWrapper {

	@Override
	protected LayoutAlgorithm getAlgorithm() {
		return new RadialLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

}
