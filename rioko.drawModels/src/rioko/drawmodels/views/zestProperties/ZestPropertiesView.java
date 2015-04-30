package rioko.drawmodels.views.zestProperties;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IWindowListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.events.WizardWindowEvent;
import rioko.drawmodels.events.listeners.WizardWindowListener;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.views.ZestEditorDependingViewPart;
import rioko.drawmodels.views.listeners.PropertiesChangeListener;
import rioko.drawmodels.wizards.AbstractWizard;
import rioko.drawmodels.wizards.CreateSpecialAlgorithmWizard;
import rioko.events.listeners.AbstractDataChangeListener;
//import rioko.events.listeners.GlobalChangeListener;

public class ZestPropertiesView extends ZestEditorDependingViewPart implements IWindowListener {
	private ConfigurationTable basicConf;
	private ConfigurationTable algConf;
		
//	private WizardWindowListener wizardListener = null;
	private PropertiesChangeListener listener = null;
	private ZestEditor activeEditor;
	private ZestProperties properties;
	
	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		
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
		
		//Creamos la lógica de la vista
		try {
			new AbstractDataChangeListener(this.basicConf, this) {
					
				@Override
				protected void dispose() { /* Do nothing */ }
				
				@Override
				public void onDataChange(Event arg0) {
					algConf.setNewConfigurable(properties.getAlgorithmConfigurable());
					properties.hasChanged(ZestEditor.UPDATE_ALL);
				}
			};
			
			new AbstractDataChangeListener(this.algConf, this) {
				
				@Override
				protected void dispose() { /* Do nothing */ }
				
				@Override
				public void onDataChange(Event arg0) {
					properties.hasChanged(ZestEditor.UPDATE_ALL);
				}
			};
		} catch (Exception e) {
			// Impossible Exception
			e.printStackTrace();
		}
		
		//Creamos el lstener para actualizar la vista al cerrar el wizard correspondiente
		/*this.wizardListener = */new WizardWindowListener(AbstractWizard.class,this) {

			@Override
			protected void openWizard(WizardWindowEvent wwe) { /* Do nothing */}

			@Override
			protected void closeWizard(WizardWindowEvent wwe) {
				updateView();
			}
			
		};
		
		this.partActivated(this.getSite().getPage().getActiveEditor());
	}
	
	//PRIVATE METHODS

	@Override
	public void updateView() {
		if(this.isActiveZest()) {	
			if(this.activeEditor != this.getZestEditor() || this.properties != this.getZestEditor().getProperties()) {
				this.activeEditor = this.getZestEditor();
				this.properties = this.activeEditor.getProperties();
				if(this.listener != null) {
					PropertiesChangeListener.destroyListener(listener);
				}
				try {
					this.listener = new PropertiesChangeListener(this.properties, this);
				} catch (Exception e) {
					// Impossible Exception
					e.printStackTrace();
				}
				
				this.properties.hasChanged(ZestEditor.UPDATE_ALL);
			}
			
			basicConf.setVisible(true);
			algConf.setVisible(true);
			
			//Actualizamos el valor de la tabla
			basicConf.setModel(this.getZestEditor().getModel());
			algConf.setModel(this.getZestEditor().getModel());
			basicConf.setNewConfigurable(this.properties.getGenericConfigurable());
			algConf.setNewConfigurable(this.properties.getAlgorithmConfigurable());
		} else {
			if(basicConf != null) {
				basicConf.setVisible(false);
			}
			if(algConf != null) {
				algConf.setVisible(false);
			}
		}
	}
	
	@Override
	protected void doBeforeChange(IWorkbenchPart part) { }

	@Override
	protected void doWhenActivate(IWorkbenchPart part) {
		this.updateView();
	}
	
	//IwindowListener methods
	@Override
	public void windowActivated(IWorkbenchWindow window) { /* Do nothing */ }

	@Override
	public void windowClosed(IWorkbenchWindow window) { 
		if(window instanceof Wizard) {
			if(window instanceof CreateSpecialAlgorithmWizard) {
				this.updateView();
			}
		}
	}

	@Override
	public void windowDeactivated(IWorkbenchWindow window) { /* Do nothing */ }

	@Override
	public void windowOpened(IWorkbenchWindow window) { /* Do nothing */ }
}
