package rioko.drawmodels.editors.zesteditor.zestproperties;

import rioko.drawmodels.configurations.AggregationAlgorithmConfiguration;
import rioko.drawmodels.configurations.LayoutAlgorithmConfiguration;
import rioko.graphabstraction.configurations.BooleanConfiguration;
import rioko.graphabstraction.configurations.Configuration;

public enum ZestGenericProperties {
	LAYOUT_ALG, AGGREGATION_ALG, SHOW_ATTR, SHOW_CON;
	
	private static final String STR_LAYOUT_ALG = "Layout Algorithm";
	
	private static final String STR_AGGREGATION_ALG = "Visualization Criteria";
	
	private static final String STR_SHOW_ATTR = "Show attributes";
	
	private static final String STR_SHOW_CON = "Show connections";
	
	@Override
	public String toString() {
		switch(this) {
			case AGGREGATION_ALG:
				return STR_AGGREGATION_ALG;
			case LAYOUT_ALG:
				return STR_LAYOUT_ALG;
			case SHOW_ATTR:
				return STR_SHOW_ATTR;
			case SHOW_CON:
				return STR_SHOW_CON;
			default:
				return null;
		}
	}
	
	public Class<? extends Configuration> getConfigurationNeeded() {
		switch(this) {
		case AGGREGATION_ALG:
			return AggregationAlgorithmConfiguration.class;
		case LAYOUT_ALG:
			return LayoutAlgorithmConfiguration.class;
		case SHOW_ATTR:
			return BooleanConfiguration.class;
		case SHOW_CON:
			return BooleanConfiguration.class;
		default:
			return null;
		}
	}
}
