package rioko.drawmodels.wizards.listeners;

import java.util.Collection;
import java.util.List;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.revent.datachange.DataChangeEvent;

public abstract class AddRemoveListener<T> implements MouseListener {
	
	private AddRemoveTable sourceTable;
	private Object controled;
	private boolean add;
	
	private Class<T> parameter;

	public AddRemoveListener(AddRemoveTable sourceTable, boolean addOrRemove, Object controled, Class<T> parameter) {
		this.sourceTable = sourceTable;
		this.add = addOrRemove;
		this.controled = controled;
		
		this.parameter = parameter;
	}
	
	//Getters & Setters methods
	protected Object getControledObject() {
		return this.controled;
	}
	
	//MouseListener Methods
	@Override
	public void mouseDoubleClick(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseDown(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseUp(MouseEvent me) {
		if(this.add) {
			this.runAdd(me);
		} else {
			this.runRemove(me);
		}
	}

	//Proper methods
	private void runRemove(MouseEvent me) {
		TableItem[] itemSelected = sourceTable.getSelection();
		if(itemSelected.length > 0) {
			TableItem item = itemSelected[0];
			
			int data = Integer.parseInt(item.getText(0));
			
			if(data > 0 && data <= this.getSizeOfControled()) {
				this.removeFromControled(data-1);
			}
		}
		
		sourceTable.setInput(this.getInputFromControled());
		new DataChangeEvent(this.sourceTable);
	}

	private void runAdd(MouseEvent me) {
		// Lanzamos un pop-up ofreciendo el tipo de paso que queremos añadir
		for(T object : this.chooseAvaible(this.parameter)) {
			this.addToControled(object); 
		}
		
		sourceTable.setInput(this.getInputFromControled());
		new DataChangeEvent(this.sourceTable);
	}

	protected abstract Collection<T> chooseAvaible(Class<? extends T> extendClass);
	protected abstract List<?> getInputFromControled();
	protected abstract void removeFromControled(int index);
	protected abstract int getSizeOfControled();
	protected abstract void addToControled(T object);

}
