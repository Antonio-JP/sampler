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
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception{
		
		int nNodes = 1;
		DiagramGraph target = super.buildNodes(data,properties);
		
		if(target == data || this.root == null) { //Quiere decir que target ya contiene a data
			return data;
		}
		
		//Mientras que no haya un total de maxNodes nodos en pantalla, a�adimos haciendo una b�squeda en anchura
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
				Log.xPrint("Built simple node " + nNodes);
				nNodes++;
				
				parentOf.put(current,currentPair.getLast());
				
				//A�adimos los siguientes a la cola
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
		
		//Creamos las colas nuevas y los v�rtices
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
				//Ya hemos terminado con este v�rtice
				if(!others.get(i).isCompose()) {
					target.addVertex(others.get(i).getRootNode());
					Log.xPrint("Built simple node" + nNodes);
					nNodes++;
				} else if(!others.get(i).isEmpty()) {
					others.get(i).setLabel(others.get(i).getRootNode().getLabel());
					target.addVertex(others.get(i));
					Log.xPrint("Built compose node " + nNodes);
					nNodes++;
				}
				
				//Quitamos ambos de las listas
				listOfQueues.remove(i);
				others.remove(i);
			} else {
				//Aun quedan nodos que a�adir
				Pair<DiagramNode,DiagramNode> currentPair = actualQueue.poll();
				DiagramNode current = currentPair.getFirst();
				
				if(!parentOf.containsKey(current)) {
					//Procesamiento del nodo - Se a�ade al nodo compuesto correspondiente
					others.get(i).addDiagramNode(current);
					
					parentOf.put(current, currentPair.getLast());
					
					//A�adimos los siguientes a la cola
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
				
		//Comprobamos que est�n todos los nodos
		if(parentOf.keySet().size() < data.vertexSet().size()) {
			ComposeDiagramNode other = data.getComposeVertexFactory().createVertex();
			other.setLabel("Other connected components");
			
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
			Log.xPrint("Added the others connected components of the model (" + nNodes + ")");
			nNodes++;
		} else if(parentOf.keySet().size()> data.vertexSet().size()) {
			throw new Exception("Rioko ERROR: There are more nodes added than existing on original graph!!");
		}
				
		return target;
	}
}
