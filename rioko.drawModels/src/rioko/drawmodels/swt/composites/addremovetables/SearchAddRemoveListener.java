package rioko.drawmodels.swt.composites.addremovetables;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.wizards.listeners.AddRemoveMouseListener;
import rioko.eclipse.registry.RegistryManagement;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.graphabstraction.runtime.registers.RegisterSearchersCriteria;

public class SearchAddRemoveListener extends AddRemoveMouseListener<FilterOfVertex> {

	private static final String EXTENSION_ID_SEARCHES = "rioko.graphabstraction.searches";

	public SearchAddRemoveListener(AddRemoveTable sourceTable, boolean addOrRemove, ArrayList<FilterOfVertex> controledList) {
		super(sourceTable, addOrRemove, controledList, FilterOfVertex.class);
	}

	@Override
	protected Collection<FilterOfVertex> chooseAvaible(Class<? extends FilterOfVertex> extendClass) {
		@SuppressWarnings("unchecked")
		String[] possibleChoices = RegisterSearchersCriteria.getRegisteredNames((Class<FilterOfVertex>) extendClass).toArray(new String[0]);
		
		ListSelectionDialog dialog = new ListSelectionDialog(null, possibleChoices, ArrayContentProvider.getInstance(),
				            new LabelProvider(), "Select one of the avaible Steps");

		dialog.setTitle("Select Step");
		dialog.open();
		Object [] result = dialog.getResult();

		ArrayList<FilterOfVertex> list = new ArrayList<>();
		if(result != null) {
			for(Object obj : result) {
				FilterOfVertex filter = (FilterOfVertex) RegistryManagement.getInstance(EXTENSION_ID_SEARCHES, (String)obj);
				list.add(filter);
			}
		}
		
		return list;
	}

}
