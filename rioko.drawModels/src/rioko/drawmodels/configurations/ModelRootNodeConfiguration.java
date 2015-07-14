package rioko.drawmodels.configurations;

import org.eclipse.jface.dialogs.Dialog;

import rioko.drawmodels.diagram.ModelDepending;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.dialogs.SearchDialog;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.DialogConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.configurations.RootNodeConfiguration;
import rioko.utilities.Log;

public class ModelRootNodeConfiguration extends RootNodeConfiguration implements ModelDepending, DialogConfiguration {
	
	//Builders
	public ModelRootNodeConfiguration() { }
	
	public ModelRootNodeConfiguration(DiagramNode root) {
		super(root);
	}

	@Override
	public boolean hasModel() {
		return this.graph != null;
	}

	@Override
	public void setModel(ModelDiagram<?> model) {
		this.setGraph(model.getModelDiagram());
	}
	
	@Override
	public Dialog getDialog() {
		return new SearchDialog(null, ModelDiagram.getModelDiagramForGraph(this.graph));
	}
	
	@Override
	public ModelRootNodeConfiguration copy() {
		ModelRootNodeConfiguration res = new ModelRootNodeConfiguration();
		
		res.graph = this.graph;
		try {
			res.setRoot(this.getConfiguration());
		} catch (BadConfigurationException e) {
			// Impossible Exception
			Log.exception(e);
		}
		return res;
	}
	
	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.DIALOG_CONFIGURATION;
	}
}
