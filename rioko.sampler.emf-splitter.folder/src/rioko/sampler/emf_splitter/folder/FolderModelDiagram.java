package rioko.sampler.emf_splitter.folder;

import org.eclipse.core.resources.IResource;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;

public class FolderModelDiagram extends ModelDiagram<IResource> {
	//Builders
		public FolderModelDiagram()
		{
			super();
		}
		
		public FolderModelDiagram(Class<? extends DiagramEdge<DiagramNode>> edgeClass, Class<? extends DiagramNode> vertexClass, Class<? extends ComposeDiagramNode> composeClass)
		{
			super(edgeClass, vertexClass, composeClass);
		}
		
		public FolderModelDiagram(DiagramGraph graph) 
		{
			super(graph);
		}

		@Override
		public void buildMetaData() { /* No meta-data needed */ }

		@Override
		public Object getMetaData() { return null; }
}
