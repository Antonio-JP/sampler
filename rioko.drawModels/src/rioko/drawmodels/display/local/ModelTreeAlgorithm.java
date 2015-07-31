package rioko.drawmodels.display.local;

import rioko.drawmodels.configurations.ModelRootNodeConfiguration;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.graphabstraction.display.local.TreeContainerAlgorithm;

public class ModelTreeAlgorithm extends TreeContainerAlgorithm {

	@Override
	protected RootNodeConfiguration getRootNodeConfiguration(DiagramGraph data, Configurable properties) {
		ModelRootNodeConfiguration rootConf = new ModelRootNodeConfiguration();
		
		if(data != null && properties != null) {
			ModelDiagram<?> model = ModelDiagram.getModelDiagramForGraph(data);
			rootConf.setModel(model);
			DiagramNode finalNode = null;
			for(DiagramNode node : model.getModelDiagram().vertexSet()) {
				if(node.equals(properties.getConfiguration(RootNodeConfiguration.class))) {
					finalNode = node;
					break;
				}
			}
			
			if(finalNode != null) {
				try {
					rootConf.setConfiguration(finalNode);
				} catch (BadConfigurationException | BadArgumentException e) {
					// Impossible Exception
					e.printStackTrace();
				}
			}
		}
		
		return rootConf;
	}

}
