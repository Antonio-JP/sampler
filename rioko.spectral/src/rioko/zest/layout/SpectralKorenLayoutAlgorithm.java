package rioko.zest.layout;

import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class SpectralKorenLayoutAlgorithm extends CustomLayoutAlgorithm {

	public SpectralKorenLayoutAlgorithm(int styles) {
		super(styles);
	}

	//Copiable methods
	@Override
	public SpectralKorenLayoutAlgorithm copy() {
		return new SpectralKorenLayoutAlgorithm(this.getStyle());
	}

	//CustomLayoutAlgorithmMethods
	@Override
	protected void preparation(InternalNode[] nodes, InternalRelationship[] relations) {
		//Do nothing
	}

	@Override
	protected void running(InternalNode[] nodes, InternalRelationship[] relations) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void adjust(InternalNode[] nodes, InternalRelationship[] relations) {
		//Do nothing
	}


}
