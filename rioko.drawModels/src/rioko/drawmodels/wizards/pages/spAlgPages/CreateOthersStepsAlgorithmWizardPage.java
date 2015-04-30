package rioko.drawmodels.wizards.pages.spAlgPages;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.addremovetables.GraphBuilderAddRemoveListener;
import rioko.drawmodels.swt.composites.addremovetables.TableConfigurationListener;

public class CreateOthersStepsAlgorithmWizardPage extends AlgorithmConfigurationWizardPage {

	private AddRemoveTable preTable, midFilterTable, midGlobalTable, postTable;
	
	private ArrayList<NestedGraphBuilder> preprocessor = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> midFilter = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> midGlobal = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> postprocessor = new ArrayList<>();
	
	public CreateOthersStepsAlgorithmWizardPage(DiagramGraph model) {
		super("Create custom Steps of Algorithm", model);

		this.setTitle("Create other Steps of Algorithm");
		this.setDescription("Create the specific steps of your new Algorithm");
	}

	public CreateOthersStepsAlgorithmWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	//Other methods
	public void updateAlgorithm(NestedBuilderAlgorithm algorithm)
	{
		algorithm.removeAllOtherSteps();
		for(NestedGraphBuilder step : this.preprocessor) {
			algorithm.addOtherStep(step, NestedBuilderAlgorithm.PRE_POSITION);
		}
		
		for(NestedGraphBuilder step : this.midFilter) {
			algorithm.addOtherStep(step, NestedBuilderAlgorithm.MID_FILTER_POSITION);
		}
		
		for(NestedGraphBuilder step : this.midGlobal) {
			algorithm.addOtherStep(step, NestedBuilderAlgorithm.MID_GLOBAL_POSITION);
		}
		
		for(NestedGraphBuilder step : this.postprocessor) {
			algorithm.addOtherStep(step, NestedBuilderAlgorithm.POST_POSITION);
		}
	}
	
	//WizarPage methods
	@Override
	public void particularSectionsView() {			    
		//PRIMERA CAJA: PARA LOS PRIMEROS
		this.preTable = new AddRemoveTable(firstColumn, SWT.NONE, "Preprocessor Steps");
		this.preTable.setLayoutData(new GridData(GridData.FILL_BOTH));
				
		this.preTable.setMouseListener(new GraphBuilderAddRemoveListener<NestedGraphBuilder>(preTable, true, preprocessor, NestedGraphBuilder.class), 
	    		new GraphBuilderAddRemoveListener<NestedGraphBuilder>(preTable, false, preprocessor, NestedGraphBuilder.class));
		
		this.preTable.setTableListener(new TableConfigurationListener<NestedGraphBuilder>(preTable, preprocessor, this.configurationTable));
				
		//SEGUNDA CAJA: PARA LOS DE DESPUÉS DE LOS FILTROS
		this.midFilterTable = new AddRemoveTable(firstColumn, SWT.NONE, "After Filter Steps");
		this.midFilterTable.setLayoutData(new GridData(GridData.FILL_BOTH));
				
		this.midFilterTable.setMouseListener(new GraphBuilderAddRemoveListener<NestedGraphBuilder>(midFilterTable, true, midFilter, NestedGraphBuilder.class), 
	    		new GraphBuilderAddRemoveListener<NestedGraphBuilder>(midFilterTable, false, midFilter, NestedGraphBuilder.class));
		
		this.midFilterTable.setTableListener(new TableConfigurationListener<NestedGraphBuilder>(midFilterTable, midFilter, this.configurationTable));	    
			    
		//TERCERA CAJA: PARA LOS DE DESPUÉS DE GLOBALES
		this.midGlobalTable = new AddRemoveTable(firstColumn, SWT.NONE, "After Global Criteria Steps");
		this.midGlobalTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		this.midGlobalTable.setMouseListener(new GraphBuilderAddRemoveListener<NestedGraphBuilder>(midGlobalTable, true, midGlobal, NestedGraphBuilder.class), 
	    		new GraphBuilderAddRemoveListener<NestedGraphBuilder>(midGlobalTable, false, midGlobal, NestedGraphBuilder.class));
		
		this.midGlobalTable.setTableListener(new TableConfigurationListener<NestedGraphBuilder>(midGlobalTable, midGlobal, this.configurationTable));	
			    
		//CUARTA CAJA: PARA LOS FINALES
		this.postTable = new AddRemoveTable(firstColumn, SWT.NONE, "Postprocessor Steps");
		this.postTable.setLayoutData(new GridData(GridData.FILL_BOTH));
			
		this.postTable.setMouseListener(new GraphBuilderAddRemoveListener<NestedGraphBuilder>(postTable, true, postprocessor, NestedGraphBuilder.class), 
	    		new GraphBuilderAddRemoveListener<NestedGraphBuilder>(postTable, false, postprocessor, NestedGraphBuilder.class));
		
		this.postTable.setTableListener(new TableConfigurationListener<NestedGraphBuilder>(postTable, postprocessor, this.configurationTable));
		 		
	}
	
	@Override
	public IWizardPage getNextPage()
	{
		return null;
	}

}
