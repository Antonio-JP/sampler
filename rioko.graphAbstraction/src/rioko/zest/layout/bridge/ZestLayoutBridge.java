package rioko.zest.layout.bridge;

import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.layouts.bridge.AbstractLayoutBridge;
import rioko.layouts.graphImpl.LayoutVertex;

public class ZestLayoutBridge extends AbstractLayoutBridge<InternalNode, LayoutVertex> {

	public ZestLayoutBridge(Class<LayoutVertex> layoutVertexClass) {
		super(layoutVertexClass);
	}

	//AbstractLayoutBridge methods
	@Override
	public LayoutVertex parseVertex(InternalNode vertex) {
		return new LayoutVertex(vertex.getWidthInLayout(), vertex.getHeightInLayout());
	}

	@Override
	public boolean parseEdge(Object obj) {
		if(obj instanceof InternalRelationship) {
			LayoutVertex source = this.getLayoutVertex(((InternalRelationship)obj).getSource());
			LayoutVertex target = this.getLayoutVertex(((InternalRelationship)obj).getDestination());
			
			this.graph.addEdge(source, target);
			return true;
		}
		
		return false;
	}

}
