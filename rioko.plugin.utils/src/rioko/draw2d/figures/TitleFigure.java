package rioko.draw2d.figures;

import org.eclipse.draw2d.Label;

import rioko.draw2d.borders.BottomBorder;

/**
 * Specific Figure with just a Label with left alignment and a bottom line.
 * 
 * @author Antonio
 */
public class TitleFigure extends VerticalFigure {

	/**
	 * Public builder with the label to draw
	 * 
	 * @param title Label to draw in the Figure
	 */
	public TitleFigure(Label title) {
		this.setBorder(new BottomBorder());
		
		this.add(title);
	}

}
