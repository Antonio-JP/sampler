package rioko.draw2d.listeners;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.UpdateManager;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * MouseListener and MouseMotionListener to drag figures throught the window
 * 
 * @author Antonio
 */
public class DraggingMouseListener implements MouseListener, MouseMotionListener {

	//Private attributes
	/**
	 * Gather the previous position of the figure
	 */
	private Point prevLoc = null;
	
	/**
	 * Indicates if the user is already clicking on the figure
	 */
	private boolean isClicked = false;
	
	/**
	 * Figure related with this listener
	 */
	private IFigure figure;
	
	//Builders
	/**
	 * Basic builder that relates the listener with an existing figure
	 * 
	 * @param figure Figuro who coul be dragged
	 */
	public DraggingMouseListener(IFigure figure)
	{
		this.figure = figure;
	}
	
	//MouseListener Interface methods
	@Override
	public void mousePressed(MouseEvent me) {
		this.isClicked = true;
		this.prevLoc = me.getLocation();
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		this.isClicked = false;
		this.prevLoc = null;
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) { /* Do nothing */ }

	//MouseMotionListener Interface methods
	@Override
	public void mouseDragged(MouseEvent me) {
		if(this.isClicked) {
			Point newLocation = me.getLocation();
			
			if (newLocation == null) {
				return;
			}
			Dimension offset = newLocation.getDifference(this.prevLoc);
			if (offset.width == 0 && offset.height == 0) {
				return;
			}
			this.prevLoc = newLocation;
			
			UpdateManager updateMgr = figure.getUpdateManager();
			LayoutManager layoutMgr = figure.getParent().getLayoutManager();
			Rectangle bounds = figure.getBounds();
			updateMgr.addDirtyRegion(figure.getParent(), bounds);
			bounds = bounds.getCopy().translate(offset.width, offset.height);
			layoutMgr.setConstraint(figure, bounds);
			figure.translate(offset.width, offset.height);
			updateMgr.addDirtyRegion(figure.getParent(), bounds);
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseExited(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseHover(MouseEvent me) { /* Do nothing */ }

	@Override
	public void mouseMoved(MouseEvent me) { /* Do nothing */ }
}
