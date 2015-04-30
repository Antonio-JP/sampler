package rioko.drawmodels.display.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.drawmodels.configurations.EClassConfiguration;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.diagram.filters.ByEClass;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.display.FilterNestedBuilder;

public class FilterEClass extends FilterNestedBuilder {
	
	//GraphBuilder methods
	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		
		Collection<DisplayOptions> res = new ArrayList<>(1);
		if(this.filter.getConfiguration().isEmpty() || this.filter.getConfiguration().iterator().next().getLast().getConfiguration() == null) {
			res.add(DisplayOptions.ECLASS_FILTER); 
		}
		
		return res;
	}
	
	//FilterNestedBuilder methods
	@Override
	protected FilterOfVertex getFilter(DiagramGraph data, Configurable properties) {
		ByEClass filterOfVertex = new ByEClass();
		
		if(data != null && properties != null) {
			ModelDiagram model = new ModelDiagram(data);
			EClass finalEClass = null;
			for(EClass eClass : model.getEClassList()) {
				if(eClass.equals(properties.getConfiguration(DisplayOptions.ECLASS_FILTER.toString()))) {
					finalEClass = eClass;
					break;
				}
			}
			
			if(finalEClass != null) {
				try {
					filterOfVertex.setConfiguration(new EClassConfiguration(model, finalEClass));
				} catch (BadConfigurationException | BadArgumentException e) {
					// Impossible Exception
					e.printStackTrace();
				}
			}
		}
		
		return filterOfVertex;
	}
}
