package rioko.draw2d.figures;

import org.eclipse.swt.graphics.Color;

import rioko.draw2d.borders.TopBorder;

/**
 * Generic compartiment Figure used to have more data with vertical layout and a top line
 * 
 * @author Antonio
 */
public class CompartimentFigure extends VerticalFigure {

	public CompartimentFigure() {
	    super();
	    
	    this.setBorder(new TopBorder());	    
	    this.setBackgroundColor(new Color(null, 255,0,0));
	  }
}
