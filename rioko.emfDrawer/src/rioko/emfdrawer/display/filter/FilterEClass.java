package rioko.emfdrawer.display.filter;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.ecore.EClass;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.emfdrawer.XMIModelDiagram;
import rioko.emfdrawer.configurations.EClassConfiguration;
import rioko.emfdrawer.filters.ByEClass;
import rioko.graphabstraction.display.FilterNestedBuilder;

public class FilterEClass extends FilterNestedBuilder {
		
	//GraphBuilder methods
	@Override
	public Collection<Class<? extends Configuration>> getConfigurationNeeded() {
		
		Collection<Class<? extends Configuration>> res = new ArrayList<>(1);
		if(this.filter == null || this.filter.getConfiguration().isEmpty() || this.filter.getConfiguration().iterator().next().getLast().getConfiguration() == null) {
			res.add(EClassConfiguration.class);
		} 
		
		return res;
	}
	
	//FilterNestedBuilder methods
	@Override
	protected FilterOfVertex getFilter(DiagramGraph data, Configurable properties) {
		ByEClass filterOfVertex = new ByEClass();
		
		if(data != null || properties != null) {			
			XMIModelDiagram model = new XMIModelDiagram(data);
			EClass finalEClass = null;
			for(EClass eClass : model.getEClassList()) {
				if(eClass.equals(properties.getConfiguration(EClassConfiguration.class))) {
					finalEClass = eClass;
					break;
				}
			}
			
			if(finalEClass == null && this.filter != null) {
				finalEClass = (EClass)this.filter.getConfiguration("EClass");
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
