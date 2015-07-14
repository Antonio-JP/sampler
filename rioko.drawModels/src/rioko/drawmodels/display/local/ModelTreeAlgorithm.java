package rioko.drawmodels.display.local;

import rioko.drawmodels.configurations.ModelRootNodeConfiguration;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.display.DisplayProperties;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.graphabstraction.display.local.TreeContainerAlgorithm;

public class ModelTreeAlgorithm extends TreeContainerAlgorithm {

	@Override
	protected RootNodeConfiguration getRootNodeConfiguration(DiagramGraph data, DisplayProperties properties) {
		ModelRootNodeConfiguration rootConf = new ModelRootNodeConfiguration();
		
		if(data != null && properties != null) {
			ModelDiagram<?> model = ModelDiagram.getModelDiagramForGraph(data);
			rootConf.setModel(model);
			DiagramNode finalNode = null;
			for(DiagramNode node : model.getModelDiagram().vertexSet()) {
				if(node.equals(properties.getAttribute(DisplayOptions.ROOT_NODE))) {
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
