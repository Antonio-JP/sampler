package rioko.draw2d.figures;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;

/**
 * Generic Figure with an horizontal Layout
 * 
 * @author Antonio
 */
public class HorizontalFigure extends Figure {
	public HorizontalFigure() {
		FlowLayout layout = new FlowLayout();
		
		this.setLayoutManager(layout);	
	}
}
