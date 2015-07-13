package rioko.graphabstraction.layouts.bridge;

import java.util.Arrays;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.layouts.runtime.registers.RegisterLayoutAlgorithm;

public class ZestLayoutAlgorithm extends AbstractLayoutAlgorithm {

	private ZestLayoutBridge bridge = new ZestLayoutBridge(LayoutVertex.class);
	private LayoutAlgorithm currentAlgorithm = RegisterLayoutAlgorithm.getRegisteredAlgorithms().get(0);
	
	public ZestLayoutAlgorithm(int styles) {
		super(styles);
	}
	
	/* Getters & Setters */
	public void setLayoutAlgorithm(LayoutAlgorithm algorithm) {
		this.currentAlgorithm = algorithm;
	}

	/* AbstractLayoutAlgorithm methods */
	@Override
	protected void applyLayoutInternal(InternalNode[] nodes, InternalRelationship[] relations, double x, double y,
			double width, double height) {
		if(this.currentAlgorithm != null) {
			/* Apply the current algorithm to the nodes */
			this.bridge.createBridge(Arrays.asList(nodes), Arrays.asList((Object[])relations));
			this.bridge.applyLayout(this.currentAlgorithm, x,y,width, height);
			
			/* Move the nodes to their new positions */
			for(InternalNode node : nodes) {
				node.setInternalLocation(this.bridge.getX(node), this.bridge.getY(node));
				node.setLocation(this.bridge.getX(node), this.bridge.getY(node));
				node.setLocationInLayout(this.bridge.getX(node), this.bridge.getY(node));
				node.setDx(0); node.setDy(0);
			}
		}
	}

	//Useless methods
	@Override
	protected int getCurrentLayoutStep() {
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return 1;
	}

	@Override
	protected boolean isValidConfiguration(boolean arg0, boolean arg1) {
		return true;
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1) { }

	@Override
	protected void preLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1, double arg2, double arg3,
			double arg4, double arg5) { }

	@Override
	public void setLayoutArea(double arg0, double arg1, double arg2, double arg3) { }

}
