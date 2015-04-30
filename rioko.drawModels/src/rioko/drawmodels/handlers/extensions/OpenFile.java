package rioko.drawmodels.handlers.extensions;

import java.io.IOException;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.ide.IDE;

import rioko.utilities.Log;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.drawmodels.editors.zesteditor.SpecialInputZestEditor;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.filemanage.XMIReader;
import rioko.drawmodels.handlers.AbstractGenericHandler;
import rioko.drawmodels.wizards.SelectSpecialAlgorithmWizard;

public class OpenFile extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent ee) throws ExecutionException {
		
		ISelection sel = HandlerUtil.getActiveMenuSelection(ee);
		if(sel instanceof IStructuredSelection) {
			IStructuredSelection strSel = (IStructuredSelection)sel;
			
			Object obj = strSel.getFirstElement();
			
			if(obj instanceof IFile) {
				IFile file = (IFile)obj;
				
				XMIReader xmiReader;
				try {
					Log.xOpen("wizard");
					Log.xPrint("Leyendo el modelo...");
					xmiReader = XMIReader.getReaderFromFile(file);
				
					DiagramGraph graph = xmiReader.getDiagram();
					ZestEditor editor = new ZestEditor();
					
					Log.xPrint("Creando el wizard...");
					SelectSpecialAlgorithmWizard wizard = new SelectSpecialAlgorithmWizard(editor, graph);
					
					Log.xPrint("Iniciando el wizard...");
					wizard.init(HandlerUtil.getActiveWorkbenchWindow(ee).getWorkbench(), null);
					    
					// Instantiates the wizard container with the wizard and opens it
					WizardDialog dialog = new WizardDialog(null, wizard);
					
					dialog.create();
					dialog.open();
					
					Log.xPrint("Wizard abierto. Esperando respuesta...");
					int ret = dialog.getReturnCode();
					
					Log.xPrint("Retorno del Wizard -> " + ret);
					Log.xClose("wizard");
					
					ZestProperties properties = editor.getProperties().copy();
					editor.dispose();
					
					IDE.openEditor(HandlerUtil.getActiveWorkbenchWindow(ee).getActivePage(), new SpecialInputZestEditor(graph, properties, file.getName()), "rioko.drawmodels.editors.zestEditor");
				} catch (IOException e) {
					Log.exception(e);
				} catch (PartInitException e) {
					Log.exception(e);
				}
			}
		}
		
		return null;
	}

}
