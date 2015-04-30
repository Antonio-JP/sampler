package rioko.graphabstraction.diagram.filters;

import java.util.Collection;

import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;

public class AndComposeFilter extends ComposeFilter{

	public AndComposeFilter() {
		super();
	}	
	
	public AndComposeFilter(Collection<? extends FilterOfVertex> collection) {
		super(collection);
	}

	//Abstract methods implementation
	@Override
	protected boolean filter(DiagramNode node, Configurable properties, DiagramGraph graph) {
		boolean res = true;
		
		for(FilterOfVertex filt : this.filters) {
			res &= filt.filter(node, properties, graph);
		}
		
		return res;
	}
}
