package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.AbstractGenericHandler;

public class ShowAllModel extends AbstractGenericHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		// Generamos un di�logo de confirmaci�n ya que puede suponer una gran carga de trabajo
		if(MessageDialog.openConfirm(null, "Acci�n costosa", "Haciendo esto se mostrar� por pantalla el modelo completo sin ninguna compresi�n. Esto podr�a acarrear un coste computacional imporante. �Desea continuar?")) {
			//Llamamos al controlador para eliminar la raiz de la vista
			try{
				IWorkbenchPage page = this.getEditorPage();
			
				IEditorPart editor = page.getActiveEditor();
				
				if(editor instanceof ZestEditor) {
					((ZestEditor)editor).showAll();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
