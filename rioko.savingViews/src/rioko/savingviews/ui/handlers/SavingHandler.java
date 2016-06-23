package rioko.savingviews.ui.handlers;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.handlers.EditorDependentHandler;
import rioko.savingviews.ZestDisplay;

public class SavingHandler extends EditorDependentHandler {

	@Override
	public Object run(ExecutionEvent event) throws ExecutionException {		
		ModelDiagram<?> model = this.getModel();
		
		ZestDisplay display = new ZestDisplay(model, this.getProperties(), model.getIdParser());
		
		IFile dest = null;
		//try {
			IResource file = this.getFileAssociated();	
			IContainer folder = file.getParent();
			dest = folder.getFile(new Path("./" + file.getName() + ".smp")); //folder.getLocation().removeLastSegments(1).append(file.getName()+".smp")
//		} catch (NullPointerException e) {
//			//Error getting the destiny file
//			FileDialog dialog = new FileDialog(null, SWT.OPEN);
//			dialog.setFilterPath(ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
//			String res = dialog.open();
//			dest = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(res + "/display.smp"));
//		}
		
		boolean proceed = true;
		
		if(dest.exists()) {
			proceed = MessageDialog.openQuestion(null, "Display already exists", "There is another display for this model. \nDo you want to overwrite it?");
		}
		
		if(proceed) {
			if(!display.saveDisplay(dest)) {
				MessageDialog.openError(null, "Error saving display", "The display of this model has had an error while saving.\n\nThe file " + dest.getName() + ".smp has not be created.");
			} else {
				MessageDialog.openInformation(null, "Display saved!", "The display of this model has been succesfully saved on file " + dest.getName() + ".smp.");
			}
		}
		
		return null;
	}

}
