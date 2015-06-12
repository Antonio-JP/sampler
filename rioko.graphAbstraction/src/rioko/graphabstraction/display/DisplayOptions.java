package rioko.graphabstraction.display;

public enum DisplayOptions {
	MAX_NODES, ROOT_NODE, LEVELS_TS, ECLASS_FILTER, STRONG_BOOLEAN;
	
	private static final String STR_ROOT_NODE = "Root Node";
	
	private static final String STR_MAX_NODES = "Nodes to Show";
	
	private static final String STR_ECLASS_FILTER = "EClass to Filter";

	private static final String STR_LEVELS_TS = "Levels to Show";

	private static final String STR_STRONG_BOOLEAN = "Strong Connection";

	private static final String STR_ERR = "Rioko ERROR: not existing option";
	
	@Override
	public String toString()
	{
		switch(this) 
		{
			case MAX_NODES:
				return STR_MAX_NODES;
			case ROOT_NODE:
				return STR_ROOT_NODE;
			case LEVELS_TS:
				return STR_LEVELS_TS;
			case ECLASS_FILTER:
				return STR_ECLASS_FILTER;
			case STRONG_BOOLEAN:
				return STR_STRONG_BOOLEAN;
		}
		
		return STR_ERR;
	}
}
