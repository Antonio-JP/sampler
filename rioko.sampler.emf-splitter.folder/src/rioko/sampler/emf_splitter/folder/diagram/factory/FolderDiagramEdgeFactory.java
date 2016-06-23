package rioko.sampler.emf_splitter.folder.diagram.factory;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.EdgeFactory;
import rioko.sampler.emf_splitter.folder.diagram.FolderDiagramEdge;

public class FolderDiagramEdgeFactory implements EdgeFactory<DiagramNode, FolderDiagramEdge> {
	@Override
	public FolderDiagramEdge createEdge(DiagramNode arg0, DiagramNode arg1) {
		return new FolderDiagramEdge(arg0, arg1);
	}
}
