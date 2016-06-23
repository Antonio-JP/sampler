package rioko.sampler.emf_splitter.folder.diagram;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;

import rioko.drawmodels.diagram.IdParser;
import rioko.graphabstraction.diagram.DiagramNode;

public class FolderIdParser implements IdParser {

	@Override
	public Object getFromString(String str) throws IllegalArgumentException {
		return ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(str));
	}

	@Override
	public Object getKey(DiagramNode node) {
		if(node instanceof FolderDiagramNode) {
			FolderDiagramNode folderNode = (FolderDiagramNode) node;
			
			return folderNode.getResource().getLocation().toString();
		}
		
		return null;
	}

}
