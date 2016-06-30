package rioko.gmldrawer.gmlReader;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import rioko.drawmodels.filemanage.Reader;
import rioko.gmldrawer.gmlDiagram.ComposeGMLDiagramNode;
import rioko.gmldrawer.gmlDiagram.GMLDiagramEdge;
import rioko.gmldrawer.gmlDiagram.GMLDiagramNode;
import rioko.gmldrawer.GMLModelDiagram;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.utilities.Log;

public class GMLReader implements Reader<Node> {
	
	private static final Class<? extends DiagramEdge<DiagramNode>> TYPEOFEDGES = GMLDiagramEdge.class;
	private static final Class<? extends DiagramNode> TYPEOFNODES = GMLDiagramNode.class;
	private static final Class<? extends ComposeDiagramNode> TYPEOFCOMPOSE = ComposeGMLDiagramNode.class;

	private IFile file = null;
	
	private GMLModelDiagram model = null;
	
	private DocumentBuilderFactory dbFactory;
	private DocumentBuilder dBuilder;
	private Document doc;
	private Element rootElement;
	
	//Builders
	public GMLReader() { }
	
	public GMLReader(IResource file) {
		if(!(file instanceof IFile)) {
			throw new RuntimeException("Invalid resource for GMLReader");
		}
		this.file = (IFile)file;
		
		//Open the file with DOM xml parser
		try {
			dbFactory = DocumentBuilderFactory.newInstance();
			dBuilder = dbFactory.newDocumentBuilder();
			doc = dBuilder.parse(new File(file.getLocation().toOSString()));
			
			doc.getDocumentElement().normalize();
		} catch (ParserConfigurationException | SAXException | IOException e) {
			Log.exception(e);
		}
		
		rootElement = doc.getDocumentElement();
	}
	
	//Methods of Reader interface
	@Override
	public String getFileName() {
		if(this.file != null) {
			return this.file.getName();
		} else {
			return "No File read";
		}
	}

	@Override
	public Node resolve(ProxyDiagramNode<Node> proxy) {
		//GraphML does not support Proxies
		return null;
	}

	@Override
	public GMLModelDiagram getModel() {
		if(this.model == null) {
			this.model = new GMLModelDiagram(TYPEOFEDGES, TYPEOFNODES, TYPEOFCOMPOSE);
			this.model.setReader(this);
			
			NodeList listOfGraphs = this.rootElement.getElementsByTagName("graph");
			for(int i = 0; i < listOfGraphs.getLength(); i++) {
				Node currentGraph = listOfGraphs.item(i);
				if(currentGraph.getNodeType() == Node.ELEMENT_NODE) {
					Element graph = (Element)currentGraph;

					//Processing the directed attribute
					boolean isDirected = true;

					String directed = graph.getAttribute("edgedefault");
					if(directed.equals("undirected")) {
						isDirected=false;
					}
					
					//Processing the vertices of the graph
					HashMap<String, GMLDiagramNode> mapToCurrentNodes = new HashMap<>();
				
					NodeList listOfVertices = graph.getElementsByTagName("node");
					for(int j = 0; j < listOfVertices.getLength(); j++) {
						Node currentNode = listOfVertices.item(j);
						if(currentNode.getNodeType() == Node.ELEMENT_NODE) {
							Element node = (Element)currentNode;
							GMLDiagramNode inNode = (GMLDiagramNode) this.model.getModelDiagram().getVertexFactory().createVertex(node);
							mapToCurrentNodes.put(node.getAttribute("id"), inNode);
							this.model.addVertex(inNode);
						}
					}
					
					//Processing the edges of the graph
					NodeList listOfEdges = graph.getElementsByTagName("edge");
					for(int j = 0; j < listOfEdges.getLength(); j++) {
						Node currentEdge = listOfEdges.item(j);
						if(currentEdge.getNodeType() == Node.ELEMENT_NODE) {
							Element edge = (Element)currentEdge;
							GMLDiagramNode source = mapToCurrentNodes.get(edge.getAttribute("source"));
							GMLDiagramNode target = mapToCurrentNodes.get(edge.getAttribute("target"));
							
							if(source == null || target == null) {
								throw new RuntimeException("Error in the structure of the file: an edge doesn't have a source or a target");
							}
							
							this.model.addEdge(source, target).setType(typeOfConnection.CONTAINMENT);
							if(!isDirected) {
								this.model.addEdge(target, source).setType(typeOfConnection.CONTAINMENT);
							}
						}
					}
					
				}
			}
		}
		
		return this.model;
	}

	@Override
	public GMLModelDiagram getModel(ProxyDiagramNode<Node> proxy) {
		//GraphML does not support Proxies
		return null;
	}

	@Override
	public void processNode(DiagramNode node, Node current) {
		//GraphML does not support Proxies
	}

}
