package rioko.sampler.emf_splitter.folder.diagram;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;

import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.StringAttribute;
import rioko.grapht.VertexFactory;
import rioko.sampler.emf_splitter.folder.diagram.factory.FolderDiagramNodeFactory;

public class FolderDiagramNode extends DiagramNode {

	private IResource resource = null;
	
	//Builders
	public FolderDiagramNode() {}
	
	public FolderDiagramNode(IResource resource) {
		this.resource = resource;
		this.setLabel(resource.getClass().getSimpleName());
	}
	
	public FolderDiagramNode(IContainer parent, String name) {
		this(parent.getFile(parent.getLocation().append(name)));
	}
	
	//Getters & Setters
	IResource getResource() {
		return this.resource;
	}
	
	//DiagramNode needed methods
	@Override
	public VertexFactory<FolderDiagramNode> getVertexFactory() {
		return new FolderDiagramNodeFactory();
	}

	@Override
	public FolderDiagramNode copy() {
		return new FolderDiagramNode(this.resource);
	}

	@Override
	protected AbstractAttribute[] getNonDrawableData() {
		return new AbstractAttribute[0];
	}

	@Override
	public AbstractAttribute[] getDrawableData() {
		if(this.resource != null) {
			AbstractAttribute[] result = new AbstractAttribute[1];
			
			result[0] = new StringAttribute("Name",resource.getName());
			
			return result;
		} else {
			return new AbstractAttribute[0];
		}
	}

	//Equality methods
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof FolderDiagramNode) {
			FolderDiagramNode other = (FolderDiagramNode)ob;
			if(this.resource == null || other.resource == null) {
				return this.resource == other.resource;
			}
			
			return this.resource.getLocation().toOSString().equals(other.resource.getLocation().toOSString());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.resource.getLocation().toOSString().hashCode();
	}
}
