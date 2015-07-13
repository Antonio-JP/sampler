package rioko.layouts.wrapper.zest.impl;

import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;

import rioko.layouts.wrapper.zest.ZestLayoutWrapper;

public class HorizontalShiftZestLayout extends ZestLayoutWrapper {

	@Override
	protected LayoutAlgorithm getAlgorithm() {
		return new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}

}
