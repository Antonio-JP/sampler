package rioko.drawmodels.filemanage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.emf.common.command.BasicCommandStack;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;

import rioko.utilities.Log;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.AbstractAttribute;
import rioko.drawmodels.diagram.XMIDiagram.ComposeXMIDiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.EmptyConnection;
import rioko.drawmodels.diagram.XMIDiagram.StringAttribute;
import rioko.drawmodels.diagram.XMIDiagram.XMIDiagramNode;

public class XMIReader {
	
	public static final Class<? extends DiagramEdge<DiagramNode>> TYPEOFEDGES = EmptyConnection.class;
	public static final Class<? extends DiagramNode> TYPEOFNODES = XMIDiagramNode.class;
	public static final Class<? extends ComposeDiagramNode> TYPEOFCOMPOSE = ComposeXMIDiagramNode.class;
	
	private static HashMap<IFile, XMIReader> Files = new HashMap<>();

	/**
	 * Campo que tendrá el recurso con el que trabajaremos, es decir, el modelo y el metamodelo
	 */
	private Resource resource = null;
	
	private AdapterFactory adapterFactory = null;
	
	private DiagramGraph graph = null;
	
	private int nNodes = 0;
	
	public XMIReader() {}
	
	private XMIReader(IFile file) throws IOException
	{
		// TODO Mejorar el sistema de lectura de los modelos
		AdapterFactoryEditingDomain domain = new AdapterFactoryEditingDomain(this.getAdapterFactory(),new BasicCommandStack());
		
		resource = domain.createResource(file.getFullPath().toString());
			
		resource.load(null);
		
		Files.put(file, this);
	}
	
	public static XMIReader getReaderFromFile(IFile file) throws IOException {
		if(!Files.containsKey(file)) {
			new XMIReader(file);
		}
		
		return Files.get(file);
	}
	
	public DiagramGraph getDiagram()
	{
		Log.xOpen("reading-file");
		if(this.graph == null)
		{
			this.graph = new DiagramGraph(TYPEOFEDGES, TYPEOFNODES, TYPEOFCOMPOSE);
			
			HashMap<EObject, XMIDiagramNode> map = new HashMap<>();
			
			Iterator<EObject> iterator = resource.getAllContents();
			EObject current = null;
			
			int nNodes = 1;
			while(iterator.hasNext())
			{
				current = iterator.next();
				Log.xPrint("Iterando sobre el nodo " + nNodes);
				nNodes++;
				//Creamos el nodo, comprobando que no se haya introducido ya
				XMIDiagramNode node = map.get(current);
				if(node == null) {
					node = this.createXMIDiagramNode(current);
					map.put(current, node);
				
					graph.addVertex(node);
				}
				
				//Conexiones de contención
				EObject parentEObject = current.eContainer();
				XMIDiagramNode parent = map.get(parentEObject);
				
				if(parent != null) {
					DiagramEdge<DiagramNode> edge = graph.addEdge(parent, node);
					
					//El casting es válido porque así hemos creado la arista
					EmptyConnection con = EmptyConnection.class.cast(edge);
					con.setType(typeOfConnection.CONTAINMENT);
						
					con.setEReference(this.getEReference(parentEObject, current));
					
				}
				
				//Conexiones de referencias
				for(EObject  ref: current.eCrossReferences())
				{
					XMIDiagramNode rfNode = map.get(ref);
					
					if(rfNode == null)
					{
						rfNode = this.createXMIDiagramNode(ref);
						map.put(ref, rfNode);
					}
					
					if(!this.graph.containsVertex(rfNode)) {
						this.graph.addVertex(rfNode);
					}
					
					DiagramEdge<DiagramNode> edge = graph.addEdge(node, rfNode);
					
					//El casting es válido porque así hemos creado la arista
					EmptyConnection con = EmptyConnection.class.cast(edge);
					con.setType(typeOfConnection.REFERENCE);
					
					con.setEReference(this.getEReference(current, ref));
				}
			}
		}
		
		Log.xClose("reading-file");
		return this.graph;
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

	//Protected methods
	/**
	 * Return an ComposedAdapterFactory for all registered models
	 * 
	 * @return a ComposedAdapterFactory
	 */
	protected AdapterFactory getAdapterFactory() {
		if (adapterFactory == null) {
			adapterFactory = new ComposedAdapterFactory(ComposedAdapterFactory.Descriptor.Registry.INSTANCE);
		}
		return adapterFactory;
	}
	
	//Private methods
	/**
	 * 
	 * @param obj
	 * @return
	 */
	private XMIDiagramNode createXMIDiagramNode(EObject obj)
	{
		EList<EAttribute> eAllAttributes = obj.eClass().getEAllAttributes();
		ArrayList<AbstractAttribute> attrs = new ArrayList<>();
		for(EAttribute attr : eAllAttributes) {
			attrs.add(new StringAttribute(attr.getName(), XMIReader.getStringFromData(obj.eGet(attr))));
		}
		
		XMIDiagramNode xmiNode = new XMIDiagramNode(obj.eClass(),attrs);
		xmiNode.setId(this.nNodes);
		this.nNodes++;
		
		return xmiNode;
	}
	
	/**
	 * 
	 * @param data
	 * @return
	 */
	private static String getStringFromData(Object data)
	{
		if(data == null)
		{
			return "null";
		}
		
		return data.toString();
	}
}
