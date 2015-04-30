package rioko.graphabstraction.display.configurations;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.collections.ListSet;

public abstract class RootNodeConfiguration implements ComboConfiguration {

	private DiagramNode root = null;
	
	protected DiagramGraph graph = null;
	
	//Builders
	public RootNodeConfiguration() { }
	
	public RootNodeConfiguration(DiagramNode root) {
		try {
			this.setRoot(root);
		} catch (BadConfigurationException e) {
			// Impossible Exception
			e.printStackTrace();
		}
	}

	//Setters
	public void setRoot(DiagramNode root) throws BadConfigurationException {
		if(root == null) {
			this.root = null;
			return;
		}
		
		if(graph == null) {
			this.root = root;
		} else if (graph.vertexSet().contains(root)){
			this.root = root;
		} else {
			throw new BadConfigurationException("The node is not in the selected graph");
		}
	}
	
	public void setGraph(DiagramGraph graph) {
		this.graph = graph;
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.COMBO_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		DiagramNode node = null;
		
		if(value instanceof DiagramNode) {
			node = this.classOfOptions().cast(value);
		} else if(value instanceof String) {
			node = this.getOptionByName((String)value);
		}
		this.setRoot(node);
	}

	@Override
	public DiagramNode getConfiguration() {
		return root;
	}

	@Override
	public String getTextConfiguration() {
		if(root == null) {
			return "null";
		}
		
		return root.getTitle();
	}

	@Override
	public Collection<DiagramNode> getPossibleOptions() {
		if(graph == null) {
			return new ArrayList<DiagramNode>();
		} else {
			return graph.vertexSet();
		}
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		ListSet<String> list = new ListSet<>();
		
		for(DiagramNode node : this.getPossibleOptions()) {
			list.add(node.getTitle());
		}
		
		return list;
	}

	@Override
	public Class<DiagramNode> classOfOptions() {
		return DiagramNode.class;
	}

	private DiagramNode getOptionByName(String value) {
		for(DiagramNode node : this.getPossibleOptions()) {
			if(node.getTitle().equals(value)) {
				return node;
			}
		}
		
		return null;
	}
}
