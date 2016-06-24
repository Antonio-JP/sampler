package rioko.sampler.emf_splitter.directorydrawer.listener;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.events.MouseEvent;

import rioko.drawmodels.editors.listeners.mouse.AbstractZestMouseListener;
import rioko.drawmodels.handlers.extensions.OpenFile;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramNode;
import rioko.sampler.directoryDrawer.handler.OpenDirectoryVisualization;
import rioko.sampler.directoryDrawer.reader.FolderReader;

public class OpenModelFragmentMouseListener extends AbstractZestMouseListener {
	
	public static final String DIAGRAM_EXTENSION_ID = "rioko.drawmodels.diagrams";

	public OpenModelFragmentMouseListener() {
		super();
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		if(e.button == 1) { //Double click with left button
			//Get the selection (if happens)
			IFigure[] figures = this.getViewer().getFiguresAtPosition(e.x, e.y);
			
			for(IFigure figure : figures) {
				if(figure instanceof ModelNodeFigure) {
					DiagramNode node = ((ModelNodeFigure)figure).getReferredNode();
					
					if(node instanceof FolderDiagramNode) {
						FolderDiagramNode tNode = (FolderDiagramNode)node;
						IResource res = tNode.getResource();
						IFile file = null;
						if(res instanceof IFile) {
							file = (IFile)res;
							
						} else if(res instanceof IContainer) {
							IContainer container = (IContainer)res;
							file = container.getFile(new Path("./" + container.getName() + ".xmi"));
						}
						
						try {
							(new OpenFile()).openEditorWithFile(file, null);
						} catch (Exception e1) {
							MessageDialog.openError(null, "Error opening file",
									"Error opening a file with SAMPLER while exploring a directory\n\n" +
									"File: " + res.getLocation().toOSString());
						}
					}
				}
			}
		}
	}

	@Override
	public void mouseDown(MouseEvent e) { /*Do nothing*/ }

	@Override
	public void mouseUp(MouseEvent e) { /*Do nothing */ }

	@Override
	protected boolean checkValidViewer() {
		return (this.getController().getReader() instanceof FolderReader);
	}
}
