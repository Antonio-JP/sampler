package rioko.drawmodels.diagram.XMIDiagram;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;

import rioko.utilities.Copiable;
import rioko.draw2d.figures.HorizontalFigure;

public abstract class AbstractAttribute implements Copiable{
	private String name = null;
	
	private IFigure figure = null;
	
	//Builders
	protected AbstractAttribute() {}
	
	protected AbstractAttribute(String name) {
		this.name = name;
	}
	
	//Getters y setters
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	//Public methods
	public IFigure getFigure()
	{
		if(this.figure == null) {
			this.figure = new HorizontalFigure();
		
			this.figure.add(new Label(" - "+this.name+": "));
			this.figure.add(this.getValueFigure());
		}
		
		return this.figure;
	}
	
	//Abstract methods
	@Override
	public abstract AbstractAttribute copy();
	
	public abstract Object getValue();
	
	public abstract IFigure getValueFigure();
}
