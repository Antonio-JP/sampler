package rioko.drawmodels.algorithms.display;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.graphabstraction.display.LocalNestedBuilder;
import rioko.graphabstraction.display.NestedGraphBuilder;

public class JustFiltersBuilder extends NestedBuilderAlgorithm {
	public JustFiltersBuilder() {
		super("Just filters algorithm");
	}
	
	@Override
	public boolean addGlobalCriteria(GlobalNestedBuilder globalStep) {
		return false;
	}
	
	@Override
	public boolean addLocalCriteria(LocalNestedBuilder localStep) {
		return false;
	}
	
	@Override
	public boolean addOtherStep(NestedGraphBuilder step, int position) {
		return false;
	}
}
