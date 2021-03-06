package rioko.emfdrawer.filters;

import java.util.ArrayList;
import java.util.Collection;

import rioko.emfdrawer.configurations.AttributeNameConfiguration;
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

public class ByHaveAttribute extends FilterOfVertex {
	
	private AttributeNameConfiguration nameConf = new AttributeNameConfiguration("");
	
	public void setAttribute(String attribute) {
		if(attribute != null) {
			try {
				this.nameConf.setValueConfiguration(attribute);
			} catch (BadArgumentException | BadConfigurationException e) {
				// Impossible Exception
				Log.exception(e);
			}
		}
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		if(node instanceof XMIDiagramNode) {
			XMIDiagramNode xmiNode = (XMIDiagramNode)node;
			
			for(AbstractAttribute attr : xmiNode.getDrawableData()) {
				if(attr.getName().equals(this.nameConf.getConfiguration())) {
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public Collection<Pair<String, Configuration>> getConfiguration() {
		Collection<Pair<String,Configuration>> collection = new ArrayList<>();

		collection.add(new Pair<String, Configuration>(this.nameConf.getNameOfConfiguration(), this.nameConf));
		
		return collection;
	}

	@Override
	public void setNewConfiguration(Collection<Configuration> newConf) throws BadConfigurationException,
			BadArgumentException {
		if(newConf.size() != 1) {
			throw new BadConfigurationException("This Configurable requires 1 configurations");
		}
		
		Configuration first = newConf.iterator().next();
		
		if(!(first instanceof TextConfiguration)) {
			throw new BadArgumentException(TextConfiguration.class, first.getClass());
		} 

		this.nameConf.setValueConfiguration(((TextConfiguration)first).getConfiguration());
	}

}
