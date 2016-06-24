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
import rioko.revent.REvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;

public class FiltersView extends ZestEditorDependingViewPart{	
	private AddRemoveTable filtersTable;
	private ConfigurationTable confTable;
		
	private JustFiltersBuilder controledListOfFilters;
	
	private DataChangeListener listenerForEditor = null;
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
		try {
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
			new DataChangeListener(this, this.filtersTable) {
				@Override
				public void run(DataChangeEvent event) {
//					filtersHasChanged = true;
					updateView();
					getCurrentEditor().updateView();
				}
			};
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
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
		}
		
		//Actualizamos los valores de la tabla de filtros
		if(this.getCurrentEditor() != null) {
			if(this.getCurrentEditor().getProperties().getFinalFilters() != this.controledListOfFilters) {
				this.controledListOfFilters = this.getCurrentEditor().getProperties().getFinalFilters();
			
				filtersTable.setInput(this.controledListOfFilters.getFiltersList());
			
				if(this.listenerForEditor != null) {
					REvent.removeListener(this.listenerForEditor);
				}
				this.listenerForEditor = new DataChangeListener(this, this.getCurrentEditor()) {
				
					@Override
					public void run(DataChangeEvent event) {
						updateView();
					}
				};
				//Actualizamos los listeners para el nuevo editor
				this.filtersTable.setMouseListener(new JustFiltersMouseListener(filtersTable, true, this.controledListOfFilters, FilterNestedBuilder.class), 
						new JustFiltersMouseListener(filtersTable, false, this.controledListOfFilters, FilterNestedBuilder.class));
			}
			//We set the models for the Configuration Table to the model in the Zest Editor
			confTable.setModel(this.getCurrentEditor().getModel());

			
		}
	}
	
	//IPartListener methods
	@Override
	protected void doBeforeChange(IWorkbenchPart part) { /* Do nothing */ }

	@Override
	protected void doWhenActivate(IWorkbenchPart part) {	
//		try {
//			//Nos aseguramos de si el editor ha cambiado
//			if(!this.getZestEditor().equals(this.getCurrentEditor())) {
//				//When activate a new ZestEditor, we set the getCurrentEditor() and the properties
//				this.getCurrentEditor() = this.getZestEditor();
//				
//				this.updateView();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
