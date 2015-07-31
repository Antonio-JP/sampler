package rioko.drawmodels.views.listeners;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.drawmodels.algorithms.display.JustFiltersBuilder;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.wizards.listeners.AddRemoveListener;
import rioko.eclipse.registry.RegistryManagement;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.graphabstraction.runtime.registers.RegisterBuilderSteps;

public class JustFiltersMouseListener extends AddRemoveListener<FilterNestedBuilder> {

	public JustFiltersMouseListener(AddRemoveTable sourceTable, boolean addOrRemove, JustFiltersBuilder controled, Class<FilterNestedBuilder> parameter) {
		super(sourceTable, addOrRemove, controled, parameter);
	}

	//Getters & Setters
	@Override
	protected JustFiltersBuilder getControledObject() {
		return (JustFiltersBuilder)super.getControledObject();
	}
	
	//AddRemoveListener Methods
	@Override
	protected Collection<FilterNestedBuilder> chooseAvaible(Class<? extends FilterNestedBuilder> extendClass) {
		String[] possibleChoices = RegisterBuilderSteps.getRegisteredNames(extendClass).toArray(new String[0]);
		
		ListSelectionDialog dialog = new ListSelectionDialog(null, possibleChoices, ArrayContentProvider.getInstance(),
				            new LabelProvider(), "Select one of the avaible Steps");

		dialog.setTitle("Select Step");
		dialog.open();
		Object [] result = dialog.getResult();

		ArrayList<FilterNestedBuilder> list = new ArrayList<>();
		if(result != null) {
			for(Object obj : result) {
				FilterNestedBuilder newInstance = (FilterNestedBuilder) RegistryManagement.getInstance("rioko.graphabstraction.steps", (String)obj);
				if(newInstance != null) {
					list.add(newInstance);
				}
			}
		}
		
		return list;
	}

	@Override
	protected List<?> getInputFromControled() {
		return this.getControledObject().getFiltersList();
	}

	@Override
	protected void removeFromControled(int index) {
		this.getControledObject().removeFilter(this.getControledObject().getFiltersList().get(index));
	}

	@Override
	protected int getSizeOfControled() {
		return this.getControledObject().getFiltersList().size();
	}

	@Override
	protected void addToControled(FilterNestedBuilder filter) {
		this.getControledObject().addFilter(filter);
	}
}
