package rioko.sampler.emf_splitter.directorydrawer.search;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.sampler.directoryDrawer.diagram.FolderDiagramNode;
import rioko.utilities.Pair;

public class EMFSplitterRepeatedFilter extends FilterOfVertex {

	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		return new ArrayList<>();
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf)
			throws BadConfigurationException, BadArgumentException {
		return;
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		if(node instanceof FolderDiagramNode) {
			FolderDiagramNode fNode = (FolderDiagramNode)node;
			IResource resource = fNode.getResource();
			
			if(resource instanceof IFile) {
				IFile file = (IFile)resource;
				String fileName = file.getName();
				String fileNameWOExtension = fileName.substring(0, fileName.lastIndexOf("." + file.getFileExtension()));
				
				IResource parent = file.getParent();
				if(parent != null && graph.containsVertex(graph.getVertexFactory().createVertex(parent))) {
					return fileNameWOExtension.equals(parent.getName());
				}
			}
		}
		return false;
	}

}
