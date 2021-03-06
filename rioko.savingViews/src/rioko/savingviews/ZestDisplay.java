package rioko.savingviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import rioko.drawmodels.diagram.IdParser;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.algorithms.basic.CenterLayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.geometry.Rectangle;
import rioko.layouts.graphImpl.LayoutGraph;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.utilities.Log;

public class ZestDisplay implements Serializable{

	/**
	 * Long serial ID for the Serializable interface 
	 */
	private static final long serialVersionUID = -6971652504194249244L;
	
	/* Fields for a Zest Display */
	private String nameOfDisplay;
	
	private HashMap<Integer, List<Object>> verticesForAbstraction;
	private HashMap<Integer, Point> positionsForVertices;
	private ArrayList<Integer> markedVertices;
	
	private ZestProperties properties;
	
	private IdParser parser;
	
	/* Builders */
	public ZestDisplay(ModelDiagram<?> model, ZestProperties properties, IdParser parser) {
		/* Getting the name of the model */
		this.nameOfDisplay = model.getName();
		
		/* Setting the parser */
		this.parser = parser;
		
		/* Getting the abstraction and position of the model */
		this.verticesForAbstraction = new HashMap<>();
		this.positionsForVertices = new HashMap<>();
		this.markedVertices = new ArrayList<>();
		
		int id = 0;
		for(DiagramNode node : model.getPrintDiagram().vertexSet()) {
			/* Creating the abstraction List */
			List<Object> auxList = new ArrayList<>();
			for(DiagramNode inNode : node.getFullListOfNodes()) {
				auxList.add(this.parser.getKey(inNode));
			}
			
			/* Getting the Point of the vertex */
			Point auxPosition = new Point(node.getFigure().getBounds().preciseX(), node.getFigure().getBounds().preciseY());
			
			this.verticesForAbstraction.put(id, auxList);
			this.positionsForVertices.put(id, auxPosition);
			if(node.isRoot()) {
				this.markedVertices.add(id);
			}
			id++;
		}
		
		/* Setting the properties of Editor */
		this.properties = properties;	
	}
	
	public ZestDisplay(IFile file, IdParser parser) throws FileFormatException {
		/* Setting the parser */
		this.parser = parser;
		
		/* Basic initialization */
		this.verticesForAbstraction = new HashMap<>();
		this.positionsForVertices = new HashMap<>();
		this.markedVertices = new ArrayList<>();
		
		/* Getting the information of Display from the file */
		this.getDataFromFile(file);
	}
	
	/* Public methods */
	public NestedBuilderAlgorithm getAbstraction() {
		return new SavedAbstractionAlgorithm(this);
	}
	
	public LayoutAlgorithm getLayout(LayoutAlgorithm auxLayout) {
		return new SavedLayoutAlgorithm(this, auxLayout);
	}
	
	public ZestProperties getProperties() {
		return this.properties;
	}
	
