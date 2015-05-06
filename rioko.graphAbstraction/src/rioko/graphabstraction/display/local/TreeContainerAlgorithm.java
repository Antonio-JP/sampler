package rioko.graphabstraction.display.local;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import rioko.utilities.Log;
import rioko.utilities.Pair;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.display.LocalNestedBuilder;

public abstract class TreeContainerAlgorithm extends LocalNestedBuilder {
		
	//GraphBuilder methods
	/**
	 * Algoritmo para generar los nodos agrupados a partir de un grafo más amplio y un nodo raíz
	 * 
	 * @param target Grafo en el se guardará la información
	 * @param source Grafo del que se obtendrá la información
	 * @param root Nodo raíz de donde se comienza el algoritmo
	 * 
	 * @throws Exception Si hay un error imposible
	 */
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception{
		
		int nNodes = 1;
		DiagramGraph target = super.buildNodes(data,properties);
		
		if(target == data || this.root == null) { //Quiere decir que target ya contiene a data
			return data;
		}
		
		//Mientras que no haya un total de maxNodes nodos en pantalla, añadimos haciendo una búsqueda en anchura
		HashMap<DiagramNode, DiagramNode> parentOf = new HashMap<>();
		Queue<Pair<DiagramNode,DiagramNode>> queue = new LinkedList<>();
		queue.add(new Pair<DiagramNode,DiagramNode>(this.root, null));
		
		
		while(!queue.isEmpty() && parentOf.keySet().size() < this.maxNodes) 
		{
			Pair<DiagramNode,DiagramNode> currentPair = queue.poll();
			
			DiagramNode current = currentPair.getFirst();
			
			if(!parentOf.containsKey(current)) {
				//Procesamiento del nodo
				target.addVertex(current);
				Log.xPrint("Construido nodo simple " + nNodes);
				nNodes++;
				
				parentOf.put(current,currentPair.getLast());
				
				//Añadimos los siguientes a la cola
				for(DiagramNode node : data.vertexFrom(current)) {
					if(!parentOf.containsKey(node) && data.getEdge(current, node).getType().equals(typeOfConnection.CONTAINMENT)) {
						queue.add(new Pair<DiagramNode,DiagramNode>(node, current));
					}
				}
				
				for(DiagramNode node : data.vertexTo(current)) {
					if(!parentOf.containsKey(node) && data.getEdge(node, current).getType().equals(typeOfConnection.CONTAINMENT)) {
						queue.add(new Pair<DiagramNode,DiagramNode>(node, current));
					}
				}
			}
		}
		
		ArrayList<ComposeDiagramNode> others = new ArrayList<>();
		ArrayList<Queue<Pair<DiagramNode,DiagramNode>>> listOfQueues = new ArrayList<>();
		
		//Creamos las colas nuevas y los vértices
		//Juntamos a gente con le mismo padre (en principio)
		while(!queue.isEmpty())
		{
			Pair<DiagramNode,DiagramNode> currentPair = queue.poll();
				
			boolean isInserted = false;
			for(Queue<Pair<DiagramNode,DiagramNode>> q : listOfQueues) {
				if(!q.isEmpty()) {
					if(q.peek().getLast() != null) {
						if(q.peek().getLast().equals(currentPair.getLast())) {
							q.add(currentPair);
							isInserted = true;
						}
					}
				}
			}
			if(!isInserted) {
				Queue<Pair<DiagramNode,DiagramNode>> newQueue = new LinkedList<Pair<DiagramNode,DiagramNode>>();
				newQueue.add(currentPair);
				
				listOfQueues.add(newQueue);
				others.add(data.getComposeVertexFactory().createVertex());
			}
		}
				
		//Finalmente vamos agrupando los nodos haciendo una pasada por cada una de las listas
		int i = -1;
		while(!listOfQueues.isEmpty())
		{
			i = (i+1)%listOfQueues.size();
			Queue<Pair<DiagramNode,DiagramNode>> actualQueue = listOfQueues.get(i);
			
			if(actualQueue.isEmpty()) {
				//Ya hemos terminado con este vértice
				if(!others.get(i).isCompose()) {
					target.addVertex(others.get(i).getRootNode());
					Log.xPrint("Construido nodo simple " + nNodes);
					nNodes++;
				} else if(!others.get(i).isEmpty()) {
					others.get(i).setLabel(others.get(i).getRootNode().getLabel());
					target.addVertex(others.get(i));
					Log.xPrint("Construido nodo compuesto " + nNodes);
					nNodes++;
				}
				
				//Quitamos ambos de las listas
				listOfQueues.remove(i);
				others.remove(i);
			} else {
				//Aun quedan nodos que añadir
				Pair<DiagramNode,DiagramNode> currentPair = actualQueue.poll();
				DiagramNode current = currentPair.getFirst();
				
				if(!parentOf.containsKey(current)) {
					//Procesamiento del nodo - Se añade al nodo compuesto correspondiente
					others.get(i).addDiagramNode(current);
					
					parentOf.put(current, currentPair.getLast());
					
					//Añadimos los siguientes a la cola
					for(DiagramNode node : data.vertexFrom(current)) {
						if(!parentOf.containsKey(node) && data.getEdge(current, node).getType().equals(typeOfConnection.CONTAINMENT)) {
							actualQueue.add(new Pair<DiagramNode,DiagramNode>(node,current));
						}
					}
					
					for(DiagramNode node : data.vertexTo(current)) {
						if(!parentOf.containsKey(node) && data.getEdge(node, current).getType().equals(typeOfConnection.CONTAINMENT)) {
							actualQueue.add(new Pair<DiagramNode,DiagramNode>(node,current));
						}
					}
				}
			}
		}
				
		//Comprobamos que están todos los nodos
		if(parentOf.keySet().size() < data.vertexSet().size()) {
			ComposeDiagramNode other = data.getComposeVertexFactory().createVertex();
			other.setLabel("Otra componente del modelo");
			
			for(DiagramNode node : data.vertexSet())
			{
				if(!parentOf.containsKey(node)) {
					other.addDiagramNode(node);
				}
			}
			
			if(other.isCompose()) {
				target.addVertex(other);
			} else {
				target.addVertex(other.getRootNode());
			}
			Log.xPrint("Añadida la otra componente del modelo (" + nNodes + ")");
			nNodes++;
		} else if(parentOf.keySet().size()> data.vertexSet().size()) {
			throw new Exception("Rioko ERROR: se han añadido más vértices de los que existen!!!");
		}
				
		return target;
	}
}
