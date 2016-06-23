package rioko.sampler.emf_splitter.folder.diagram;

import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.sampler.emf_splitter.folder.diagram.factory.FolderDiagramEdgeFactory;

public class FolderDiagramEdge extends DiagramEdge<DiagramNode> {
	//Builders
	public FolderDiagramEdge() {}
	
	public FolderDiagramEdge(DiagramNode source, DiagramNode target) {
		super(source, target);
	}
	
	@Override
	public FolderDiagramEdgeFactory getEdgeFactory()
	{
		return new FolderDiagramEdgeFactory();
	}
}
