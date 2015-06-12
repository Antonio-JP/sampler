package rioko.drawmodels.filemanage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;
import org.eclipse.emf.ecore.xmi.XMIResource;
import org.eclipse.emf.ecore.xmi.XMLParserPool;
import org.eclipse.emf.ecore.xmi.XMLResource;
import org.eclipse.emf.ecore.xmi.impl.XMLParserPoolImpl;
import rioko.utilities.Log;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.diagram.XMIDiagram.ComposeXMIDiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.EmptyConnection;
import rioko.drawmodels.diagram.XMIDiagram.XMIDiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.XMIProxyDiagramNode;

public class XMIReader {
	
	public static final Class<? extends DiagramEdge<DiagramNode>> TYPEOFEDGES = EmptyConnection.class;
	public static final Class<? extends DiagramNode> TYPEOFNODES = XMIDiagramNode.class;
	public static final Class<? extends ComposeDiagramNode> TYPEOFCOMPOSE = ComposeXMIDiagramNode.class;
	
	private static HashMap<Object, XMIReader> Files = new HashMap<>();
	
	private IFile file = null;

	/**
	 * Campo que tendr� el recurso con el que trabajaremos, es decir, el modelo y el metamodelo
	 */
	private Resource resource = null;
	
	private ResourceSet resSet = null;
	
	private ModelDiagram model = null;
	
//	private int nNodes = 0;
	
	// Other fields
	private XMLParserPool parserPool = new XMLParserPoolImpl();
	private Map<IFile, Object> nameToFeatureMap = new HashMap<>();
	
	//Builders
	public XMIReader() {}
	
	private XMIReader(IFile file) throws IOException
	{
		//We set the file of this xmiReader as file
		this.file = file;
		
		//Create the ResourceSet associated to this Reader
		this.resSet = new ResourceSetImpl();
		
		//We create a caching map to URIs with their Resource
		((ResourceSetImpl)this.resSet).setURIResourceMap(new HashMap<>());
		
		//We create the Root resource
		this.resource = this.resSet.createResource(URI.createURI(file.getFullPath().makeAbsolute().toString()));
		resource.load(this.getDefaultLoadConfiguration());
			
		Files.put(file, this);
	}
	
	public static XMIReader getReaderFromFile(IFile file) throws IOException {
		if(!Files.containsKey(file)) {
			new XMIReader(file);
		}
		
		return Files.get(file);
	}

	
	public static void closeFile(IFile file) {
		if(Files.containsKey(file)) {
			XMIReader reader = Files.get(file);
		
			reader.resource.unload();
		}
	}

	public String getFileName() {
		return this.file.getName();
	}

	public EObject resolve(XMIProxyDiagramNode proxy) {
		Resource resolved = this.resSet.createResource(EcoreUtil.getURI(proxy.getProxyObject()).trimFragment());
		
		try {
			resolved.load(this.getDefaultLoadConfiguration());
			
			Iterator<EObject> iterator = EcoreUtil.getAllContents(resolved, false);
			if(iterator.hasNext()) {
				return iterator.next();
			}
		} catch(IOException e) {
			Log.exception(e);
			Log.print("Error in the model: bad reference");
		}
		
		return null;
//		return EcoreUtil.resolve(proxy.getProxyObject(), this.resSet);
	}
	
	public ModelDiagram getModel()
	{
		Log.xOpen("reading-file");
		if(this.model == null)
		{
			this.model = new ModelDiagram(TYPEOFEDGES, TYPEOFNODES, TYPEOFCOMPOSE);
			this.model.setXMIReader(this);
			
			Iterator<EObject> iterator = EcoreUtil.getAllContents(resource, false);
			
			int nNodes = 1;
			while(iterator.hasNext())
			{
				Log.xPrint("Working on node " + nNodes);
				nNodes++;
				this.processNode(iterator.next());
			}
		}
		
		Log.xClose("reading-file");
		return this.model;
	}
	
	public ModelDiagram getModel(XMIProxyDiagramNode proxy) {
		URI proxyUri = EcoreUtil.getURI(proxy.getProxyObject()).trimFragment();
		
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		IFile file = root.getFile(new Path(this.resSet.getURIConverter().normalize(proxyUri).toString().replaceFirst("resource/", "")));
		try {
			if(!file.exists()) {
				throw new IOException("Rioko ERROR: file not exist");
			}

			return XMIReader.getReaderFromFile(file).getModel();
		} catch (IOException e) {
			Log.exception(e);
			
			return null;
		}
	}
	
