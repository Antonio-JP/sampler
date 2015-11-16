package rioko.drawmodels.handlers.zesteditor;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.handlers.EditorDependentHandler;

public class ShowAllModel extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent arg0) throws ExecutionException {
		// Generamos un di�logo de confirmaci�n ya que puede suponer una gran carga de trabajo
		if(MessageDialog.openConfirm(null, "Acci�n costosa", "Haciendo esto se mostrar� por pantalla el modelo "
				+ "completo sin ninguna compresi�n. Esto podr�a acarrear un coste computacional imporante.\n"
				+ "�Desea continuar?")) {
			//Llamamos al controlador para eliminar la raiz de la vista
			((ZestEditor)editor).showAll();
		}
		
		return null;
	}

}
