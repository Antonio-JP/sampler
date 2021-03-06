package rioko.drawmodels.swt.composites.addremovetables;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.wizards.listeners.AddRemoveMouseListener;
import rioko.eclipse.registry.RegistryManagement;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.graphabstraction.runtime.registers.RegisterBuilderSteps;

public class GraphBuilderAddRemoveListener<T extends NestedGraphBuilder> extends AddRemoveMouseListener<T> {

	public GraphBuilderAddRemoveListener(AddRemoveTable sourceTable, boolean addOrRemove, List<T> controledList, Class<T> parameter) {
		super(sourceTable, addOrRemove, controledList, parameter);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<T> chooseAvaible(Class<? extends T> extendClass) {
		String[] possibleChoices = RegisterBuilderSteps.getRegisteredNames(extendClass).toArray(new String[0]);
		
		ListSelectionDialog dialog = new ListSelectionDialog(null, possibleChoices, ArrayContentProvider.getInstance(),
				            new LabelProvider(), "Select one of the avaible Steps");

		dialog.setTitle("Select Step");
		dialog.open();
		Object [] result = dialog.getResult();

		ArrayList<T> list = new ArrayList<>();
		if(result != null) {
			for(Object obj : result) {
				T newInstance = (T) RegistryManagement.getInstance("rioko.graphabstraction.steps", (String)obj);
				if(newInstance != null) {
					list.add(newInstance);
				}
			}
		}
		
		return list;
	}
}
