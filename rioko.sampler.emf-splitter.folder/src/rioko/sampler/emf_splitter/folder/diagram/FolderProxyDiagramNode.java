package rioko.sampler.emf_splitter.folder.diagram;

import java.util.ArrayList;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.graphabstraction.diagram.StringAttribute;

public class FolderProxyDiagramNode extends FolderDiagramNode implements ProxyDiagramNode<IResource> {

	public FolderProxyDiagramNode(IResource resource) {
		super(resource);
	}

	public FolderProxyDiagramNode(IContainer parent, String name) {
		super(parent, name);
	}
	
	@Override
	public AbstractAttribute[] getDrawableData()
	{
		ArrayList<AbstractAttribute> attrs = new ArrayList<>();
		attrs.add(new StringAttribute("Directory", this.getProxyObject().getName()));
		
		return attrs.toArray(new AbstractAttribute[1]);
	} 
	
	@Override
	public IFigure getFigure(boolean showData) {
		return super.getFigure(showData, new Color(null, 232,159,255));
	}

	@Override
	public IResource getProxyObject() {
		return getResource();
	}
}
