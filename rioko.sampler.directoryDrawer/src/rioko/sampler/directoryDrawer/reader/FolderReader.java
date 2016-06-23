package rioko.sampler.directoryDrawer.reader;

import java.io.IOException;
import java.util.HashMap;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.filemanage.GeneralReader;
import rioko.drawmodels.filemanage.Reader;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.sampler.directoryDrawer.FolderModelDiagram;
import rioko.sampler.directoryDrawer.diagram.ComposeFolderDiagramNode;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramEdge;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramNode;
import rioko.sampler.directoryDrawer.diagram.FolderProxyDiagramNode;
import rioko.utilities.Log;

public class FolderReader implements Reader<IResource> {
	private static final Class<? extends DiagramEdge<DiagramNode>> TYPEOFEDGES = FolderDiagramEdge.class;
	private static final Class<? extends DiagramNode> TYPEOFNODES = FolderDiagramNode.class;
	private static final Class<? extends ComposeDiagramNode> TYPEOFCOMPOSE = ComposeFolderDiagramNode.class;
	
	private static HashMap<Object, FolderReader> Files = new HashMap<>();
	
	private IResource root = null;
	private FolderModelDiagram model = null;
	
	//Builders
	public FolderReader() {}
	
	public FolderReader(IResource root) throws IOException
	{
		//We set the file of this xmiReader as file
		this.root = root;
		
		//We create the Root resource			
		Files.put(root, this);
	}
	
	//Reader methods
	@Override
	public String getFileName() {
		return this.root.getName();
	}
	@Override
	public IResource resolve(ProxyDiagramNode<IResource> proxy) {
		return this.copyResource(proxy.getProxyObject());
	}
	private IResource copyResource(IResource resource) {
		IContainer parent = resource.getParent();

		IPath path = new Path("./" + resource.getName());
		return (resource instanceof IFolder) ? parent.getFolder(path) : parent.getFile(path);
	}

	@Override
	public ModelDiagram<IResource> getModel() {
		Log.xOpen("reading-file");
		if(this.model == null)
		{
			this.model = new FolderModelDiagram(TYPEOFEDGES, TYPEOFNODES, TYPEOFCOMPOSE);
			this.model.setReader(this);
			
			this.processNode(this.model.getModelDiagram().getVertexFactory().createVertex(root), root);
		}
		
		return this.model;
	}
	
	@Override
	public ModelDiagram<IResource> getModel(ProxyDiagramNode<IResource> proxy) {
		IPath path = proxy.getProxyObject().getLocation();
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = root.getFile(path);
		try {
			return (FolderModelDiagram)GeneralReader.getReaderFromFile(file, this.getClass()).getModel();
		} catch (IOException e) {
			Log.exception(e);
			
			return null;
		}
	}
	@Override
	public void processNode(DiagramNode node, IResource current) {
		DiagramNode inNode = this.getDiagramNodeFromDiagramNode(node);
		//First Case: the node already exist but was a proxy
		if((inNode instanceof FolderProxyDiagramNode) && !(node instanceof FolderProxyDiagramNode)) {
			this.model.changeNode((FolderProxyDiagramNode) inNode, node, current);
		} 
		//Second Case: the node does not exist in the model (it is a call from changeNode) 
		else if(inNode == null) {
			model.addVertex(node);
				
			if((current != null)) {
				//Conexiones de contención
				IContainer parentObject = current.getParent();
				DiagramNode parent = this.getDiagramNodeFromResource(parentObject);
				
				if(parent != null) {
					DiagramEdge<DiagramNode> edge = model.addEdge(parent, node);
					
					//El casting es válido porque así hemos creado la arista
					FolderDiagramEdge con = FolderDiagramEdge.class.cast(edge);
					con.setType(typeOfConnection.CONTAINMENT);
				}
				
				//Recorremos los hijos de contención
				if(!(node instanceof FolderProxyDiagramNode) && (current instanceof IContainer)) {
					IContainer currentContainer = (IContainer)current;
					try {
						for(IResource res : currentContainer.members()) {
							this.processNode(res);
						}
					} catch (CoreException e) {
						Log.exception(e);
						return;
					}
				}
			}
		}
	}
	
	private DiagramNode getDiagramNodeFromResource(IResource resource) {
		FolderDiagramNode node = (FolderDiagramNode) this.model.getModelDiagram().getVertexFactory().createVertex(resource);
		return this.getDiagramNodeFromDiagramNode(node);
	}

	private void processNode(IResource resource) {
		this.processNode(this.model.getModelDiagram().getVertexFactory().createVertex(resource, true), resource);
	}

	private DiagramNode getDiagramNodeFromDiagramNode(DiagramNode node) {
		if(node instanceof FolderDiagramNode) {
			for(DiagramNode inNode : this.model.getModelDiagram().vertexSet()) {
				if(inNode.equals(node)) {
					return inNode;
				}
			}
		}
		
		return null;
	}
		
}
