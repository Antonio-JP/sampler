package rioko.drawmodels.wizards;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.graphabstraction.runtime.registers.RegisterBuilderSteps;
import rioko.graphabstraction.runtime.registers.RegisterRuntimeException;
import rioko.utilities.Log;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.wizards.pages.spAlgPages.AlgorithmConfigurationWizardPage;
import rioko.drawmodels.wizards.pages.spAlgPages.CreateBasicStepsAlgorithmWizardPage;
import rioko.drawmodels.wizards.pages.spAlgPages.CreateOthersStepsAlgorithmWizardPage;
import rioko.drawmodels.wizards.pages.spAlgPages.SelectExistingAlgorithmWizardPage;

public class SelectSpecialAlgorithmWizard extends AbstractWizard {
	
	//Static constnats
	
	//Private attributes
	private NestedBuilderAlgorithm algorithm = new NestedBuilderAlgorithm();
	
	private ZestEditor editor;
	private ZestProperties properties;
	private DiagramGraph model;

	private boolean saveAlgorithm = false;
	
	//Builders
	public SelectSpecialAlgorithmWizard(ZestEditor editor) {
		this.editor = editor;
		this.model = this.editor.getModel().getModelDiagram();
		this.properties = this.editor.getProperties().copy();
	}
	
	public SelectSpecialAlgorithmWizard(ZestEditor editor, DiagramGraph graph) {
		this.editor = editor;
		this.model = graph;
		this.properties = this.editor.getProperties().copy();
	}
	
	//Getters & Setters	
	public NestedBuilderAlgorithm getAlgorithm() {
		return this.algorithm;
	}
	
	public void setSaveAlgorith(boolean saveAlgorithm) {
		this.saveAlgorithm   = saveAlgorithm;
	}
	
	private boolean getSaveAlgorithm() {
		return this.saveAlgorithm;
	}

	//IWizard methods
	@Override
	public void addPages() {
		//Añadimos las páginas del wizard
		this.addPage(new SelectExistingAlgorithmWizardPage(this.properties, this.model));
		
		this.addPage(new CreateBasicStepsAlgorithmWizardPage(this.model));
		this.addPage(new CreateOthersStepsAlgorithmWizardPage(this.model));
	}

	@Override
	public boolean performFinish() {
		this.updateWizardData(this.getCurrentPage());
		
		SelectExistingAlgorithmWizardPage selectPage = this.getSelectExistingPage();
		
		if(!(this.getCurrentPage() instanceof SelectExistingAlgorithmWizardPage)) {
			this.properties.addNewNestedAlgorithm(this.algorithm);
			this.algorithm.setAlgorithmName(selectPage.getAlgorithmName());
			
			if(this.getSaveAlgorithm()) {
				try {
					RegisterBuilderAlgorithm.register(this.algorithm);
				} catch (RegisterRuntimeException e) {
					Log.exception(e);
					Log.print("Error trying to register a new algorithm");
				}
			}
		}
		
		this.editor.changeZestProperties(this.properties);
				
		return super.performFinish();
	}
		
	@Override
	public void dispose() {		
		super.dispose();
	}
	
	//AbstractWizard methods
	protected void updateWizardData(IWizardPage page) {
		if(page instanceof SelectExistingAlgorithmWizardPage) {
			this.saveAlgorithm = ((SelectExistingAlgorithmWizardPage)page).getSaveAlgorithm();
		} else if(page instanceof AlgorithmConfigurationWizardPage) {
			((AlgorithmConfigurationWizardPage) page).updateAlgorithm(this.algorithm);
		}
	}

	//Private methods
	private SelectExistingAlgorithmWizardPage getSelectExistingPage() {
		IWizardPage[] pages = this.getPagesOfType(SelectExistingAlgorithmWizardPage.class);
		
		if(pages.length == 1) {
			return (SelectExistingAlgorithmWizardPage)pages[0];
		}
		
		return null;
	}
	
	//Static methods
	public static NestedGraphBuilder[] chooseStepAvaible(Class<? extends NestedGraphBuilder> builderClass) {
		String[] possibleChoices = RegisterBuilderSteps.getRegisteredNames(builderClass).toArray(new String[0]);
		
		ListSelectionDialog dialog = new ListSelectionDialog(null, possibleChoices, ArrayContentProvider.getInstance(),
				            new LabelProvider(), "Select one of the avaible Steps");

		dialog.setTitle("Select Step");
		dialog.open();
		Object [] result = dialog.getResult();
		
		ArrayList<NestedGraphBuilder> list = new ArrayList<>();
		if(result != null) {
			for(Object obj : result) {
				try {
					list.add((NestedGraphBuilder)Class.forName((String)obj).newInstance());
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
		
		return list.toArray(new NestedGraphBuilder[0]);
	}
}
