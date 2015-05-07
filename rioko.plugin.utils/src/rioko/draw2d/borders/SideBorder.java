package rioko.draw2d.borders;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;

/**
 * Class extending AbstractBorder and give an interface to manage standard box borders.
 * 
 * @author Antonio 
 */
public class SideBorder extends AbstractBorder {
	
	/**
	 * side: int with indicate the lines of the border
	 */
	int side = 0;
	
	public static final int UP_BORDER = 1, DOWN_BORDER = 2, LEFT_BORDER = 4, RIGHT_BORDER = 8;
	
	/**
	 * Public constructor indicating in the parameter the sides of the box to draw
	 * 
	 * @param side Value with the lines to draw
	 */
	public SideBorder (int side)
	{
		this.side = side;
	}

	@Override
	public Insets getInsets(IFigure figure) {
		return new Insets(this.hasBorder(UP_BORDER), this.hasBorder(LEFT_BORDER),this.hasBorder(DOWN_BORDER),this.hasBorder(RIGHT_BORDER));
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		graphics.drawLine(getPaintRectangle(figure, insets).getTopLeft(), tempRect.getTopRight());

	}
	
	/**
	 * Private method which indicates if the SideBorder has to draw a side
	 * 
	 * @param side 1,2,4,8 value indicating the line to check
	 * 
	 * @return 1 if it is necessary to draw the side indicated and 0 otherwise.
	 */
	private int hasBorder(int side) {
		return (this.side & side)/side;
	}

}
