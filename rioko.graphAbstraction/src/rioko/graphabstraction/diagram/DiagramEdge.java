package rioko.graphabstraction.diagram;

import rioko.grapht.EdgeFactory;

import rioko.grapht.AbstractEdge;

public class DiagramEdge<V extends DiagramNode> extends AbstractEdge<V> {
	
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
