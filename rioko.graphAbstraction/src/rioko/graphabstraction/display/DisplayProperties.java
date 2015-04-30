package rioko.graphabstraction.display;

import rioko.utilities.Copiable;
import rioko.graphabstraction.diagram.DiagramNode;

public class DisplayProperties implements Copiable {
	
	
	//Private attributes
	private int maxNodes = -1;
	
	private DiagramNode rootNode = null;
	
	private int levelsToShow = -1;
	
	private String filterByText = null;

	//Builders
	public DisplayProperties() { }
	
	public DisplayProperties(int maxNodes, DiagramNode rootNode, int levelsToShow, String filterByText) {
		this.maxNodes = maxNodes;
		this.rootNode = rootNode;
		this.levelsToShow = levelsToShow;
		this.filterByText = filterByText;
	}
	
	public DisplayProperties(DisplayOptions attribute, Object newAttr) {
		this();
		this.setAttribute(attribute, newAttr);
	}
	
	public DisplayProperties(DisplayOptions[] attrs, Object[] newAttrs) {
		this();
		
		if(attrs.length < newAttrs.length) //Error, se queda sin configurar nada
		{
			return;
		}
		
		for(int i=0; i < attrs.length; i++) {
			this.setAttribute(attrs[i], newAttrs[i]);
		}
	}
	
	public DisplayProperties(int maxNodes, DiagramNode rootNode) {
		this(maxNodes,rootNode,-1, null);
	}
	
	public DisplayProperties(int levelsToShow) {
		this(-1,null,levelsToShow, null);
	}
	
	//Getters & Setters
	public int getMaxNodes() {
		return maxNodes;
	}

	public void setMaxNodes(int maxNodes) {
		this.maxNodes = maxNodes;
	}

	public DiagramNode getRootNode() {
		return rootNode;
	}

	public void setRootNode(DiagramNode rootNode) {
		this.rootNode = rootNode;
	}

	public int getLevelsToShow() {
		return levelsToShow;
	}

	public void setLevelsToShow(int levelsToShow) {
		this.levelsToShow = levelsToShow;
	}
	
	public String getFilterByText() {
		return filterByText;
	}

	public void setFilterByText(String filterByText) {
		this.filterByText = filterByText;
	}
	
	public void setAttribute(DisplayOptions attribute, Object newAttr) {
		if(this.checkAttribute(attribute, newAttr)) {
			switch(attribute) {
				case MAX_NODES:
					this.setMaxNodes((Integer)newAttr);
					break;
				case ROOT_NODE:
					this.setRootNode((DiagramNode)newAttr);
					break;
				case LEVELS_TS:
					this.setLevelsToShow((Integer)newAttr);
					break;
				case ECLASS_FILTER:
					this.setFilterByText((String)newAttr);
					break;
			}
		}
	}
	
	public Object getAttribute(DisplayOptions attribute) {
		switch(attribute) {
			case MAX_NODES:
				return this.getMaxNodes();
			case ROOT_NODE:
				return this.getRootNode();
			case LEVELS_TS:
				return this.getLevelsToShow();
			case ECLASS_FILTER:
				return this.getFilterByText();
		}
	
	return false;
	}
	//Checking Methods
	public boolean isSet(DisplayOptions attribute) {
		switch(attribute) {
			case MAX_NODES:
				return this.isSetMaxNodes();
			case ROOT_NODE:
				return this.isSetRootNode();
			case LEVELS_TS:
				return this.isSetLevelsToShow();
			case ECLASS_FILTER:
				return this.isSetFilterByText();
		}
		
		return false;
	}
	
	public boolean isSet()
	{
		boolean res = false;
		
		for(DisplayOptions option : DisplayOptions.values()) {
			res |= this.isSet(option);
		}
		
		return res;
	}
	
	public boolean isSet(DisplayOptions[] attrs) {
		boolean res = true;
		
		for(DisplayOptions option : attrs) {
			res &= this.isSet(option);
		}
		
		return res;
	}
	
	public boolean isSetMaxNodes()
	{
		return this.maxNodes > 0;
	}
	
	public boolean isSetRootNode()
	{
		return this.rootNode != null;
	}
	
	public boolean isSetLevelsToShow()
	{
		return this.levelsToShow > 0;
	}
	
	public boolean isSetFilterByText()
	{
		return this.filterByText != null;
	}

	//Copiable interface methods
	public DisplayProperties copy() {
		DisplayProperties newProperties = new DisplayProperties();
		
		for(DisplayOptions option : DisplayOptions.values()) {
			newProperties.setAttribute(option, this.getAttribute(option));
		}
		
		return newProperties;
	}
	
	//Private methods	
	private boolean checkAttribute(DisplayOptions attribute, Object newAttr) {
		if(newAttr != null) {
			switch(attribute) {
				case MAX_NODES:
					return (newAttr instanceof Integer);
				case ROOT_NODE:
					return (newAttr instanceof DiagramNode);
				case LEVELS_TS:
					return (newAttr instanceof Integer);
				case ECLASS_FILTER:
					return (newAttr instanceof String);
			}
			
			return false;
		}
		
		return true;
	}

}
