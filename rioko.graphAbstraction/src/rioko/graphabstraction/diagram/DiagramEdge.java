package rioko.graphabstraction.diagram;

import org.jgrapht.EdgeFactory;

import rioko.grapht.VisibleEdge;

public class DiagramEdge<V extends DiagramNode> extends VisibleEdge<V> {

	private static final long serialVersionUID = 3465920667910496532L;
	
	private typeOfConnection type = null;
	
	public DiagramEdge() {}
	
	public DiagramEdge(V source, V target) {
		super(source, target);
	}
	
	//Getters & Setters
	@Override
	public typeOfConnection getType() {
		return type;
	}

	@Override
	public void setType(Object type) {
		if(type instanceof typeOfConnection) {
			this.type = (typeOfConnection)type;
		}
	}
	
	//Edge Factory
	@Override 
	public EdgeFactory<V, ? extends DiagramEdge<V>> getEdgeFactory()
	{
		return new DiagramEdgeFactory<V>();
	}
	
	//Other stuff
	public enum typeOfConnection {SIMPLE, CONTAINMENT, REFERENCE}

	public String getText() {
		return "";
	}
}
