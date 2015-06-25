package rioko.zest.layout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class ComposeCustomLayoutAlgorithm extends CustomLayoutAlgorithm {

	private List<CustomLayoutAlgorithm> layouts = new ArrayList<>();
	
	public ComposeCustomLayoutAlgorithm(int styles, List<CustomLayoutAlgorithm> algorithms) {
		super(styles);

		for(CustomLayoutAlgorithm algorithm : algorithms) {
			this.addAlgorithm(algorithm);
		}
	}
	
	//List Modifiers methods (just subclasses)
	protected void addAlgorithm(CustomLayoutAlgorithm algorithm) {
		CustomLayoutAlgorithm toAdd = algorithm.copy();
		toAdd.setInitialMove(false);
		
		this.layouts.add(toAdd);
	}

	@Override
	public CustomLayoutAlgorithm copy() {
		return new ComposeCustomLayoutAlgorithm(this.getStyle(), this.layouts);
	}

	@Override
	protected void preparation(InternalNode[] nodes, InternalRelationship[] relations) {
		// Do nothing

	}

	@Override
	protected void running(InternalNode[] nodes, InternalRelationship[] relations) {
		Rectangle area = this.getLayoutArea();

		double x = area.getLeft(), y = area.getTop(), width = area.getWidth(), height = area.getHeight();
		
		for(CustomLayoutAlgorithm algorithm : this.layouts) {
			try {
				algorithm.applyLayout(nodes, relations, x, y, width, height, false, false);
			} catch (InvalidLayoutConfiguration e) {
				// Impossible Exception
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void adjust(InternalNode[] nodes, InternalRelationship[] relations) {
		//Do nothing
	}

}
