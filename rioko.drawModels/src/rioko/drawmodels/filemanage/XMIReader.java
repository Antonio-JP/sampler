package rioko.drawmodels.filemanage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
	
	private static HashMap<IFile, XMIReader> Files = new HashMap<>();

	/**
	 * Campo que tendrá el recurso con el que trabajaremos, es decir, el modelo y el metamodelo
	 */
	private Resource resource = null;
	
	private ResourceSet resSet = null;
	
	private ModelDiagram model = null;
	
	private int nNodes = 0;
	
	// Other fields
	private XMLParserPool parserPool = new XMLParserPoolImpl();
	private Map<Object, Object> nameToFeatureMap = new HashMap<>();
	
	//Builders
	public XMIReader() {}
	
	private XMIReader(IFile file) throws IOException
	{
		//Create the ResourceSet associated to this Reader
		this.resSet = new ResourceSetImpl();
		
		//We create a caching map to URIs with their Resource
		((ResourceSetImpl)this.resSet).setURIResourceMap(new HashMap<>());
		
		this.resource = this.resSet.createResource(URI.createURI(file.getFullPath().makeAbsolute().toString()));
		//AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(this.getAdapterFactory(),new BasicCommandStack());
		
//		if(res instanceof XMIResource) {
//			String uri = file.getFullPath().makeAbsolute().toString();
	
	//		resource = resSet.createResource(uri);
	//		resource = domain.createResource(uri);
	//		resource = domain.createResource(URI.createFileURI(file.getFullPath().toOSString()).toFileString());
			resource.load(this.getDefaultLoadConfiguration());
//		}
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

	public EObject resolve(XMIProxyDiagramNode proxy) {
//		resSet.createResource(EcoreUtil.getURI(proxy.getProxyObject()));
		
		return EcoreUtil.resolve(proxy.getProxyObject(), this.resSet);
	}
	
	public ModelDiagram getModel()
	{
		Log.xOpen("reading-file");
		if(this.model == null)
		{
			this.model = new ModelDiagram(TYPEOFEDGES, TYPEOFNODES, TYPEOFCOMPOSE);
			this.model.setXMIReader(this);
			
			HashMap<EObject, DiagramNode> map = new HashMap<>();
			
			Iterator<EObject> iterator = EcoreUtil.getAllContents(resource, false);
			EObject current = null;
			
			int nNodes = 1;
			while(iterator.hasNext())
			{
				current = iterator.next();
				Log.xPrint("Working on node " + nNodes);
				nNodes++;
				//Creamos el nodo, comprobando que no se haya introducido ya
				DiagramNode node = map.get(current);
				if(node == null) {
					node = this.createXMIDiagramNode(current);
					map.put(current, node);
				
					model.addVertex(node);
				}
				
				//Conexiones de contención
				EObject parentEObject = current.eContainer();
				DiagramNode parent = map.get(parentEObject);
				
				if(parent != null) {
					DiagramEdge<DiagramNode> edge = model.addEdge(parent, node);
					
					//El casting es válido porque así hemos creado la arista
					EmptyConnection con = EmptyConnection.class.cast(edge);
					con.setType(typeOfConnection.CONTAINMENT);
						
					con.setEReference(this.getEReference(parentEObject, current));
					
				}
				
				//Conexiones de referencias
				for(EObject  ref: current.eCrossReferences())
				{
					DiagramNode rfNode = map.get(ref);
					
					if(rfNode == null)
					{
						rfNode = this.createXMIDiagramNode(ref);
						map.put(ref, rfNode);
					}
					
					if(!this.model.containsVertex(rfNode)) {
						this.model.addVertex(rfNode);
					}
					
					model.addEdge(node, rfNode).setType(typeOfConnection.REFERENCE);
				}
			}
		}
		
		Log.xClose("reading-file");
		return this.model;
	}
	
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
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private DiagramNode createXMIDiagramNode(EObject obj)
	{
		
		DiagramNode node = this.model.getModelDiagram().getVertexFactory().createVertex(obj);
		node.setId(this.nNodes);
		this.nNodes++;
		
		return node;
	}
	
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
}
