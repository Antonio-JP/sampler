package rioko.graphabstraction.display.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import rioko.utilities.Log;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.iterators.TreeUndirectedSearchIterator;
import rioko.graphabstraction.display.GlobalNestedBuilder;

public class GlobalLeavesCompactificationNestedBuilder extends GlobalNestedBuilder {
	
	//GraphBuilders Methods
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		
		//Hacemos una búsqueda en anchura para ver quiénes son las hojas
		Set<DiagramNode> leaves = new HashSet<>();
		
		DiagramNode root = data.vertexSet().iterator().next();
		
		TreeUndirectedSearchIterator<DiagramNode, DiagramEdge<DiagramNode>> iterator = new TreeUndirectedSearchIterator<DiagramNode, DiagramEdge<DiagramNode>>(data, root, true);
		
		Log.xPrint(" --- Working on the graph nodes - Getting leaves");
		Log.xOpen("nodes-iteration");
		int nNodes = 1;
		while(iterator.hasNext()) {
			Log.xPrint(" --- & Working on node " + nNodes);
			nNodes++;
			
			DiagramNode currentNode = iterator.next();
			leaves.add(currentNode);
			
			DiagramNode parent = iterator.getParent(currentNode);
			if(parent == null) {
				leaves.remove(currentNode);
				
				target.addVertex(currentNode);
			} else if(parent != null && leaves.contains(parent)) {
				//Como el nodo parent no es hoja, lo añadimos al grafo
				leaves.remove(parent);
				
				target.addVertex(parent);
			}
		}
		Log.xClose("nodes-iteration");
		
		Log.xPrint(" --- Compactifying leaves by their parent");
		Log.xOpen("nodes-agrupation");
		//Ahora tenemos en leaves las hojas del árbol. Las agrupamos por padre
		HashMap<DiagramNode, ArrayList<DiagramNode>> listOfParents = new HashMap<>();
		
		nNodes = 1;
		for(DiagramNode node : leaves) {
			Log.xPrint(" --- & Working on leaf " + nNodes+"/"+leaves.size());
			nNodes++;
			DiagramNode parent = iterator.getParent(node);
			
			if(!listOfParents.containsKey(parent)) {
				listOfParents.put(parent, new ArrayList<DiagramNode>());
			} 
			
			listOfParents.get(parent).add(node);
		}
		Log.xClose("nodes-agrupation");
		
		Log.xPrint(" --- Adding composed nodes");
		Log.xOpen("nodes-creation");
		//Ahora creamos los nodos agrupando los nodos de las hojas según el padre (y tipo, si corresponde)
		for(DiagramNode parent : listOfParents.keySet()) {
			DiagramNode compose = data.getComposeVertexFactory().createVertex(listOfParents.get(parent));
			compose.setLabel(compose.getRootNode().getLabel());
			target.addVertex(compose);
			Log.xPrint(" --- & Compose node added");
		}
		Log.xClose("nodes-creation");
		
		return target;
	}
}
