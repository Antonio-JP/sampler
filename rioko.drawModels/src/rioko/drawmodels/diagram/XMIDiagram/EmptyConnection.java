package rioko.drawmodels.diagram.XMIDiagram;

import org.eclipse.draw2d.IFigure;
import org.jgrapht.EdgeFactory;

import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.edgeFactories.EmptyConnectionFactory;

public class EmptyConnection extends XMIDiagramEdge {

	private static final long serialVersionUID = -1555793768896487309L;

	//Builders
	public EmptyConnection() {}
	
	public EmptyConnection(DiagramNode source, DiagramNode target) {
		super(source, target);
	}
	
	@Override
	public EdgeFactory<DiagramNode, EmptyConnection> getEdgeFactory()
	{
		return new EmptyConnectionFactory();
	}
	
	@Override
	public IFigure getValueFigure() {
		return null;
	}

}