	public void processNode(DiagramNode node, EObject current) {
		DiagramNode inNode = this.getDiagramNodeFromDiagramNode(node);
		//First Case: the node already exist but was a proxy
		if((inNode instanceof XMIProxyDiagramNode) && !(node instanceof XMIProxyDiagramNode)) {
			this.model.changeNode((XMIProxyDiagramNode) inNode, node, current);
		} 
		//Second Case: the node does not exist in the model (it is a call from changeNode 
		else if(inNode == null) {
			model.addVertex(node);
				
			if(current != null) {
				//Conexiones de contenci�n
				EObject parentEObject = current.eContainer();
				DiagramNode parent = this.getDiagramNodeFromEObject(parentEObject);
				
				if(parent != null) {
					DiagramEdge<DiagramNode> edge = model.addEdge(parent, node);
					
					//El casting es v�lido porque as� hemos creado la arista
					EmptyConnection con = EmptyConnection.class.cast(edge);
					con.setType(typeOfConnection.CONTAINMENT);
						
					con.setEReference(this.getEReference(parentEObject, current));
				}
				
				//Recorremos los hijos de contenci�n
				Iterator<EObject> children = EcoreUtil.getAllContents(current, false);
				while(children.hasNext()) {
					this.processNode(children.next());
				}
				
				
				EClass eClass = current.eClass();
				for(EReference reference : eClass.getEReferences()) {
					if(!reference.isContainment() && !reference.isContainer()) {
						reference.setResolveProxies(false);
						Object object = current.eGet(reference, false);
						
						if(object == null) {
							Log.print("Null Object found");
						} else {
							if(object instanceof InternalEList) {
								@SuppressWarnings("rawtypes")
								List list = ((InternalEList) object).basicList();
								for(Object obj: list) {
									EObject eObject = (EObject)obj;
												this.processNode(eObject);
									DiagramNode rfNode = this.getDiagramNodeFromEObject(eObject);
									
									model.addEdge(node, rfNode).setType(typeOfConnection.REFERENCE);
								}
							} else if(object instanceof EList) {
								@SuppressWarnings("rawtypes")
								List list = (EList) object;
								for(Object obj: list) {
									EObject eObject = (EObject)obj;
												this.processNode(eObject);
									DiagramNode rfNode = this.getDiagramNodeFromEObject(eObject);
									
									model.addEdge(node, rfNode).setType(typeOfConnection.REFERENCE);
								}
							} else{
								EObject eObject = (EObject)object;
								this.processNode(eObject);
								DiagramNode rfNode = this.getDiagramNodeFromEObject(eObject);
								
								model.addEdge(node, rfNode).setType(typeOfConnection.REFERENCE);
							}
						}
					}
				}
			}
		}
 	}
	
	private void processNode(EObject current) {
		//Creamos el nodo, comprobando que no se haya introducido ya
		DiagramNode node = this.model.getModelDiagram().getVertexFactory().createVertex(current);
		
		this.processNode(node, current);
	}
	
	/**
	 * Private method to get the DiagramNode attached to a EObject of the model.
	 * 
	 * @param eObject we want to search
	 * @return DiagramNode with the EObject attached
	 */
	private DiagramNode getDiagramNodeFromEObject(EObject eObject) {
		DiagramNode node = this.model.getModelDiagram().getVertexFactory().createVertex(eObject);
		
		return this.getDiagramNodeFromDiagramNode(node);
	}
	
	private DiagramNode getDiagramNodeFromDiagramNode(DiagramNode node) {
		if(this.model.containsVertex(node)) {
			for(DiagramNode inNode : this.model.getModelDiagram().vertexSet()) {
				if(node.equals(inNode)) {
					return inNode;
				}
			}
		}
		
		return null;
	}
	
//	/**
//	 * Private method to get a Resource from an EObject of the model
//	 * 
//	 * @param eObject EObject we use as base object
//	 * @return Resource that contains the EObject
//	 */
//	private Resource getResourceFromEObject(EObject eObject) {
//		URI uriOfResource = EcoreUtil.getURI(eObject);
//		
//		return this.resSet.getResource(uriOfResource, false);
//	}
	
	private EReference getEReference(EObject parentEObject, EObject current) {
		EReference result = null;
		
		for(EReference ref : parentEObject.eClass().getEAllReferences())
		{
			Object refObj = parentEObject.eGet(ref);
			if(refObj != null) {
				if(((refObj instanceof List) && ((List<?>)refObj).contains(current)) ||
						refObj.equals(current)) {
					result = ref;
					break;
				}
			}
		}
		
		return result;
	}
	
	//Private methods	
	private Map<Object,Object> getDefaultLoadConfiguration() {
		if(this.resource == null || !(this.resource instanceof XMIResource)) {
			return null;
		} else {
			XMIResource resource = (XMIResource)this.resource;
			
			Map<Object,Object> options = resource.getDefaultLoadOptions();
			
			options.put(XMLResource.OPTION_DEFER_ATTACHMENT, Boolean.TRUE);
			options.put(XMLResource.OPTION_DEFER_IDREF_RESOLUTION, Boolean.TRUE);
			options.put(XMLResource.OPTION_USE_DEPRECATED_METHODS, Boolean.TRUE);
			options.put(XMLResource.OPTION_USE_PARSER_POOL, this.parserPool);
			options.put(XMLResource.OPTION_USE_XML_NAME_TO_FEATURE_MAP, this.nameToFeatureMap);
			
			return options;
		}
	}
	
	//Static methods
}
