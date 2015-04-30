package rioko.drawmodels.wizards.pages.spAlgPages;

import java.util.ArrayList;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.graphabstraction.display.LocalNestedBuilder;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.addremovetables.GraphBuilderAddRemoveListener;
import rioko.drawmodels.swt.composites.addremovetables.TableConfigurationListener;

public class CreateBasicStepsAlgorithmWizardPage extends AlgorithmConfigurationWizardPage {
	
	private AddRemoveTable filterTable, globalTable, localTable;

	private ArrayList<FilterNestedBuilder> filters = new ArrayList<>();
	private ArrayList<GlobalNestedBuilder> global = new ArrayList<>();
	private ArrayList<LocalNestedBuilder> local = new ArrayList<>();
	
	//Builders
	public CreateBasicStepsAlgorithmWizardPage(DiagramGraph model) {
		super("Create basic Steps of Algorithm", model);

		this.setTitle("Create basic Steps of Algorithm");
		this.setDescription("Create the basic steps of your new Algorithm");
	}

	public CreateBasicStepsAlgorithmWizardPage(String pageName, String title, ImageDescriptor titleImage) {
		super(pageName, title, titleImage);
	}
	
	//Other methods
	public void updateAlgorithm(NestedBuilderAlgorithm algorithm)
	{
		algorithm.removeAllFilters();
		for(FilterNestedBuilder filter : this.filters) {
			algorithm.addFilter(filter);
		}
		
		algorithm.removeAllGlobalCriteria();
		for(GlobalNestedBuilder global : this.global) {
			algorithm.addGlobalCriteria(global);
		}
		
		algorithm.removeAllLocalCriteria();
		for(LocalNestedBuilder local : this.local) {
			algorithm.addLocalCriteria(local);
		}
	}
	
	//WizardPage methods
	@Override
	public void particularSectionsView() {
		//PRIMERA CAJA: PARA LOS FILTROS
	    this.filterTable = new AddRemoveTable(firstColumn, SWT.NONE, "Filters");
	    this.filterTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		
	    this.filterTable.setMouseListener(new GraphBuilderAddRemoveListener<FilterNestedBuilder>(filterTable, true, filters, FilterNestedBuilder.class), 
	    		new GraphBuilderAddRemoveListener<FilterNestedBuilder>(filterTable, false, filters, FilterNestedBuilder.class));
		
		this.filterTable.setTableListener(new TableConfigurationListener<FilterNestedBuilder>(filterTable, filters, this.configurationTable));
		
	    //SEGUNDA CAJA: PARA LOS GLOBALES
	    this.globalTable = new AddRemoveTable(firstColumn, SWT.NONE, "Global Criteria");
	    this.globalTable.setLayoutData(new GridData(GridData.FILL_BOTH));
	    
	    this.globalTable.setMouseListener(new GraphBuilderAddRemoveListener<GlobalNestedBuilder>(globalTable, true, global, GlobalNestedBuilder.class), 
	    		new GraphBuilderAddRemoveListener<GlobalNestedBuilder>(globalTable, false, global, GlobalNestedBuilder.class));
	    
	    this.globalTable.setTableListener(new TableConfigurationListener<GlobalNestedBuilder>(globalTable, global, this.configurationTable));
	    
	    //TERCERA CAJA: PARA LOS LOCALES
 		this.localTable = new AddRemoveTable(firstColumn, SWT.NONE, "Local Criteria");
	    this.localTable.setLayoutData(new GridData(GridData.FILL_BOTH));
	
	    this.localTable.setMouseListener(new GraphBuilderAddRemoveListener<LocalNestedBuilder>(localTable, true, local, LocalNestedBuilder.class), 
	    		new GraphBuilderAddRemoveListener<LocalNestedBuilder>(localTable, false, local, LocalNestedBuilder.class));
	    
	    this.localTable.setTableListener(new TableConfigurationListener<LocalNestedBuilder>(localTable, local, this.configurationTable));
	}
}
