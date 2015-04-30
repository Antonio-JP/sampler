package rioko.graphabstraction.display;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.UnsignedIntConfiguration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.utilities.Pair;

public abstract class LocalNestedBuilder extends NestedGraphBuilder {

	
	protected RootNodeConfiguration rootConf = this.getRootNodeConfiguration(null,null);
	protected UnsignedIntConfiguration toShowConf = new UnsignedIntConfiguration();
	protected DiagramNode root = null;
	
	protected int maxNodes = -1;
	
	//GraphBuilder Methods
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception{
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		//Si la configuración no nos da un límite de vértices devolvemos el grafo original
//		if ((this.toShowConf.getConfiguration() == 0) && (!properties.isSetMaxNodes())) {
		if ((this.toShowConf.getConfiguration() == 0) && 
				((properties.getConfiguration(DisplayOptions.MAX_NODES.toString()) == null) || 
						((int)(properties.getConfiguration(DisplayOptions.MAX_NODES.toString())) == 0))) {
			return data;
		} else {
			//Si ahora lo que no nos dan es un nodo raíz, lo establecemos como el primero del grafo data
			this.root = rootConf.getConfiguration();
			Object auxRoot = properties.getConfiguration(DisplayOptions.ROOT_NODE.toString());
			if(this.root == null && auxRoot != null) {
				this.root = this.existsRelatedNode(data,(DiagramNode)auxRoot);
			}
			
			this.maxNodes = toShowConf.getConfiguration();
			if(this.maxNodes == 0 || this.maxNodes == -1) {
				this.maxNodes = (int) properties.getConfiguration(DisplayOptions.MAX_NODES.toString());
			}
		}
		
		return target;
	}

	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		Collection<DisplayOptions> res = new ArrayList<>();
		
		if(this.rootConf.getConfiguration() == null) {
			res.add(DisplayOptions.ROOT_NODE);
		} 
		if(this.toShowConf.getConfiguration() == 0) {
			res.add(DisplayOptions.MAX_NODES);
		}
		
		return res;
	}

	//Configurable Methods
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String, Configuration>> conf = new ArrayList<>();
		
		conf.add(new Pair<>("Root Node", rootConf));
		conf.add(new Pair<>("Nodes tho Show", toShowConf));
		
		return conf;
	}

	@Override
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		boolean root = false, toShow = false;
		
		for(Configuration conf : newConf) {
			if(!root && conf instanceof RootNodeConfiguration) {
				this.rootConf.setConfiguration(conf.getConfiguration());
				
				root = true;
			} else if(!toShow && conf instanceof UnsignedIntConfiguration) {
				this.toShowConf.setConfiguration(conf.getConfiguration());
				
				toShow = true;
			}
			
			if(root && toShow) { //Ya está todo configurado
				break;
			}
		}
	}
	


	protected abstract RootNodeConfiguration getRootNodeConfiguration(DiagramGraph data, DisplayProperties properties);
}
