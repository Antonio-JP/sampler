package rioko.drawmodels.editors.zesteditor;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.graphabstraction.diagram.DiagramGraph;

public class SpecialInputZestEditor implements IEditorInput {

	private DiagramGraph model;
	private String name;
	private ZestProperties properties;
	
	//Builders
	public SpecialInputZestEditor(DiagramGraph model, ZestProperties initialConfiguration, String name) {
		this.model = model;
		this.properties = initialConfiguration;
		this.name = name;
	}
	
	//Getters
	public DiagramGraph getModel() {
		return model;
	}

	public ZestProperties getProperties() {
		return properties;
	}
		
	//IEditorInput methods
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) { return null; }

	@Override
	public boolean exists() { return true; }

	@Override
	public ImageDescriptor getImageDescriptor() { return null; }

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public IPersistableElement getPersistable() { return null; }

	@Override
	public String getToolTipText() { return "Rioko: model custom explorer"; }

}
