package rioko.drawmodels.views.filtersView;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;

import rioko.drawmodels.algorithms.display.JustFiltersBuilder;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.views.ZestEditorDependingViewPart;
import rioko.drawmodels.views.listeners.JustFiltersMouseListener;
import rioko.graphabstraction.display.FilterNestedBuilder;

public class FiltersView extends ZestEditorDependingViewPart{	
	private AddRemoveTable filtersTable;
	private ConfigurationTable confTable;
	
	private ZestEditor activeEditor = null;
	
	private JustFiltersBuilder controledListOfFilters;
//	private boolean filtersHasChanged = false;
	
//	private static HashMap<ZestEditor, ListenedArrayList<FilterNestedBuilder>> filtersForEditor = new HashMap<>();
	
	//Creating Part methods
	@Override
	protected void createUIPart(Composite parent) {
		//Creamos el Composite para toda la vista
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.verticalSpacing = 2;
		composite.setLayout(layout);
				
		//Creamos la tabla con la lista de filtros
		this.filtersTable = new AddRemoveTable(composite, SWT.NONE, "Filters");
		this.filtersTable.setLayoutData(new GridData(GridData.FILL_BOTH));
				
		//Creamos la tabla para configurar los filtros
		this.confTable = new ConfigurationTable(composite, SWT.NONE, null);
		this.confTable.setLayoutData(new GridData(GridData.FILL_BOTH));		
	}

	@Override
	protected void createLogicPart(Composite parent) {
		//Añadimos un wizardListener para actualizar la vista cuando cambie la lista
//		try {
//			new AbstractDataChangeListener(this.controledListOfFilters, this) {
//				
//				@Override
//				protected void dispose() { /* Do nothing */}
//				
//				@Override
//				public void onDataChange(Event arg0) {
//					filtersHasChanged = true;
//					updateView();
//				}
//			};
//					
//			new AbstractDataChangeListener(this.confTable, this) {
//				
//				@Override
//				protected void dispose() { /* Do nothing */}
//				
//				@Override
//				public void onDataChange(Event arg0) {
//					filtersHasChanged = true;
//					updateView();
//				}
//			};
//		} catch (Exception e) {
//			// Impossible exception
//			e.printStackTrace();
//		}
//		
		//We add a listener to get the click over the Table and update the configuration table
		this.filtersTable.setTableListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent e) { /* Do nothing */ }

			@Override
			public void mouseDown(MouseEvent e) { /* Do nothing */ }

			@Override
			public void mouseUp(MouseEvent e) {
				TableItem[] selections = filtersTable.getSelection();
				if(selections.length > 0) {
					//Set the configurable of the table to the configurable of the selected 
					confTable.setNewConfigurable(controledListOfFilters.getFiltersList().get(
							Integer.parseInt(filtersTable.getSelection()[0].getText(0))-1));
				}
			}
			
		});
	}
	
	//ZestDependingViewPart methods
	@Override
	public void updateView() {
		if(this.isActiveZest()) {
			//Establecemos como visibles las tablas
			filtersTable.setVisible(true);
			confTable.setVisible(true);
			
			//Actualizamos los valores de la tabla de filtros
			filtersTable.setInput(this.controledListOfFilters.getFiltersList());
		}
//		if(this.isActiveZest()) {						
//			//Establecemos como visibles las tablas
//			filtersTable.setVisible(true);
//			confTable.setVisible(true);
//			confTable.setModel(this.getZestEditor().getModel());
//			
//			//Ahora establecemos los valores de las tablas
//			filtersTable.setInput(this.controledListOfFilters);
//			
//			//Reinsertamos los filtros en las propiedades del editor
//			if(this.filtersHasChanged) {
//				this.activeEditor.getProperties().removeAllFilterFromPostFilters();
//				this.activeEditor.getProperties().addAllFilterToPostFilters(controledListOfFilters);
//				
//				this.filtersHasChanged=false;
//			}
//		} else {
//			if(this.activeEditor != null) {				
//				if(this.controledListOfFilters != null) {
//					filtersForEditor.put(this.activeEditor, this.controledListOfFilters);
//					this.controledListOfFilters = null;
//				}
//				
//				this.activeEditor = null;
//			}
//			
//			if(filtersTable != null) {
//				filtersTable.setVisible(false);
//			}
//			if(confTable != null) {
//				confTable.setVisible(false);
//			}
//		}
	}
	
	//IPartListener methods
	@Override
	protected void doBeforeChange(IWorkbenchPart part) { /* Do nothing */ }

	@Override
	protected void doWhenActivate(IWorkbenchPart part) {	
		try {
			//Nos aseguramos de si el editor ha cambiado
			if(!this.getZestEditor().equals(this.activeEditor)) {
				//When activate a new ZestEditor, we set the activeEditor and the properties
				this.activeEditor = this.getZestEditor();
				this.controledListOfFilters = this.activeEditor.getProperties().getFinalFilters();
				
				//We set the models for the Configuration Table to the model in the Zest Editor
				confTable.setModel(this.activeEditor.getModel());
//				//Actualizamos la lista de filtros para el editor actual
//				filtersForEditor.put(lastEditor, controledListOfFilters);
//				this.lastEditor = this.getZestEditor();
//				this.controledListOfFilters = filtersForEditor.get(this.lastEditor);
//				if(this.controledListOfFilters == null) {
//					this.controledListOfFilters = new ListenedArrayList<>();
//				}
//				this.filtersHasChanged=true;
//				
				//Actualizamos los listeners para el nuevo editor
				this.filtersTable.setMouseListener(new JustFiltersMouseListener(filtersTable, true, this.controledListOfFilters, FilterNestedBuilder.class), 
						new JustFiltersMouseListener(filtersTable, false, this.controledListOfFilters, FilterNestedBuilder.class));
//				this.filtersTable.setTableListener(new TableConfigurationListener<FilterNestedBuilder>(this.filtersTable, this.controledListOfFilters, this.confTable));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
