package rioko.graphabstraction.diagram.filters;

import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;

public class OrComposeFilter extends ComposeFilter {

	public OrComposeFilter() {
		super();
	}

	public OrComposeFilter(Collection<? extends FilterOfVertex> collection) {
		super(collection);
	}

	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		boolean res = false;
		
		for(FilterOfVertex filt : this.filters) {
			res |= filt.filter(node, properties, graph);
		}
		
		return res;
	}

}
