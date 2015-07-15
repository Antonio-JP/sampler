package rioko.emfdrawer.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.emfdrawer.xmiDiagram.XMIDiagramNode;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.TextConfiguration;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.utilities.Pair;

public class ByAttributeValue extends FilterOfVertex {

	private Pair<String, String> attribute= new Pair<String,String>("","");
	
	public void setAttribute(Pair<String,String> attribute) {
		if(attribute != null) {
			this.attribute.setFirst(attribute.getFirst());
			this.attribute.setLast(attribute.getLast());
		}
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		if(attribute != null) {
			if(node instanceof XMIDiagramNode) {
				XMIDiagramNode xmiNode = (XMIDiagramNode)node;
				
				for(AbstractAttribute attr : xmiNode.getAttributes()) {
					if(attr.getName().equals(attribute.getFirst())) {
						return attribute.getLast().equals(attr.getValue());
					}
				}
			}
			
			return false;
		}
		
		return true;
	}

	@Override
	public Collection<Pair<String,Configuration>> getConfiguration() {
		Collection<Pair<String,Configuration>> collection = new ArrayList<>();
		
		if(this.attribute == null) {
			collection.add(new Pair<String, Configuration>("Attribute Name", new TextConfiguration("")));
			collection.add(new Pair<String, Configuration>("Attribute Value", new TextConfiguration("")));
		} else {
			collection.add(new Pair<String, Configuration>("Attribute Name", new TextConfiguration(this.attribute.getFirst())));
			collection.add(new Pair<String, Configuration>("Attribute Value", new TextConfiguration(this.attribute.getLast())));
		}
		
		return collection;
	}

	@Override
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		if(newConf.size() != 2) {
			throw new BadConfigurationException("This Configurable requires 2 configurations");
		}
		
		Iterator<Configuration> iterator = newConf.iterator();
		
		Configuration first = iterator.next();
		Configuration second = iterator.next();
		
		if(!(first instanceof TextConfiguration)) {
			throw new BadArgumentException(TextConfiguration.class, first.getClass());
		} else if(!(second instanceof TextConfiguration)) {
			throw new BadArgumentException(TextConfiguration.class, second.getClass());
		}

		this.attribute.setFirst(((TextConfiguration)first).getConfiguration());
		this.attribute.setLast(((TextConfiguration)second).getConfiguration());
	}

}
