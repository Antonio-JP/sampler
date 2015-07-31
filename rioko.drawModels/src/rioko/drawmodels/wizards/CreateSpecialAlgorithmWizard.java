package rioko.drawmodels.wizards;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.ui.dialogs.ListSelectionDialog;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.graphabstraction.runtime.registers.RegisterBuilderSteps;
import rioko.graphabstraction.runtime.registers.RegisterRuntimeException;
import rioko.utilities.Log;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.wizards.pages.spAlgPages.AlgorithmConfigurationWizardPage;
import rioko.drawmodels.wizards.pages.spAlgPages.CreateBasicStepsAlgorithmWizardPage;
import rioko.drawmodels.wizards.pages.spAlgPages.CreateOthersStepsAlgorithmWizardPage;
import rioko.eclipse.registry.RegistryManagement;

public class CreateSpecialAlgorithmWizard extends AbstractWizard {

	//Static constnats
	
	//Private attributes
	private NestedBuilderAlgorithm algorithm;
		
	private ZestEditor editor;

	private boolean saveAlgorithm = true;
		
	//Builders
	public CreateSpecialAlgorithmWizard(ZestEditor editor, String algName) {
		this.editor = editor;
		this.algorithm = new NestedBuilderAlgorithm(algName);
	}
	
	//Getters & Setters	
	public NestedBuilderAlgorithm getAlgorithm() {
		return this.algorithm;
	}
	
	public void setSaveAlgorith(boolean saveAlgorithm) {
		this.saveAlgorithm  = saveAlgorithm;
	}
	
	private boolean getSaveAlgorithm() {
		return this.saveAlgorithm;
	}
		
	//IWizard methods
	@Override
	public void addPages() {
		//Añadimos las páginas del wizard
		this.addPage(new CreateBasicStepsAlgorithmWizardPage(this.editor.getModel().getModelDiagram()));
		this.addPage(new CreateOthersStepsAlgorithmWizardPage(this.editor.getModel().getModelDiagram()));
	}

	@Override
	public boolean performFinish() {
		this.updateWizardData(this.getCurrentPage());
		
		this.editor.getProperties().addNewNestedAlgorithm(this.algorithm);
		
		if(this.getSaveAlgorithm()) {
			try {
				RegisterBuilderAlgorithm.register(this.algorithm);
			} catch (RegisterRuntimeException e) {
				Log.exception(e);
				Log.print("Error trying to register a new algorithm");
			}
		}
				
		return super.performFinish();
	}
	
	//AbstractWizard methods
	protected void updateWizardData(IWizardPage page) {
		if(page instanceof AlgorithmConfigurationWizardPage) {
			((AlgorithmConfigurationWizardPage) page).updateAlgorithm(this.algorithm);
		}
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
				NestedGraphBuilder newBuilder = (NestedGraphBuilder) RegistryManagement.getInstance("rioko.drawmodels.steps", (String)obj);
				if(newBuilder == null) {
					Log.exception(new Exception("Error while creating a " + obj.toString() + " after selecting as a possible step"));
				} else {
					list.add(newBuilder);
				}
			}
		}
		
		return list.toArray(new NestedGraphBuilder[0]);
	}
}
