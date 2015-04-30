package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import rioko.searchers.SearchIterator;
import rioko.utilities.Pair;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
//import rioko.graphabstraction.diagram.XMIDiagram.ComposeXMIDiagramNode;
import rioko.graphabstraction.diagram.iterators.TreeUndirectedSearchIterator;

public class ExpandComposeNodeNestedBuilder extends NestedGraphBuilder {
	
	private ComposeDiagramNode compose;
	private DiagramGraph baseGraph;
	
	private static final int levelsToShow = 1;
	
	public ExpandComposeNodeNestedBuilder(ComposeDiagramNode compose, DiagramGraph baseGraph) {
		this.compose = compose;
		this.baseGraph = baseGraph;
	}

	//GraphBuilder Methods
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		//Primero hacemos una busqueda en anchura del grafo data para saber su distribución
		DiagramNode root = this.baseGraph.vertexSet().iterator().next();
		
		TreeUndirectedSearchIterator<DiagramNode, DiagramEdge<DiagramNode>> iterator = new TreeUndirectedSearchIterator<DiagramNode, DiagramEdge<DiagramNode>>(data, this.getDataNode(root, data), true);
		
		//Creamos una lista para almacenar los niveles
		ArrayList<ArrayList<DiagramNode>> listOfLevels = new ArrayList<>();
		listOfLevels.add(new ArrayList<DiagramNode>());
		
		HashSet<DiagramNode> foundNodes = new HashSet<>();
		
		while(iterator.hasNext()) {
			DiagramNode currentNode = iterator.next();
			
			DiagramNode currentReal = this.getRealNode(currentNode);
			
			if(currentReal != null && !foundNodes.contains(currentReal)) {
				//Según miramos, vamos añadiendo el vértice si toca:
				if(!compose.containsInnerNode(currentNode)) {
					target.addVertex(currentReal);
				} else if(!compose.containsInnerNode(iterator.getParent(currentNode))) {
						target.addVertex(currentNode);
						listOfLevels.get(0).add(currentNode);
				} else {
					//No es un nodo externo a compose y su padre no está fuera: es un nodo interno, luego tendrá al padre en un nivel
					for(int i = listOfLevels.size(); i >= 0; i--) {
						//OBS: No puede llega la i a 0 NUNCA, ya que eso querría decir que o bien currentNode está fuera de compose o que
						//   su padre está fuera de compose
						if(listOfLevels.get(i-1).contains(this.getRealNode(iterator.getParent(currentNode)))) {
							if(i == listOfLevels.size()) {
								listOfLevels.add(new ArrayList<DiagramNode>());
							}
							
							listOfLevels.get(i).add(currentReal);
							
							//Si está en un nivel que toca mostrar, se copia
							if(i < levelsToShow) {
								target.addVertex(currentReal);
							}
							
							break;
						}
					}
				}

				foundNodes.add(currentReal);
			}
		}
		
		//Una vez terminada la búsqueda en anchura, queda, a partir del nivel a mostrar, agrupar el resto de nodos
		HashMap<DiagramNode,ArrayList<DiagramNode>> subTrees = new HashMap<>();
		for(int i = levelsToShow; i < listOfLevels.size(); i++) {
			for(DiagramNode auxNode : listOfLevels.get(i)) 
			{
				DiagramNode baseNode = this.getIParent(auxNode, iterator, listOfLevels, levelsToShow-1); 
				
				if(!subTrees.containsKey(baseNode)) {
					subTrees.put(baseNode, new ArrayList<DiagramNode>());
				}
				
				subTrees.get(baseNode).add(auxNode);
			}
		}
		
		//Añadimos todos los nodos compuestos que han salido
		for(DiagramNode parent : subTrees.keySet()) {
			ComposeDiagramNode xmiNode = data.getComposeVertexFactory().createVertex(subTrees.get(parent));
			xmiNode.setLabel(xmiNode.getNodes().get(0).getLabel());
			target.addVertex(xmiNode);
		}
		
		//Retornamos el grafo con los nuevos vértices
		return target;
	}

	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		return new ArrayList<>();
	}
	
	//Configurable methods
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		return new ArrayList<>();
	}

	@Override
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		/* Do nothing */
	}
	
	//Private methods
	private DiagramNode getPreviousParent(DiagramNode node, SearchIterator<DiagramNode> iterator, int currentLevel, ArrayList<ArrayList<DiagramNode>> listOfLevels) {
		if(currentLevel > 0) {
			if(node instanceof ComposeDiagramNode) {
				return this.getPreviousParent(node.getRootNode(), iterator, currentLevel, listOfLevels);
			} else {
				DiagramNode parent = iterator.getParent(node);
				for(DiagramNode realParent : listOfLevels.get(currentLevel-1)) {
					if(parent.areRelated(realParent)) {
						return realParent;
					}
				}
			}
		}
		return null;
	}
	
	private DiagramNode getIParent(DiagramNode node, SearchIterator<DiagramNode> iterator, ArrayList<ArrayList<DiagramNode>> listOfLevels, int level) {
		DiagramNode auxNode;
		DiagramNode currentNode = node;
		
		if(level >= 0 && listOfLevels.size() > level) {
			int i = -1;
			for(int j = level + 1; j < listOfLevels.size() && i == -1; j++) {
				if(listOfLevels.get(j).contains(node)) {
					i = j;
				}
			}
			
			if(i != -1) {
				for(; i > level && currentNode != null; i--) {
					auxNode = this.getPreviousParent(currentNode, iterator, i, listOfLevels);
					currentNode = auxNode;
				}
			}
		}
		
		return currentNode;
	}

	private DiagramNode getRealNode(DiagramNode node) {
		DiagramNode res = this.getInComposeNode(node);
		
		if(res == null) {
			for(DiagramNode real : this.baseGraph.vertexSet()) {
				if(real.areRelated(node)) {
					return real;
				}
			}
		}
		
		return res;
	}
	
	private DiagramNode getInComposeNode(DiagramNode node) {
		for(DiagramNode real : this.compose.getNodes()) {
			if(real.areRelated(node)) {
				return real;
			}
		}
		
		return null;
	}
	
	private DiagramNode getDataNode(DiagramNode node, DiagramGraph data) {
		for(DiagramNode inData : data.vertexSet()) {
			if(inData.areRelated(node)) {
				return inData;
			}
		}
		
		return null;
	}

}
