package rioko.draw2d.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ToolbarLayout;

/**
 * Generic Figure with a vertical Layout
 * 
 * @author Antonio
 */
public class VerticalFigure extends Figure {
	/**
	 * Public builder with you can choose the alignment on the figure data
	 * 
	 * @param alignment Integer with the ToolbarLayout alignment
	 */
	public VerticalFigure(int alignment)
	{
		ToolbarLayout layout = new ToolbarLayout();
	    layout.setMinorAlignment(alignment);
	    layout.setStretchMinorAxis(false);
	    layout.setSpacing(2);
	    layout.setStretchMinorAxis(true);
	    		
		this.setLayoutManager(layout);	
	}
	
	/**
	 * Public empty builder. It returns a new Figure with left alignment.
	 */
	public VerticalFigure() {
		this(ToolbarLayout.ALIGN_TOPLEFT);
	}
}
