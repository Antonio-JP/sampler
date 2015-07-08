package rioko.drawmodels.diagram;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rioko.drawmodels.diagram.XMIDiagram.XMIDiagramNode;
import rioko.drawmodels.diagram.XMIDiagram.XMIProxyDiagramNode;
import rioko.drawmodels.filemanage.XMIReader;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Pair;



/**
 * Class with the structure and methods to use a Diagram of a Model
 * 
 * @author Antonio
 *
 */
public class ModelDiagram implements IEditorInput{
	private DiagramGraph graph;
	
	private DiagramGraph printable;
	
	private XMIReader xmiReader = null;
	
	//Builders
	public ModelDiagram()
	{
		this.graph = new DiagramGraph();
		
		this.printable = null;
	}
	
	public ModelDiagram(Class<? extends DiagramEdge<DiagramNode>> edgeClass, Class<? extends DiagramNode> vertexClass, Class<? extends ComposeDiagramNode> composeClass)
	{
		this.graph = new DiagramGraph(edgeClass, vertexClass, composeClass);
		
		this.printable = this.graph;
	}
	
//	public ModelDiagram(XMIReader xmiReader) throws IOException
//	{
//		this.xmiReader = xmiReader;
//		
//		this.graph = xmiReader.getDiagram();
//		
//		this.printable = this.graph;
//	}
	
	public ModelDiagram(DiagramGraph graph) 
	{
		this.graph = graph;
		
		this.printable = this.graph;
	}
		
	//Getters & Setters
	public DiagramGraph getModelDiagram()
	{
		return this.graph;
	}
	
	public DiagramGraph getPrintDiagram()
	{
		return this.printable;
	}
	
	public void setPrintDiagram(DiagramGraph printable)
	{
		this.printable = printable;
	}

	public void setXMIReader(XMIReader xmiReader) {
		this.xmiReader = xmiReader;
	}
	
	//Diagram Gestion methods
	private int nextId = 1;
	public boolean addVertex(DiagramNode node) {
		if(this.graph.addVertex(node)) {
			if(node.getId() == -1) {
				node.setId(nextId);
				nextId++;
			}
			
			return true;
		}
		
		return false;
	}
	
	public DiagramEdge<DiagramNode> addEdge(DiagramNode source, DiagramNode target) {
		return this.graph.addEdge(source, target);
	}
	
	public boolean containsVertex(DiagramNode node) {
		return this.graph.containsVertex(node);
	}
	
	//Meta-model analysis methods
	private Set<EClass> eClassList = null;
	private Set<String> eClassNames = null;
	public Set<EClass> getEClassList() {
		if(this.eClassList == null) {
			this.eClassList = new HashSet<EClass>();
			
			if(this.graph != null) {
				for(DiagramNode node : this.graph.vertexSet()) {
					if(node instanceof XMIDiagramNode) {
						this.eClassList.add(((XMIDiagramNode) node).getEClass());
					}
				}
			}
		}
		
		return this.eClassList;
	}
	
	public Set<String> getEClassListNames() {
		if(this.eClassNames == null) {
			if(this.eClassList == null) {
				this.getEClassList();
			}
			
			this.eClassNames = new HashSet<String>();
			
			for(EClass eClass : this.eClassList) {
				this.eClassNames.add(eClass.getName());
			}
		}
		
		return this.eClassNames;
	}
	
	//Proxy methods
	public boolean resolveProxy(XMIProxyDiagramNode proxy) {
		if(this.graph.containsVertex(proxy)) {
			EObject resolved = this.xmiReader.resolve(proxy);
			
			if(resolved == null || resolved == proxy.getProxyObject()) {
				return false;
			}
			
			DiagramNode newNode = this.graph.getVertexFactory().createVertex(resolved);

			this.changeNode(proxy, newNode, resolved);
			
			return true;
		} else {
			return false;
		}
	}

	public ModelDiagram getModelFromProxy(XMIProxyDiagramNode proxy) {
		if(this.containsVertex(proxy)) {
			return this.xmiReader.getModel(proxy);
		}
		
		return null;
	}
	
	public void changeNode(XMIProxyDiagramNode proxy, DiagramNode resolved, EObject eObject) {
		ArrayList<Pair<typeOfConnection, DiagramNode>> connectionsTo = new ArrayList<>();
		ArrayList<Pair<typeOfConnection, DiagramNode>> connectionsFrom = new ArrayList<>();
		
		for(DiagramEdge<DiagramNode> edge : this.graph.edgesOf(proxy)) {
			if(edge.getTarget().equals(proxy)) {
				connectionsTo.add(new Pair<>(edge.getType(), edge.getSource()));
			} else {
				connectionsFrom.add(new Pair<>(edge.getType(), edge.getTarget()));
			}
		}
		
		//Remove the previous vertex from the graph
		this.graph.removeVertex(proxy);
		
		//Add the new resolved node to the graph
		resolved.setId(proxy.getId());
		this.xmiReader.processNode(resolved, eObject);

		//Adding the in-edges
		for(Pair<typeOfConnection,DiagramNode> nodeTo : connectionsTo) {
			this.graph.addEdge(nodeTo.getLast(), resolved).setType(nodeTo.getFirst());
		}
		
		//Adding the out-edges
		for(Pair<typeOfConnection,DiagramNode> nodeTo : connectionsFrom) {
			this.graph.addEdge(resolved, nodeTo.getLast()).setType(nodeTo.getFirst());
		}
	}

	//IEditorInput methods
	//Todos están vacíos porque no quiero nada de ellos
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) { return null; }

	@Override
	public boolean exists() { return (this.graph!=null || this.xmiReader != null); }

	@Override
	public ImageDescriptor getImageDescriptor() { return null; }

	private String customName = null;
	@Override
	public String getName() { 
		if(!(customName == null)) {
			return this.customName;
		} else if(this.xmiReader == null) {
			return "ZestEditor";
		} else {
			return this.xmiReader.getFileName();
		}
	}
	
	public void setName(String name) {
		this.customName = new String(name);
	}

	@Override
	public IPersistableElement getPersistable() { return null; }

	@Override
	public String getToolTipText() { return "SAMPLER: " + this.getName(); }

	
}
