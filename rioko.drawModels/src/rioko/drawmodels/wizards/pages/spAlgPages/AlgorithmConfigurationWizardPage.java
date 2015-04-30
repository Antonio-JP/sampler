package rioko.drawmodels.wizards.pages.spAlgPages;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.drawmodels.wizards.pages.CustomWizardPage;

public abstract class AlgorithmConfigurationWizardPage extends CustomWizardPage {
	
	/* Attributes */
	protected ConfigurationTable configurationTable;
	
	protected DiagramGraph model;
	
	protected Composite firstColumn;

	/* Builders */
	public AlgorithmConfigurationWizardPage(String pageName, DiagramGraph model) {
		super(pageName);
		
		this.model = model;
	}
	
	public AlgorithmConfigurationWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	/* Getters & Setters */
	public DiagramGraph getModel() {
		return this.model;
	}

	/* Methods */
	public abstract void updateAlgorithm(NestedBuilderAlgorithm algorithm);
	
	protected abstract void particularSectionsView();
	
	/* CustomWizardPage methods implementation */
	@Override
	public void createControl(Composite parent)
	{
		//Esta página del Wizard tiene 2 columnas
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		container.setLayout(layout);
		  
		//En la primera tiene tres casillas
		this.firstColumn = new Composite(container, SWT.NONE);
		this.firstColumn.setLayoutData(new GridData(GridData.FILL_BOTH));	//Lo añadimos en la primera columna de container
		
		GridLayout frstColLayout = new GridLayout();
		frstColLayout.verticalSpacing = 4;
		frstColLayout.numColumns = 1;
		this.firstColumn.setLayout(frstColLayout);
			    
		//Dentro de esta columna metemos tres cajas, cada una de ellas será una composición en dos columnas de:
		//	- una tabla con una sola columna
		//	- dos botones en la segunda columna
		   
		Label stepsLabel = new Label(this.firstColumn, SWT.NONE);
		stepsLabel.setText("Steps Programmed");
		stepsLabel.setFont(new Font(null, "Arial", 0, 15));
		stepsLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		 		
		//Ahora pasamos a construir la segunda Columna de nuestro Wizard: una tabla de atributo/valor para configurar el algoritmo seleccionado
		Composite secondColumn = new Composite(container, SWT.NONE);
		secondColumn.setLayoutData(new GridData(GridData.FILL_BOTH));	//Lo añadimos en la primera columna de container
			    
		GridLayout scndColLayout = new GridLayout();
		scndColLayout.numColumns = 1;
		secondColumn.setLayout(scndColLayout);
			    
		Label confLabel = new Label(secondColumn, SWT.NONE);
		confLabel.setText("Step Configuration");
		confLabel.setFont(new Font(null, "Arial", 0, 15));
		confLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
			    
		this.configurationTable = new ConfigurationTable(secondColumn, SWT.NONE, new ModelDiagram(this.getModel()));
		this.configurationTable.setLayoutData(new GridData(GridData.FILL_BOTH));	//Lo añadimos en la segunda columna
		
		this.particularSectionsView();
	    
	    // required to avoid an error in the system
	    setControl(container);
	    setPageComplete(true);
	}

	@Override
	public boolean isWizardFinishable() {
		return true;
	}
}
