package rioko.drawmodels.wizards.listeners;

import java.util.List;

import rioko.drawmodels.swt.composites.AddRemoveTable;

public abstract class AddRemoveMouseListener<T> extends AddRemoveListener<T> {
	
	public AddRemoveMouseListener(AddRemoveTable sourceTable, boolean addOrRemove, List<T> controledList, Class<T> parameter)
	{
		super(sourceTable, addOrRemove, controledList, parameter);
	}
	
	//Getters & Setters
	@SuppressWarnings("unchecked")
	@Override
	protected List<T> getControledObject() {
		return (List<T>)super.getControledObject();
	}

	//AddRemoveListener Methods
	@Override
	protected List<?> getInputFromControled() {
		return this.getControledObject();
	}

	@Override
	protected void removeFromControled(int index) {
		this.getControledObject().remove(index);
	}

	@Override
	protected int getSizeOfControled() {
		return this.getControledObject().size();
	}

	@Override
	protected void addToControled(T object) {
		this.getControledObject().add(object);
	}
}
