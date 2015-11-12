package rioko.savingviews;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.algorithms.basic.CenterLayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.utilities.Log;

public class ZestDisplay<K extends Serializable, T> implements Serializable{

	/**
	 * Long serial ID for the Serializable interface 
	 */
	private static final long serialVersionUID = -6971652504194249244L;
	
	/* Fields for a Zest Display */
	private String nameOfDisplay;
	
	private HashMap<Integer, List<K>> verticesForAbstraction;
	private HashMap<Integer, Point> positionsForVertices;
	
	private ZestProperties properties;
	
	private IdParser<K> parser;
	
	/* Builders */
	public ZestDisplay(ModelDiagram<T> model, ZestProperties properties, IdParser<K> parser) {
		/* Getting the name of the model */
		this.nameOfDisplay = model.getName();
		
		/* Setting the parser */
		this.parser = parser;
		
		/* Getting the abstraction and position of the model */
		this.verticesForAbstraction = new HashMap<>();
		this.positionsForVertices = new HashMap<>();
		
		int id = 0;
		for(DiagramNode node : model.getPrintDiagram().vertexSet()) {
			/* Creating the abstraction List */
			List<K> auxList = new ArrayList<>();
			for(DiagramNode inNode : node.getFullListOfNodes()) {
				auxList.add(this.parser.getKey(inNode));
			}
			
			/* Getting the Point of the vertex */
			Point auxPosition = new Point(node.getFigure().getBounds().preciseX(), node.getFigure().getBounds().preciseY());
			
			this.verticesForAbstraction.put(id, auxList);
			this.positionsForVertices.put(id, auxPosition);
			id++;
		}
		
		/* Setting the properties of Editor */
		this.properties = properties;	
	}
	
	public ZestDisplay(IFile file, IdParser<K> parser) throws FileFormatException {
		/* Setting the parser */
		this.parser = parser;
		
		/* Basic initialization */
		this.verticesForAbstraction = new HashMap<>();
		this.positionsForVertices = new HashMap<>();
		
		/* Getting the information of Display from the file */
		this.getDataFromFile(file);
	}
	
	/* Public methods */
	public NestedBuilderAlgorithm getAbstraction() {
		return new SavedAbstractionAlgorithm(this);
	}
	
	public LayoutAlgorithm getLayout() {
		return new SavedLayoutAlgorithm(this);
	}
	
	public ZestProperties getProperties() {
		return this.properties;
	}
	
	public boolean saveDisplay(IFile file) {
		try {
			PipedInputStream in = new PipedInputStream();
			PipedOutputStream out = new PipedOutputStream(in);
		
			PrintStream ps = new PrintStream(out);
			
			/* Printing the name of the model */
			ps.println(this.nameOfDisplay);
			
			/* Printing the abstraction */
			ps.println("abstraction {");
			for(Integer id : this.verticesForAbstraction.keySet()) {
				ps.println(""+id+":"+printList(this.verticesForAbstraction.get(id)));
			}
			
			ps.println("}");
			
			/* Printing the position of the vertices */
			ps.println("position {");
			
			
			ps.println("}");
			
			/* Printing the configuration */
			ps.println("configuration {");
			ps.println(this.properties.serialize());
			ps.println("}");
			
			/* Closing the streams */
			ps.flush();
			ps.close();
			out.close();
			
			/* Writing the file */
			file.setContents(in, true, true, null);
			
		} catch(IOException e) {
			Log.exception(e);
			return false;
		} catch (CoreException e) {
			Log.exception(e);
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
					do {
						line = bf.readLine();
						aux = cleanString(line);
						String[] tokens = aux.split(":");
						if(tokens.length != 2) {
							return false;
						}
						String[] nodes = tokens[1].split(",");
						
						ArrayList<K> list = new ArrayList<>();
						for(int i = 0; i < nodes.length; i++) {
							list.add(this.parser.getFromString(nodes[i]));
						}
						
						this.verticesForAbstraction.put(Integer.parseInt(tokens[0]), list);
					} while(!line.endsWith("}"));
					
					break;
				} else if(line.startsWith("position")) {
					do {
						line = bf.readLine();
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
						
					} while(!line.endsWith("}"));
					
					break;
				} else if(line.startsWith("configuration")) {
					this.properties = new ZestProperties(bf);
					
					line = bf.readLine();
					
					break;
				}
			}
			
			return line == null;
		} catch (IOException | NullPointerException e) {
			return false;
		}
	}
	
	private String printList(List<K> list) {
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
				.replaceAll("(", "")
				.replaceAll(")", "")
				.replaceAll(";", "")
				.replaceAll("\\}", "");
	}
	
	/* Private classes */
	private class SavedAbstractionAlgorithm extends NestedBuilderAlgorithm {
		
		private ZestDisplay<K, T> display;
		
		public SavedAbstractionAlgorithm(ZestDisplay<K, T> zestDisplay) {
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
				mapToNodes.get(display.parser.getKey(node)).add(node);
			}
			
			for(Integer id : mapToNodes.keySet()) {
				if(mapToNodes.get(id).size() > 1) {
					data.addVertex(data.getComposeVertexFactory().createVertex(mapToNodes.get(id)));
				} else {
					data.addVertex(mapToNodes.get(id).get(0));
				}
			}
			
			return res;
		}
	}
	
	private class SavedLayoutAlgorithm extends CenterLayoutAlgorithm {
		
		private ZestDisplay<K, T> display;
		
		public SavedLayoutAlgorithm(ZestDisplay<K,T> zestDisplay) {
			this.display = zestDisplay;
		}

		@Override
		public LayoutAlgorithm copy() {
			return new SavedLayoutAlgorithm(this.display);
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
