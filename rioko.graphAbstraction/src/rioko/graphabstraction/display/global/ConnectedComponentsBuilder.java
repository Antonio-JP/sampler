package rioko.graphabstraction.display.global;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.graphabstraction.display.configurations.StrongConnectionConfiguration;

public class ConnectedComponentsBuilder extends GlobalNestedBuilder {
	
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph target = data.getSimilarType();
		
		Object conf = properties.getConfiguration(new StrongConnectionConfiguration().getNameOfConfiguration());
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
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		ArrayList<Class<? extends Configuration>> confs = new ArrayList<>(1);
		
		confs.add(new StrongConnectionConfiguration().getClass());
		
		return confs;
	} 
}
