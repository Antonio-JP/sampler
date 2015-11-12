package rioko.drawmodels.views.zestProperties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPart;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.views.ZestEditorDependingViewPart;
import rioko.drawmodels.views.listeners.ZestPropertiesListener;
import rioko.revent.REvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.revent.datachange.DataChangeListener;
import rioko.utilities.Log;

public class ZestPropertiesView extends ZestEditorDependingViewPart /*implements IWindowListener*/ {
	private ConfigurationTable basicConf;
	private ConfigurationTable algConf;
		
//	private PropertiesChangeListener listener = null;
	private ZestEditor activeEditor;
	private ZestProperties properties;

	private DataChangeListener editorListener;
	private ZestPropertiesListener propertiesListener;
	
	@Override
	protected void createUIPart(Composite parent) {
		Composite basicPanel = new Composite(parent, SWT.NONE);
		
		//Creamos el layout para la vista
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		basicPanel.setLayout(layout);
		
		//Creamos la primera tabla
		this.basicConf = new ConfigurationTable(basicPanel, SWT.NONE, null);
		this.basicConf.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//Creamos la segunda tabla
		this.algConf = new ConfigurationTable(basicPanel, SWT.NONE, null);
		this.algConf.setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	@Override
	protected void createLogicPart(Composite parent) {
		this.createEditorListener();
		this.createPropertiesListener();
		
//		try {
//			new AbstractDataChangeListener(this.basicConf, this) {
//				
//				@Override
//				public void onDataChange(Event arg0) {
//					algConf.setNewConfigurable(properties.getAlgorithmConfigurable());
//					if(arg0 instanceof PropertiesChangeEvent) {
//						properties.hasChanged(((PropertiesChangeEvent) arg0).getChanges());
//					} else {
//						properties.hasChanged(ZestEditor.UPDATE_ALL);
//					}
//				}
//			};
//			
//			new AbstractDataChangeListener(this.algConf, this) {
//				
//				@Override
//				public void onDataChange(Event arg0) {
//					if(arg0 instanceof PropertiesChangeEvent) {
//						properties.hasChanged(((PropertiesChangeEvent) arg0).getChanges());
//					} else {
//						properties.hasChanged(ZestEditor.UPDATE_ALL);
//					}
//				}
//			};
//		} catch (Exception e) {
//			// Impossible Exception
//			e.printStackTrace();
//		}
//		
//		//Creamos el lstener para actualizar la vista al cerrar el wizard correspondiente
//		new WizardWindowListener(AbstractWizard.class,this) {
//
//			@Override
//			protected void openWizard(WizardWindowEvent wwe) { /* Do nothing */}
//
//			@Override
//			protected void closeWizard(WizardWindowEvent wwe) {
//				updateView();
//			}
//			
//		};
	}
	
	

	//PRIVATE METHODS

	@Override
	public void updateView() {
//		if(this.isActiveZest()) {	
//			if(this.activeEditor != this.getZestEditor() || this.properties != this.getZestEditor().getProperties()) {
//				//Case when the Editor has changed -- We update the shole view
//				this.activeEditor = this.getZestEditor();
//				this.properties = this.activeEditor.getProperties();
//				if(this.listener != null) {
//					PropertiesChangeListener.destroyListener(listener);
//				}
////				try {
////					this.listener = new PropertiesChangeListener(this.properties, this);
////				} catch (Exception e) {
////					// Impossible Exception
////					e.printStackTrace();
////				}
//				
//				//We put the Model of the Editor to the Tables
//				basicConf.setModel(this.activeEditor.getModel());
//				algConf.setModel(this.activeEditor.getModel());
//				
//				this.properties.hasChanged(ZestEditor.UPDATE_ALL);
//			}
//			
//			//Update the values of the tables
//			basicConf.setVisible(true);
//			algConf.setVisible(true);
//			
//			basicConf.setNewConfigurable(this.properties.getGenericConfigurable());
//			algConf.setNewConfigurable(this.properties.getAlgorithmConfigurable());
//		} else {
//			//Else, we put the tables out of view
//			if(basicConf != null) {
//				basicConf.setVisible(false);
//			}
//			if(algConf != null) {
//				algConf.setVisible(false);
//			}
//		}
	}
	
	@Override
	protected void doBeforeChange(IWorkbenchPart part) { 
		/* Nothing to do here */
	}

	@Override
	protected void doWhenActivate(IWorkbenchPart part) {
		if(part instanceof ZestEditor) {
			//When activate a new ZestEditor, we set the activeEditor and the properties
			this.activeEditor = this.getZestEditor();
			this.properties = this.activeEditor.getProperties();
			
			this.changeBasicTable();
			this.changeAlgTable();
			
			//Make the tables visible
			basicConf.setVisible(true);
			algConf.setVisible(true);
			
			//We set the model for the tables to the model we are opening
			basicConf.setModel(this.activeEditor.getModel());
			this.algConf.setModel(this.activeEditor.getModel());
			
			//Create the new Listeners for the Editor and the Properties
			this.createEditorListener();
			this.createPropertiesListener();
		}
	}
	
	//IwindowListener methods
//	@Override
//	public void windowActivated(IWorkbenchWindow window) { /* Do nothing */ }
//
//	@Override
//	public void windowClosed(IWorkbenchWindow window) { 
//		if(window instanceof Wizard) {
//			if(window instanceof CreateSpecialAlgorithmWizard) {
//				this.updateView();
//			}
//		}
//	}
//
//	@Override
//	public void windowDeactivated(IWorkbenchWindow window) { /* Do nothing */ }
//
//	@Override
//	public void windowOpened(IWorkbenchWindow window) { /* Do nothing */ }

	private void changeAlgTable() {
		//This method update the algorithm table from the Properties of the View
		algConf.setNewConfigurable(this.properties.getAlgorithmConfigurable());
	}

	private void changeBasicTable() {
		//This method update the Generic table from the Properties of the View
		basicConf.setNewConfigurable(this.properties.getGenericConfigurable());
	}
	
	private void createEditorListener() {
		if(this.activeEditor != null) {
			try{	
				if(editorListener != null) {
					REvent.removeListener(editorListener);
				}
				editorListener = new DataChangeListener(this,this.activeEditor) {
					@Override
					public void run(DataChangeEvent event) {
						createPropertiesListener();
					}				
				};
			} catch (Exception e) {
				// Impossible Exception
				Log.exception(e);
			}
		}
	}
	
	private void createPropertiesListener() {
		if(this.properties != null) {
			try{	
				if(propertiesListener != null) {
					REvent.removeListener(propertiesListener);
				}
				propertiesListener = new ZestPropertiesListener(this.properties, this) {
	
					@Override
					protected void doWhenGenericChange() {
						// We update the generic table
						changeBasicTable();
					}
	
					@Override
					protected void doWhenAlgorithmChange() {
						// We update the algorithm table
						changeAlgTable();
						
					}

					@Override
					protected void doWhenFiltersChange() { /* Do nothing */	}
					
				};
			} catch (Exception e) {
				// Impossible Exception
				Log.exception(e);
			}
		}
	}
}