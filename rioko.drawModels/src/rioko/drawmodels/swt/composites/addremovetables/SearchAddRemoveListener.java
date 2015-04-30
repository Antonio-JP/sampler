package rioko.drawmodels.swt.composites.addremovetables;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.wizards.listeners.AddRemoveMouseListener;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.runtime.registers.RegisterSearchersCriteria;

public class SearchAddRemoveListener extends AddRemoveMouseListener<FilterOfVertex> {

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
				try {
					list.add((FilterOfVertex) Class.forName((String)obj).newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list;
	}

}
