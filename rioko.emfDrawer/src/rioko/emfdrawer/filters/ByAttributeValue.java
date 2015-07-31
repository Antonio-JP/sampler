package rioko.emfdrawer.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import rioko.emfdrawer.configurations.AttributeNameConfiguration;
import rioko.emfdrawer.configurations.AttributeValueConfiguration;
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
import rioko.utilities.Log;
import rioko.utilities.Pair;

public class ByAttributeValue extends FilterOfVertex {
	private AttributeNameConfiguration nameConfiguration = new AttributeNameConfiguration("");
	private AttributeValueConfiguration valConfiguration = new AttributeValueConfiguration("");
	
	public void setAttribute(Pair<String,String> attribute) {
		if(attribute != null) {
			try {
				this.nameConfiguration.setValueConfiguration(attribute.getFirst());
				this.valConfiguration.setValueConfiguration(attribute.getLast());
			} catch (BadArgumentException | BadConfigurationException e) {
				// Impossible Exceptions
				Log.exception(e);
			}
		}
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		if((!this.nameConfiguration.getConfiguration().equals("")) && (!this.valConfiguration.getConfiguration().equals(""))) {
			if(node instanceof XMIDiagramNode) {
				XMIDiagramNode xmiNode = (XMIDiagramNode)node;
				
				for(AbstractAttribute attr : xmiNode.getDrawableData()) {
					if(attr.getName().equals(this.nameConfiguration.getConfiguration())) {
						return this.valConfiguration.getConfiguration().equals(attr.getValue());
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
		
		collection.add(new Pair<>(this.nameConfiguration.getNameOfConfiguration(), this.nameConfiguration));
		collection.add(new Pair<>(this.valConfiguration.getNameOfConfiguration(), this.valConfiguration));
		
		return collection;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
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

		this.nameConfiguration.setValueConfiguration(((TextConfiguration)first).getConfiguration());
		this.valConfiguration.setValueConfiguration(((TextConfiguration)second).getConfiguration());
	}
}
