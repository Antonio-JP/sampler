package rioko.drawmodels.swt.composites.addremovetables;

import java.util.ArrayList;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.graphabstraction.configurations.Configurable;

public class TableConfigurationListener<T extends Configurable> implements MouseListener {
	
	private AddRemoveTable sourceTable;
	private ArrayList<T> controledList;
	private ConfigurationTable targetTable;

	public TableConfigurationListener(AddRemoveTable sourceTable, ArrayList<T> controledList, ConfigurationTable targetTable) {
		this.sourceTable = sourceTable;
		this.controledList = controledList;
		this.targetTable = targetTable;
	}

	@Override
	public void mouseDoubleClick(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseDown(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseUp(MouseEvent me) {
		TableItem[] itemSelected = sourceTable.getSelection();
		if(itemSelected.length > 0) {
			TableItem item = itemSelected[0];
			
			int data = Integer.parseInt(item.getText(0));
			
			if(data > 0 && data <= controledList.size()) {
				//Actualizamos el Configurable de la tabla de destino
				this.targetTable.setNewConfigurable(controledList.get(data-1));
			}
		}
	}

}
