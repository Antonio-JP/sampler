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
import rioko.graphabstraction.display.configurations.MaxNodesConfiguration;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.utilities.Pair;

public abstract class LocalNestedBuilder extends NestedGraphBuilder {

	
	protected RootNodeConfiguration rootConf = this.getRootNodeConfiguration(null,null);
	protected UnsignedIntConfiguration toShowConf = new MaxNodesConfiguration();
	protected DiagramNode root = null;
	
	protected int maxNodes = -1;
	
	//GraphBuilder Methods
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception{
		DiagramGraph target = new DiagramGraph(data.getEdgeClass(), data.getVertexClass(), data.getComposeClass());
		//Si la configuraci�n no nos da un l�mite de v�rtices devolvemos el grafo original
//		if ((this.toShowConf.getConfiguration() == 0) && (!properties.isSetMaxNodes())) {
		if ((this.toShowConf.getConfiguration() == 0) && 
				((properties.getConfiguration(this.toShowConf.getNameOfConfiguration()) == null) || 
						((int)(properties.getConfiguration(this.toShowConf.getNameOfConfiguration())) == 0))) {
			return data;
		} else {
			//Si ahora lo que no nos dan es un nodo ra�z, lo establecemos como el primero del grafo data
			this.root = rootConf.getConfiguration();
			Object auxRoot = properties.getConfiguration(this.rootConf.getNameOfConfiguration());
			if(this.root == null && auxRoot != null) {
				this.root = this.existsRelatedNode(data,(DiagramNode)auxRoot);
			}
			
			this.maxNodes = toShowConf.getConfiguration();
			if(this.maxNodes == 0 || this.maxNodes == -1) {
				this.maxNodes = (int) properties.getConfiguration(MaxNodesConfiguration.class);
			}
		}
		
		return target;
	}

	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		Collection<Class<? extends Configuration>> res = new ArrayList<>();
		
		if(this.rootConf.getConfiguration() == null) {
			res.add(this.rootConf.getClass());
		} 
		if(this.toShowConf.getConfiguration() == 0) {
			res.add(this.toShowConf.getClass());
		}
		
		return res;
	}

	//Configurable Methods
	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		ArrayList<Pair<String, Configuration>> conf = new ArrayList<>();
		
		conf.add(new Pair<>(rootConf.getNameOfConfiguration(), rootConf));
		conf.add(new Pair<>(toShowConf.getNameOfConfiguration(), toShowConf));
		
		return conf;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
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
			
			if(root && toShow) { //Ya est� todo configurado
				break;
			}
		}
	}

	protected abstract RootNodeConfiguration getRootNodeConfiguration(DiagramGraph data, Configurable properties);
}
