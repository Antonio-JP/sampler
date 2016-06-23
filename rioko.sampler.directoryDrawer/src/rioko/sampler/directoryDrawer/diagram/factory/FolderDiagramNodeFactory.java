package rioko.sampler.directoryDrawer.diagram.factory;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;

import rioko.grapht.VertexFactory;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramNode;
import rioko.sampler.directoryDrawer.diagram.FolderProxyDiagramNode;

public class FolderDiagramNodeFactory implements VertexFactory<FolderDiagramNode>{

	@Override
	public FolderDiagramNode createVertex(Object... args) {
		if(args.length == 1 && (args[0] instanceof IResource)){
			return new FolderDiagramNode((IResource)args[0]);
		} else if(args.length == 2 && (args[0] instanceof IContainer) && (args[1] instanceof String)) {
			return new FolderDiagramNode((IContainer)args[0], (String)args[1]);
		} else if(args.length == 2 && (args[0] instanceof IResource) && (args[1] instanceof Boolean)) {
			if((Boolean)args[1]) {
				IResource resource = (IResource)args[0];
				if(resource instanceof IContainer) {
					return new FolderProxyDiagramNode(resource);
				} else {
					return new FolderDiagramNode(resource);
				}
			} else {
				return new FolderDiagramNode((IResource)args[0]);
			}
		} 
		
		return null;
	}

}
