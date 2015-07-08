package rioko.drawmodels.wizards.pages.spAlgPages;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.swt.StringTableRow;
import rioko.drawmodels.swt.composites.CheckButton;
import rioko.drawmodels.swt.composites.RadioButtonLine;
import rioko.drawmodels.swt.composites.StringTable;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.wizards.SelectSpecialAlgorithmWizard;
import rioko.drawmodels.wizards.pages.CustomWizardPage;
import rioko.events.listeners.AbstractDataChangeListener;

public class SelectExistingAlgorithmWizardPage extends CustomWizardPage {
	
	//Private attributes
	private ZestProperties generalConfiguration;
	private DiagramGraph model = null;
	
	private RadioButtonLine radioButtons;
	
	private StringTable existingAlg;
	
	private Text newAlg;
	private CheckButton saveNewAlgorithm;
	
	private ConfigurationTable generalConf, initConf;
	
	//Auxiliary attributes
	private int prevSelected = -1;
	
	//Builders
	public SelectExistingAlgorithmWizardPage(ZestProperties properties, DiagramGraph graph) {
		super("Select Existing Algorithm");
		
		setTitle("Select Existing Algorithm");
	    setDescription("Select an existing algorithm to use or create a new one");
	    
	    this.generalConfiguration = properties;
	    this.model = graph;
	}

	public SelectExistingAlgorithmWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}

	//Getters & Setters	
	public ZestProperties getConfiguration() {
		return this.generalConfiguration;
	}

	//IWizardPage methods
	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
	    
		//Creamos el layout básico para el conjunto
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		container.setLayout(layout);
		
		try {
			//Creamos la parte visual de la página
			//Primera fila
			this.radioButtons = new RadioButtonLine(container, SWT.NONE, 2, new String[]{"Use an existing algorithm", "Create a new Algorithm"});
			this.radioButtons.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
			
			//Segunda fila
			Composite secondRow = new Composite(container, SWT.NONE);
			secondRow.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout scnRowLayout = new GridLayout();
			scnRowLayout.numColumns = 2;
			scnRowLayout.horizontalSpacing = 4;
			secondRow.setLayout(scnRowLayout);
			
			existingAlg = new StringTable(secondRow, SWT.NONE, "Existing Algorithms");
			existingAlg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_BEGINNING));
			
			Composite compNewAlg = new Composite(secondRow, SWT.NONE);
			GridLayout auxLayout = new GridLayout();
			auxLayout.numColumns = 1;
			auxLayout.verticalSpacing = 2;
			compNewAlg.setLayout(auxLayout);
			compNewAlg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
			
			newAlg = new Text(compNewAlg, SWT.BORDER);
			newAlg.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
			
			saveNewAlgorithm = new CheckButton(compNewAlg, "Save the new Algorithm");
			saveNewAlgorithm.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		
			//Tercera fila
			Composite thirdRow = new Composite(container, SWT.NONE);
			thirdRow.setLayoutData(new GridData(GridData.FILL_BOTH));
			GridLayout thirdRowLayout = new GridLayout();
			thirdRowLayout.numColumns = 2;
			thirdRowLayout.horizontalSpacing = 4;
			thirdRow.setLayout(thirdRowLayout);
			
			ModelDiagram model = new ModelDiagram(this.model);
			generalConf = new ConfigurationTable(thirdRow, SWT.NONE, model);
			generalConf.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
			
			initConf = new ConfigurationTable(thirdRow, SWT.NONE, model);
			initConf.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_BOTH));
			
			generalConf.setNewConfigurable(this.generalConfiguration.getGenericConfigurable());
			initConf.setNewConfigurable(this.generalConfiguration.getAlgorithmConfigurable());
			
			try {
				new AbstractDataChangeListener(this.generalConf, this) {
						
					@Override
					protected void dispose() { /* Do nothing */ }
					
					@Override
					public void onDataChange(Event arg0) {
						updatePage();
					}
				};
				
				new AbstractDataChangeListener(this.initConf, this) {
					
					@Override
					protected void dispose() { /* Do nothing */ }
					
					@Override
					public void onDataChange(Event arg0) {
						updatePage();
					}
				};
			} catch (Exception e) {
				// Impossible Exception
				e.printStackTrace();
			}
			
			//Creamos la parte de control de la página
			//Control de clicks
			MouseListener updateOnClick = new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent me) { /* Do nothing */}

				@Override
				public void mouseDown(MouseEvent me) { /* Do nothing */ }

				@Override
				public void mouseUp(MouseEvent me) {
						updatePage();
				}
			};
			
			this.radioButtons.addMouseListener(updateOnClick);
			this.existingAlg.addTableListener(updateOnClick);
			
			//Creamos el wizardListener para las teclas en el cuadro de texto
			this.newAlg.addKeyListener(new KeyListener() {

				@Override
				public void keyPressed(KeyEvent arg0) { /* Do nothing */ }

				@Override
				public void keyReleased(KeyEvent arg0) {
					updatePage();
				}
				
			});
			
			//Creamos el contenido (que no cambia) de los algoritmStringTableRowes
			ArrayList<StringTableRow> list = new ArrayList<>();
			for(NestedBuilderAlgorithm algorithm : RegisterBuilderAlgorithm.getRegisteredAlgorithms()) {
				if(this.getWizard() instanceof SelectSpecialAlgorithmWizard) {
					if(algorithm != ((SelectSpecialAlgorithmWizard)this.getWizard()).getAlgorithm()) {
						list.add(new StringTableRow(algorithm.getAlgorithmName()));
					}
				}
			}
			this.existingAlg.setInput(list.toArray(new StringTableRow[0]));
	    
			this.updatePage();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// required to avoid an error in the system
	    setControl(container);
	    setPageComplete(false);

	}
	
	@Override
	public IWizardPage getNextPage() {
		if(this.prevSelected == 0) {
			if(this.existingAlg.getSelection() != null) {
				return null;
			}
		}
		
		return super.getNextPage();
	}

	@Override
	public boolean isWizardFinishable() {
		return true;
	}

	//Private methods
	public synchronized void updatePage() {
		if(prevSelected != radioButtons.getSelection()) {
			prevSelected = radioButtons.getSelection();
		}
		
		if(this.prevSelected == 0) { //Seleccionado un algoritmo antiguo
			this.newAlg.setEditable(false);
			this.saveNewAlgorithm.setEnabled(false);
			this.existingAlg.setEnabled(true);
			
			if(this.existingAlg.getSelection() != null) {
				this.generalConfiguration.changeNestedAlgorithm(this.existingAlg.getSelection());
				this.setPageComplete(true);
			} else {
				this.setPageComplete(false);
			}
			
			
		} else if(this.prevSelected == 1) {
			this.newAlg.setEditable(true);
			this.saveNewAlgorithm.setEnabled(true);
			this.existingAlg.setEnabled(false);
			
			if(!this.newAlg.getText().equals("")) {
				this.setPageComplete(true);
			} else {
				this.setPageComplete(false);
			}
		}
		this.initConf.setNewConfigurable(this.generalConfiguration.getAlgorithmConfigurable());
		
		this.generalConf.setNewConfigurable(this.generalConfiguration.getGenericConfigurable());
	}

	public void addAlgorithm(NestedBuilderAlgorithm algorithm) {
		this.generalConfiguration.addNewNestedAlgorithm(algorithm);
	}

	public String getAlgorithmName() {
		return this.newAlg.getText();
	}

	public boolean getSaveAlgorithm() {
		return this.saveNewAlgorithm.isChecked();
	}
}