	public boolean saveDisplay(IFile file) {
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			if(file.exists()) {
				file.delete(true, monitor);
			}
			
			StringBuilder builder = new StringBuilder();
			
			/* Printing the name of the model */
			builder.append(this.nameOfDisplay + "\n");
			
			/* Printing the abstraction */
			builder.append("abstraction {\n");
			for(Integer id : this.verticesForAbstraction.keySet()) {
				builder.append("\t"+id+":"+printList(this.verticesForAbstraction.get(id)) + "\n");
			}
			
			builder.append("}\n");
			
			/* Printing the position of the vertices */
			builder.append("position {\n");
			for(Integer id : this.positionsForVertices.keySet()) {
				builder.append("\t"+id+": (" + this.positionsForVertices.get(id).getX() + ", " + this.positionsForVertices.get(id).getY() + ")\n");
			}
			
			builder.append("}\n");
			
			/* Printing the configuration */
			builder.append("configuration {\n");
			builder.append(this.properties.serialize() + "\n");
			builder.append("}\n");
			
			/* Printing the marking of vertices */
			builder.append("roots {\n");
			builder.append("\t");
			
			for(int i = 0; i < this.markedVertices.size() - 1; i++) {
				builder.append(this.markedVertices.get(i)+",");
			}
			if(this.markedVertices.size() > 0) {
				builder.append(this.markedVertices.get(this.markedVertices.size() - 1)+"\n");
			}
			
			builder.append("}\n");
			
			file.create(new ReaderInputStream(new StringReader(builder.toString())), true, monitor);		
		} catch(CoreException e) {
			Log.exception(e);
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	/* Protected methods */

	/* Private methods */
	private void getDataFromFile(IFile file) throws FileFormatException {
		try {
			BufferedReader bf = new BufferedReader(new InputStreamReader(file.getContents()));
			
			this.nameOfDisplay = bf.readLine();
			
			while(this.readBlock(bf));
			
			bf.close();
		
		} catch (CoreException | IOException e) {
			Log.exception(e);
		}
	}

	private boolean readBlock(BufferedReader bf) {
		String line;
		String aux;
		try {
			line = bf.readLine();
		
			while(line != null) {
				if(line.startsWith("abstraction")) {
					line = bf.readLine();
					while(!line.endsWith("}")) {
						aux = cleanString(line);
						String[] tokens = aux.split(":");
						if(tokens.length != 2) {
							return false;
						}
						String[] nodes = tokens[1].split(",");
						
						ArrayList<Object> list = new ArrayList<>();
						for(int i = 0; i < nodes.length; i++) {
							if(!nodes[i].isEmpty()) {
								list.add(this.parser.getFromString(nodes[i]));
							}
						}
						
						this.verticesForAbstraction.put(Integer.parseInt(tokens[0]), list);
						
						line = bf.readLine();
					}
					
					break;
				} else if(line.startsWith("position")) {
					line = bf.readLine();
					while(!line.endsWith("}")) {
						aux = cleanString(line);
						String[] tokens = aux.split(":");
						if(tokens.length != 2) {
							return false;
						}
						String[] coords = tokens[1].split(",");
						if(coords.length != 2) {
							return false;
						}
						
						this.positionsForVertices.put(
								Integer.parseInt(tokens[0]), 
								new Point(Double.parseDouble(coords[0]), Double.parseDouble(coords[1])));
						
						line = bf.readLine();
					}
					
					break;
				} else if(line.startsWith("configuration")) {
					this.properties = new ZestProperties(bf);
					
					line = bf.readLine();
					
					break;
				} else if(line.startsWith("roots")) {
					line = bf.readLine();
					while(!line.endsWith("}")) {
						aux = cleanString(line);
						String[] tokens = aux.split(",");
						
						for(String token : tokens) {
							if(!token.isEmpty()) {
								this.markedVertices.add(Integer.parseInt(token));
							}
						}
						
						line = bf.readLine();
					}
					
					break;
				}
				
				line = bf.readLine();
			}
			
			return line != null;
		} catch (IOException | NullPointerException e) {
			return false;
		}
	}
	
	private String printList(List<Object> list) {
		String res = "(";
		for(int i = 0; i < list.size()-1; i++) {
			res += list.get(i).toString() + ",";
		}
		if(list.size() > 0) {
			res += list.get(list.size()-1).toString();
		}
		
		res += ")";
		
		return res;
	}
	
	/* Static methods */
	private static String cleanString(String str) {
		return str.replaceAll(" ", "")
				.replaceAll("\t", "")
				.replaceAll("\\{", "")
				.replaceAll("\\(", "")
				.replaceAll("\\)", "")
				.replaceAll(";", "")
				.replaceAll("\\}", "");
	}
	
	/* Private classes */
	private class SavedAbstractionAlgorithm extends NestedBuilderAlgorithm {
		
		private ZestDisplay display;
		
		public SavedAbstractionAlgorithm(ZestDisplay zestDisplay) {
			super("Saved Algorithm");
			
			this.display = zestDisplay;
		}
		
		@Override
		public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) {
			DiagramGraph res = data.getSimilarType();
			
			HashMap<Integer, ArrayList<DiagramNode>> mapToNodes = new HashMap<>();
			for(Integer id : display.verticesForAbstraction.keySet()) {
				mapToNodes.put(id, new ArrayList<>());
			}
			
			for(DiagramNode node : data.vertexSet()) {
				mapToNodes.get(this.getKeyFor(display.parser.getKey(node))).add(node);
			}
			
			for(Integer id : mapToNodes.keySet()) {
				DiagramNode toAdd;
				if(mapToNodes.get(id).size() > 1) {
					toAdd = res.getComposeVertexFactory().createVertex(mapToNodes.get(id));
				} else {
					toAdd = mapToNodes.get(id).get(0);
				}
				
				if(this.display.markedVertices.contains(id)) {
					toAdd.setBeRoot(true);
				} else {
					toAdd.setBeRoot(false);
				}
				
				res.addVertex(toAdd);
			}
			
			return res;
		}
		
		@Override
		public void buildEdges(DiagramGraph target, DiagramGraph data) {
			// For each new node
			for(DiagramNode tgNode : target.vertexSet()) {
				//For each inner node (that is contained in data graph)
				for(DiagramNode dtNode : tgNode.getFullListOfNodes()) {
					// We iterate in every outing edge
					for(DiagramEdge<DiagramNode> edge : data.edgesFrom(dtNode)) {
						// And search in which vertex of target graph is contained the target
						for(DiagramNode destTgNode : target.vertexSet()) {
							if(destTgNode.areRelated(edge.getTarget())) {
								if(destTgNode != tgNode) {
									DiagramEdge<DiagramNode> prevEdge = target.getEdge(tgNode, destTgNode);
									if(prevEdge == null) {	//If the edge does not exist previously, we add it
										DiagramEdge<DiagramNode> edgeTg = target.addEdge(tgNode, destTgNode);
										edgeTg.setType(edge.getType());
									} else if(!prevEdge.getType().equals(edge.getType())) {	//If not, but the type of the edge is different, we put simple type
										prevEdge.setType(typeOfConnection.SIMPLE);
									}
								}
								
								break;
							}
						}
					}
				}
			}
		}

		private Integer getKeyFor(Object key) {
			for(Integer id : this.display.verticesForAbstraction.keySet()) {
				for(Object obj : this.display.verticesForAbstraction.get(id)) {
					if(obj.toString().equals(key.toString())) {
						return id;
					}
				}
			}
			
			return null;
		}
	}
	
	private class SavedLayoutAlgorithm extends CenterLayoutAlgorithm {
		
		private ZestDisplay display;
		private LayoutAlgorithm auxAlgorithm;
		
		public SavedLayoutAlgorithm(ZestDisplay zestDisplay, LayoutAlgorithm auxAlgorithm) {
			this.display = zestDisplay;
			this.auxAlgorithm = auxAlgorithm;
		}

		@Override
		public LayoutAlgorithm copy() {
			return new SavedLayoutAlgorithm(this.display, this.auxAlgorithm);
		}
		
		@Override
		public void applyLayout(LayoutGraph graphToLayout, Rectangle layoutArea) {
			try {
				super.applyLayout(graphToLayout, layoutArea);
			} catch(Exception e) {
				e.printStackTrace();
				Log.print("Applying the auxiliary algorithm");
				this.auxAlgorithm.applyLayout(this.graph, this.getLayoutArea());
			}
		}

		@Override
		protected void running() {
			int i = 0;
			for(LayoutVertex lv : this.graph.vertexSet()) {
				this.setLocation(lv, this.display.positionsForVertices.get(i));
				i++;
			}
		}
	}
}
