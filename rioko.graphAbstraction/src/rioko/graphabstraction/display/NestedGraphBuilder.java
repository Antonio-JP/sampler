package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import rioko.utilities.Log;
import rioko.utilities.Pair;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;

public abstract class NestedGraphBuilder implements GraphBuilder,Configurable{
	//Graph Builder methods	
	@Override
	public DiagramGraph createNestedGraph(DiagramGraph data, Configurable properties)
	{
		DiagramGraph target = data;
		
		if(this.checkProperties(properties)) {			
			try {
				//Nodos
				Log.xOpen("nodes");
				target = this.buildNodes(data, properties);
				Log.xClose("nodes");
				//Aristas
				if(target != data) {
					Log.xOpen("edges");
					this.buildEdges(target, data);
					Log.xClose("edges");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return target;
	}
	
	@Override
	public boolean checkProperties(Configurable properties)
	{
		if(properties != null) {
			boolean res = true;
			
			for(DisplayOptions option : this.getConfigurationNeeded()) {
//				res &= properties.isSet(option);
				res &= (properties.getConfiguration(option.toString()) != null);
			}
			
			return res;
		}
		return this.getConfigurationNeeded().isEmpty();
	}

	@Override
	public void buildEdges(DiagramGraph target, DiagramGraph data)
	{
		Log.xPrint(" -- Construyendo aristas:");
		int nNodes = 1;
		//Para cada nodo del grafo que vamos a pintar
		for(DiagramNode trgSource : target.vertexSet())
		{
			Log.xPrint("Creando arista desde el nodo " + nNodes);
			Log.xOpen("edges-node");
			//Vemos de cu�les de los nodos del grafo original procede
			int dataNodes = 1;
			//for(DiagramNode dtSource : data.getNodes())
			for(DiagramNode dtSource : this.getNodesForSource(trgSource, data))
			{
				Log.xPrint("Mirando nodo de data (" + dataNodes + ")");
				Log.xOpen("edges-dataNodes");
				dataNodes++;
				Set<DiagramNode> listOfSons = data.vertexFrom(dtSource);
				int sonNodes = 1;
				for(DiagramNode dtTarget : listOfSons)
				{
					Log.xPrint(" -- Analizando hijo n�mero " + sonNodes+"/"+listOfSons.size());
					sonNodes++;
					Log.xOpen("edges-son");
					//Miramos que dtTarget no est� relacionado con trgSource
					if(!trgSource.areRelated(dtTarget))
					{
						Log.xPrint("--- Encontrada conexi�n a a�adir...");
						//En ese caso, a�adimos la nueva conexi�n
						//Buscamos en target los nodos del grafo a pintar que es o contiene a dtTarget.
						//	- Se tiene en cuenta si se han filtrado o no v�rtices
						Log.xPrint("--- & Buscando los nodos en el grafo destino al que a�adir esta conexi�n");
						Log.xOpen("edges-findNodes");
						Pair<DiagramNode[], DiagramNode> trgsTarget = this.getListOfTargetSons(target, dtTarget, trgSource, data);
						Log.xClose("edges-findNodes");
						Log.xPrint("--- & Encontradas las conexiones");
						int trgsNodes = 1;
						for(DiagramNode trgTarget: trgsTarget.getFirst())
						{
							Log.xPrint("--- * A�adiendo conexi�n " + trgsNodes+"/"+trgsTarget.getFirst().length);
							Log.xOpen("edges-addConnection");
							if(target.getEdge(trgSource, trgTarget) == null) {
								DiagramEdge<DiagramNode> edge = target.addEdge(trgSource, trgTarget);
									
								//Ponemos el tipo de arista que corresponda
								Log.xPrint("--- * Ajustando el tipo de arista");
								if(trgsTarget.getLast() != null) {
									Log.xPrint("--- ** El nodo no existe: el tipo es simple");
									edge.setType(data.getEdge(dtSource, trgsTarget.getLast()).getType());
								} else {
									Log.xPrint("--- ** Hay que calcular el tipo de conexi�n");
									Log.xOpen("edges-typeConnection");
									edge.setType(this.getTypeOfEdge(trgSource, trgTarget, data));
									Log.xClose("edges.typeConnection");
								}
							}
							Log.xClose("edges.addConnection");
							Log.xPrint("--- * Conexi�n bien introducida");
						}
						Log.xPrint("--- A�adidas las conexiones");
						//FIN !trgSource.equals(dtTarget)
					}
					//FIN bucle de adyacentes a dtSource
					Log.xClose("edges-son");
				}
				//FIN trgSource.equals(dtSource)
				//Este bucle lo repetimos todas las veces ya que queremos pintar todas las aristas que salen de los nodos que componen trgSource
				Log.xClose("edges-dataNodes");
			}
			Log.xClose("edges-node");
			Log.xPrint("Nodo " + nNodes + " terminado");
			nNodes++;
		}
		Log.xPrint(" -- Done");
	}
	
	//Proper methods
	private Pair<DiagramNode[], DiagramNode> getListOfTargetSons(DiagramGraph target, DiagramNode dtTarget, DiagramNode trgSource, DiagramGraph data) {
		//Miramos si hay un nodo relacionado que sea dtTarget
		ArrayList<DiagramNode> list = new ArrayList<>();
		Log.xPrint("--- && Mirando si existe un nodo en el grafo destino...");
		list.add(this.existsRelatedNode(target, dtTarget));
		
		DiagramNode first = null;
		
		if(list.get(0) == null) {
			Log.xPrint("--- &&& No existe. Buscamos a sus hijos");
			list.remove(0);
			first = dtTarget;
			
			//Buscamos los nodos m�s cercanos que est�n enlazados
			Queue<DiagramNode> queue = new LinkedList<DiagramNode>();
			queue.add(dtTarget);
			
			while(!queue.isEmpty()) {
				DiagramNode current = queue.poll();
				for(DiagramNode dtNode : data.vertexFrom(current)) {
					DiagramNode node = this.existsRelatedNode(target, dtNode);
					if(node == null) {
						queue.add(dtNode);
					} else if(!node.areRelated(trgSource)) {
						list.add(node);
					}
				}
			}
			Log.xPrint("--- &&& Hijos encontrados");
		}
		Log.xPrint("--- && Busqueda de v�rtices terminada -> " + list.size());
		
		return new Pair<DiagramNode[],DiagramNode>(list.toArray(new DiagramNode[0]), first);
	}
	
	protected DiagramNode existsRelatedNode(DiagramGraph target, DiagramNode node) {
		for(DiagramNode trgNode : target.vertexSet()) {
			if(node.areRelated(trgNode)) {
				return trgNode;
			}
		}
		
		return null;
	}

	/**
	 * M�todo que averigua el tipo de conexi�n que deber�n tener dos nodos del grafo a pintar. El criterio es el que sigue:
	 *   - Si los dos son simples, se toma el tipo que ya hab�a
	 *   - Si un nodo es simple, se deber� comprobar que todas las conexiones son del mismo tipo (y en el caso de contaiment, en la misma direcci�n)
	 *   - Si los nodos son compuestos, se necesita que todas las conexiones sean iguales para poner un tipo.
	 * 
	 * @param source Nodo de origen de la arista
	 * @param target Nodo de llegada de la arista
	 * 
	 * @return Un tipo de conexi�n. Null en caso de que no exista dicha conexi�n
	 */
	private typeOfConnection getTypeOfEdge(DiagramNode source, DiagramNode target, DiagramGraph data)
	{
		//Caso simple-simple (ambos est�n en el grafo de datos)
		//if((!(source instanceof ComposeDiagramNode)) && (!(target instanceof ComposeDiagramNode))) {
		if((data.containsVertex(source)) && (data.containsVertex(target))) {
			Log.xPrint("--- *** Caso simple-simple");
			
			//Devolvemos el tipo de arista que ya ten�an en el grafo data
			//DiagramEdge<DiagramNode> edge = data.getEdge(src, trg);
			DiagramEdge<DiagramNode> edge = data.getEdge(source, target);
			if(edge != null) {
				return edge.getType();
			} else {
				return null;
			}			
		}
		
		//Caso general
		Log.xPrint("--- *** Caso general");
		typeOfConnection actualType = null;	//Inicializamos al caso "sin conexion"
		
		//Recorremos los nodos que est�n contenidos en source
		for(DiagramNode src : this.getNodesForSource(source,data)) {
			//Recorremos los nodos que est�n contenidos en target
			for(DiagramNode trg : this.getNodesForSource(target,data)) {
				//Miramos el tipo de arista en data que tienen esos v�rtices
				typeOfConnection newType = this.getTypeOfEdge(src, trg, data);
				
				//Si es la primera vez, establecemos newType como actualType
				if(actualType == null) {
					actualType = newType;
				} 
				
				//Si newType es null no hacemos m�s comprobaciones
				if(newType != null) {
					//OBS: si newType != null, entonces actualType != null
					
					//Comprobamos que siempre tenemos el mismo tipo
					if (!newType.equals(actualType)) {
						//Si no es as�, ponemos el caso de error y rompemos el bucle
						actualType = typeOfConnection.SIMPLE;
						break;
					}
				}
			}
			//Comprobamos que el fin del bucle anterior no sea por error
			if(actualType != null && actualType.equals(typeOfConnection.SIMPLE)) {
				break;
			}
		}
		
		return actualType;
	}
	
	private ArrayList<DiagramNode> getNodesForSource(DiagramNode trgSource, DiagramGraph data) {
		ArrayList<DiagramNode> list = new ArrayList<>();
		
		if(data.containsVertex(trgSource)) {
			list.add(trgSource);
		} else {
			for(DiagramNode node : trgSource.getNodes()) {
				if(data.containsVertex(node)) {
					list.add(node);
				}
			}
		}
		
		if(list.isEmpty()) { //Entonces es el caso de expandir
			for(DiagramNode node : data.vertexSet()) {
				if(node.areRelated(trgSource)) {
					list.add(node);
					break;
				}
			}
		}
		
		return list;
	}
}
