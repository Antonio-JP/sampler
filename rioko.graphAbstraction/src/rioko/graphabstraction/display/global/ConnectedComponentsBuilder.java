package rioko.graphabstraction.display.global;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.display.GlobalNestedBuilder;

public class ConnectedComponentsBuilder extends GlobalNestedBuilder {

	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph target = data.getSimilarType();
		
		Object conf = properties.getConfiguration(DisplayOptions.STRONG_BOOLEAN.toString());
		if(!(conf instanceof Boolean)) {
			return data;
		}
		
		Boolean strong = (Boolean)conf;
		
		for(Set<DiagramNode> component : data.getConnectedComponents(strong)) {
			target.addVertex(target.getComposeVertexFactory().createVertex(component));
		}
		
		return target;
	}
	
	//This algorithm requires a Boolean
	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		ArrayList<DisplayOptions> confs = new ArrayList<>(1);
		
		confs.add(DisplayOptions.STRONG_BOOLEAN);
		return confs;
	} 

}
