package rioko.drawmodels.wizards.listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.composites.AddRemoveTable;

public abstract class AddRemoveMouseListener<T> implements MouseListener {

	private AddRemoveTable sourceTable;
	private ArrayList<T> controledList;
	private boolean add;
	
	private Class<T> paremeter;
	
	public AddRemoveMouseListener(AddRemoveTable sourceTable, boolean addOrRemove, ArrayList<T> controledList, Class<T> paremeter)
	{
		this.sourceTable = sourceTable;
		this.add = addOrRemove;
		this.controledList = controledList;
		this.paremeter = paremeter;
	}
	
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

	private void runRemove(MouseEvent me) {
		TableItem[] itemSelected = sourceTable.getSelection();
		if(itemSelected.length > 0) {
			TableItem item = itemSelected[0];
			
			int data = Integer.parseInt(item.getText(0));
			
			if(data > 0 && data <= controledList.size()) {
				controledList.remove(data-1);
			}
		}
		
		sourceTable.setInput(controledList);
	}

	private void runAdd(MouseEvent me) {
		// Lanzamos un pop-up ofreciendo el tipo de paso que queremos añadir
		for(T object : this.chooseAvaible(this.paremeter)) {
			controledList.add(object); 
		}
		
		sourceTable.setInput(controledList);
	}
	
	protected abstract Collection<T> chooseAvaible(Class<? extends T> extendClass);
}
