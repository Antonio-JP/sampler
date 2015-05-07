package rioko.zest;

import org.eclipse.draw2d.IFigure;

/**
 * Interface used in Zest components which indicates that an object can be draw.
 * 
 * @author Antonio
 *
 */
public interface Drawable {
	/**
	 * Public method to get the image to be draw.
	 * 
	 * @return IFigure with the object drawn.
	 */
	public IFigure getFigure();
}
