package rioko.drawmodels.views.filtersView;

import java.util.HashMap;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWorkbenchPart;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.addremovetables.GraphBuilderAddRemoveListener;
import rioko.drawmodels.swt.composites.addremovetables.TableConfigurationListener;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.views.ZestEditorDependingViewPart;
import rioko.events.listeners.AbstractDataChangeListener;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.utilities.collections.ListenedArrayList;

public class FiltersView extends ZestEditorDependingViewPart{	
	private AddRemoveTable filtersTable;
	private ConfigurationTable confTable;
	
	private ZestEditor lastEditor = null;
	
	private ListenedArrayList<FilterNestedBuilder> controledListOfFilters = new ListenedArrayList<>();
	private boolean filtersHasChanged = false;
	private static HashMap<ZestEditor, ListenedArrayList<FilterNestedBuilder>> filtersForEditor = new HashMap<>();
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
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
		
		//Añadimos un wizardListener para actualizar la vista cuando cambie la lista
		try {
			new AbstractDataChangeListener(this.controledListOfFilters, this) {
				
				@Override
				protected void dispose() { /* Do nothing */}
				
				@Override
				public void onDataChange(Event arg0) {
					filtersHasChanged = true;
					updateView();
				}
			};
			
			new AbstractDataChangeListener(this.confTable, this) {
				
				@Override
				protected void dispose() { /* Do nothing */}
				
				@Override
				public void onDataChange(Event arg0) {
					filtersHasChanged = true;
					updateView();
				}
			};
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
		
		//Creamos el lstener para actualizar la vista al cerrar el wizard correspondiente
		this.partActivated(this.getSite().getPage().getActiveEditor());
	}
	
	//ZestDependingViewPart methods
	@Override
	public void updateView() {
		if(this.isActiveZest()) {						
			//Establecemos como visibles las tablas
			filtersTable.setVisible(true);
			confTable.setVisible(true);
			confTable.setModel(this.getZestEditor().getModel());
			
			//Ahora establecemos los valores de las tablas
			filtersTable.setInput(this.controledListOfFilters);
			
			//Reinsertamos los filtros en las propiedades del editor
			if(this.filtersHasChanged) {
				this.lastEditor.getProperties().removeAllFilterFromPostFilters();
				this.lastEditor.getProperties().addAllFilterToPostFilters(controledListOfFilters);
				
				this.filtersHasChanged=false;
			}
		} else {
			if(this.lastEditor != null) {				
				if(this.controledListOfFilters != null) {
					filtersForEditor.put(this.lastEditor, this.controledListOfFilters);
					this.controledListOfFilters = null;
				}
				
				this.lastEditor = null;
			}
			
			if(filtersTable != null) {
				filtersTable.setVisible(false);
			}
			if(confTable != null) {
				confTable.setVisible(false);
			}
		}
	}
	
	//IPartListener methods
	@Override
	protected void doBeforeChange(IWorkbenchPart part) { }

	@Override
	protected void doWhenActivate(IWorkbenchPart part) {		
		try {
			//Nos aseguramos de si el editor ha cambiado
			if(!this.getZestEditor().equals(this.lastEditor)) {
				//Actualizamos la lista de filtros para el editor actual
				filtersForEditor.put(lastEditor, controledListOfFilters);
				this.lastEditor = this.getZestEditor();
				this.controledListOfFilters = filtersForEditor.get(this.lastEditor);
				if(this.controledListOfFilters == null) {
					this.controledListOfFilters = new ListenedArrayList<>();
				}
				this.filtersHasChanged=true;
				
				//Actualizamos los listeners para el nuevo editor
				this.filtersTable.setMouseListener(new GraphBuilderAddRemoveListener<FilterNestedBuilder>(filtersTable, true, this.controledListOfFilters, FilterNestedBuilder.class), 
						new GraphBuilderAddRemoveListener<FilterNestedBuilder>(filtersTable, false, this.controledListOfFilters, FilterNestedBuilder.class));
				this.filtersTable.setTableListener(new TableConfigurationListener<FilterNestedBuilder>(this.filtersTable, this.controledListOfFilters, this.confTable));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
