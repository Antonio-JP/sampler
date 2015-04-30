package rioko.drawmodels.algorithms.display.test;

import rioko.drawmodels.display.local.ModelTreeAlgorithm;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.global.GlobalLeavesCompactificationNestedBuilder;

public class SimpleGlobalAndLocalAlgorithm extends NestedBuilderAlgorithm {
		
	public SimpleGlobalAndLocalAlgorithm()
	{
		super("Leaves-Local");
		
		this.addGlobalCriteria(new GlobalLeavesCompactificationNestedBuilder());
		this.addLocalCriteria(new ModelTreeAlgorithm());
	}
}
