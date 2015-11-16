package rioko.drawmodels.diagram;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rioko.drawmodels.filemanage.Reader;
import rioko.eclipse.registry.RegistryManagement;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.utilities.Pair;



/**
 * Class with the structure and methods to use a Diagram of a Model
 * 
 * @author Antonio
 *
 */
public abstract class ModelDiagram<T> implements IEditorInput{
	private static final String ID_DIAGRAM_EXTENSION = "rioko.drawmodels.diagrams";
	
	private DiagramGraph graph;
	
	private DiagramGraph printable;
	
	private Reader<T> reader = null;
	
	private IdParser parser = null;
	
	//Static generic builder for a ModelDiagram
	/**
	 * This method create a new ModelDiagram using the type of the elements contained inside.
	 * 
	 * @param graph The DiagramGraph used to create the ModelDiagram
	 * 
	 * @return The new ModelDiagram based in the Graph argument
	 * 
	 * @throws IllegalArgumentException if there is no ModelDiagram valid for this Graph
	 */
	public static ModelDiagram<?> getModelDiagramForGraph(DiagramGraph graph) throws IllegalArgumentException{
		
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(ID_DIAGRAM_EXTENSION);
		ArrayList<IConfigurationElement> possibleModels = new ArrayList<>();
		
		for(IConfigurationElement element : elements) {
			boolean valid = true;
			
			try {
				valid &= element.createExecutableExtension("node").getClass().isAssignableFrom(graph.getVertexClass());
				valid &= element.createExecutableExtension("edge").getClass().isAssignableFrom(graph.getEdgeClass());
				valid &= element.createExecutableExtension("compose").getClass().isAssignableFrom(graph.getComposeClass());
			} catch (InvalidRegistryObjectException | CoreException e) {
				throw new IllegalArgumentException("A class from " + element + " is not accesible");
			}
			
			if(valid) {
				possibleModels.add(element);
			}
		}
		
		if(possibleModels.isEmpty()) {
			throw new IllegalArgumentException("There is no possible model registered for the graph");
		} else {
			try {
				ModelDiagram<?> fooModel = (ModelDiagram<?>) possibleModels.get(0).createExecutableExtension("diagram");
				//We use the Constructor with the IFile parameter and throw and exception if it does not exist
				return fooModel.getClass().getConstructor(DiagramGraph.class).newInstance(graph);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | CoreException e) {
				throw new IllegalArgumentException("Reader " + possibleModels.get(0).getAttribute("diagram") + " not have a simple DiagramGraph Constructor or a empty Constructor");
			}
		}
	}
	
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

	public void setReader(Reader<T> reader) {
		this.reader = reader;
	}
	
	public IdParser getIdParser() {
		if(this.parser == null) {
			//Get the extension diagram where this ModelDiagram has been taken.
			IConfigurationElement[] elements = RegistryManagement.getElementsFor(ID_DIAGRAM_EXTENSION);
			for(IConfigurationElement conf : elements) {
				if(this.getClass().isInstance(RegistryManagement.getInstance(conf,"diagram"))) {
					//Once found, instantiate a new IdParser for that diagram
					this.parser = (IdParser) RegistryManagement.getInstance(conf, "parser");
					break;
				}
			}
		}
		
		return this.parser;
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
	public abstract void buildMetaData();
	public abstract Object getMetaData();
	
	//Proxy methods
	public boolean resolveProxy(ProxyDiagramNode<?> proxy) {
		ProxyDiagramNode<T> tProxy = this.castProxy(proxy);
		
		if(this.graph.containsVertex((DiagramNode)tProxy)) {
			T resolved = this.reader.resolve(tProxy);
			
			if(resolved == null || resolved == tProxy.getProxyObject()) {
				return false;
			}
			
			DiagramNode newNode = this.graph.getVertexFactory().createVertex(resolved);

			this.changeNode(tProxy, newNode, resolved);
			
			return true;
		} else {
			return false;
		}
	}

	public ModelDiagram<T> getModelFromProxy(ProxyDiagramNode<?> proxy) throws IllegalArgumentException {
		ProxyDiagramNode<T> tProxy = this.castProxy(proxy);
		
		if(this.containsVertex((DiagramNode)tProxy)) {
			return this.reader.getModel(tProxy);
		}
		
		return null;
	}
	
	public void changeNode(ProxyDiagramNode<T> proxy, DiagramNode resolved, T object) {
		ArrayList<Pair<typeOfConnection, DiagramNode>> connectionsTo = new ArrayList<>();
		ArrayList<Pair<typeOfConnection, DiagramNode>> connectionsFrom = new ArrayList<>();
		
		for(DiagramEdge<DiagramNode> edge : this.graph.edgesOf((DiagramNode)proxy)) {
			if(edge.getTarget().equals(proxy)) {
				connectionsTo.add(new Pair<>(edge.getType(), edge.getSource()));
			} else {
				connectionsFrom.add(new Pair<>(edge.getType(), edge.getTarget()));
			}
		}
		
		//Remove the previous vertex from the graph
		this.graph.removeVertex((DiagramNode)proxy);
		
		//Add the new resolved node to the graph
		resolved.setId(((DiagramNode)proxy).getId());
		this.reader.processNode(resolved, object);

		//Adding the in-edges
		for(Pair<typeOfConnection,DiagramNode> nodeTo : connectionsTo) {
			this.graph.addEdge(nodeTo.getLast(), resolved).setType(nodeTo.getFirst());
		}
		
		//Adding the out-edges
		for(Pair<typeOfConnection,DiagramNode> nodeTo : connectionsFrom) {
			this.graph.addEdge(resolved, nodeTo.getLast()).setType(nodeTo.getFirst());
		}
	}
	
	private ProxyDiagramNode<T> castProxy(ProxyDiagramNode<?> proxy) throws IllegalArgumentException {
		Class<?> modelClass = (Class<?>) ((ParameterizedType) this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		Class<?> proxyClass = (Class<?>) ((ParameterizedType) proxy.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if(modelClass.equals(proxyClass)) {
			throw new IllegalArgumentException("The parmeters of the model and proxy are not the same: " + modelClass.getSimpleName() + " vs " + proxyClass.getSimpleName());
		}
		
		@SuppressWarnings("unchecked")
		ProxyDiagramNode<T> tProxy = (ProxyDiagramNode<T>)proxy;
		return tProxy;
	}

	//IEditorInput methods
	//Todos están vacíos porque no quiero nada de ellos
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) { return null; }

	@Override
	public boolean exists() { return (this.graph!=null || this.reader != null); }

	@Override
	public ImageDescriptor getImageDescriptor() { return null; }

	private String customName = null;
	@Override
	public String getName() { 
		if(!(customName == null)) {
			return this.customName;
		} else if(this.reader == null) {
			return "ZestEditor";
		} else {
			return this.reader.getFileName();
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
