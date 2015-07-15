package rioko.graphabstraction.diagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import rioko.graphabstraction.diagram.AbstractAttribute;

public class StringAttribute extends AbstractAttribute {
	
	private String value;
	
	//Builders
	public StringAttribute() {super();}
	
	public StringAttribute(String name) {
		super(name);
		
		value = null;
	}
	
	public StringAttribute(String name, String value) {
		super(name);
		
		this.value = value;
	}
	
	//Getters & Setters
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	//Public methods

	//Override methods
	@Override
	public IFigure getValueFigure() {
		return new Label(this.value);
	}

	@Override
	public AbstractAttribute copy() {
		StringAttribute copy = new StringAttribute();
		
		copy.setName(this.getName());
		copy.setValue(this.getValue());
		
		return copy;
	}

}
