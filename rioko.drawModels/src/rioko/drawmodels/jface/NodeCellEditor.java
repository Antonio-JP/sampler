package rioko.drawmodels.jface;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import rioko.drawmodels.dialogs.ValuableDialog;
import rioko.graphabstraction.configurations.DialogConfiguration;

public class NodeCellEditor extends DialogCellEditor {
	
	DialogConfiguration configuration;

	public NodeCellEditor(Composite parent, int style, DialogConfiguration configuration) {
		super(parent, style);

		this.configuration = configuration;
	}

	@Override
	protected Object openDialogBox(Control parent) {
		Dialog dialog = this.configuration.getDialog();
		
		if(dialog instanceof ValuableDialog) {
			if(dialog.open() == Window.OK) {
				return ((ValuableDialog) dialog).getValue();
			}
		}
		
		return null;
	}

}
