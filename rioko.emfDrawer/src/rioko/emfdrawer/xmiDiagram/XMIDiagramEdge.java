package rioko.emfdrawer.xmiDiagram;

import org.eclipse.emf.ecore.EReference;

import rioko.emfdrawer.xmiDiagram.factories.XMIDiagramEdgeFactory;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramNode;

public class XMIDiagramEdge extends DiagramEdge<DiagramNode>{
	
	private EReference ref;

	//Builders
	public XMIDiagramEdge() {}
	
	public XMIDiagramEdge(DiagramNode source, DiagramNode target) {
		super(source, target);
	}
	
	//Getters & Setters
	public void setEReference(EReference ref) {
		this.ref = ref;
	}
	
	public EReference getEReference() {
		return ref;
	}
	
	//Public methods
//	public IFigure getFigure() {
//		return this.getValueFigure();
//	}
	
	@Override
	public XMIDiagramEdgeFactory getEdgeFactory()
	{
		return new XMIDiagramEdgeFactory();
	}
	

	@Override
	public String getText() {
		if(ref == null) {
			return super.getText();
		} else {
			return this.ref.getName();
		}
	}
	
	//Abstract methods
//	public IFigure getValueFigure() {
//		return null;
//	}
}
