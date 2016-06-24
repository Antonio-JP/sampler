package rioko.sampler.directoryDrawer.diagram;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.StringAttribute;
import rioko.grapht.VertexFactory;
import rioko.sampler.directoryDrawer.diagram.factory.FolderDiagramNodeFactory;

public class FolderDiagramNode extends DiagramNode {

	private static final Color DEFAULT_COLOR = new Color(null, 253,255,183);
	private static final Color FILE_COLOR = new Color(null, 166,249,255);
	private static final Color FOLDER_COLOR = new Color(null, 71,226,118);
	private static final Color PROJECT_COLOR = new Color(null, 250,99,99);

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
	public IResource getResource() {
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
	
	@Override
	public IFigure getFigure(boolean showData) {
		if(this.resource instanceof IFile) {
			return super.getFigure(showData, FILE_COLOR);
		} else if(this.resource instanceof IFolder) {
			return super.getFigure(showData, FOLDER_COLOR);
		} else if(this.resource instanceof IProject) {
			return super.getFigure(showData, PROJECT_COLOR);
		} else {
			return super.getFigure(showData, DEFAULT_COLOR);
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
