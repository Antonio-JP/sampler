package rioko.draw2d.listeners;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;

/**
 * Draw2 MouseListener and MouseMotionListener used to put together different listeners. It follows the Compose patron.
 * 
 * @author Antonio
 */
public class ComposeMouseListener implements MouseListener, MouseMotionListener {
	
	//Private attributes
	/**
	 * Array of listeners added to the compose
	 */
	private ArrayList<MouseListener> listeners;
	
	//Builders
	/**
	 * Simple builder that initializes the array to an empty one
	 */
	public ComposeMouseListener()
	{
		this.listeners = new ArrayList<>();
	}
	
	/**
	 * Builder that initializes the array with a collection of listeners
	 * 
	 * @param listeners Listeners to add
	 */
	public ComposeMouseListener(Collection<MouseListener> listeners)
	{
		this.listeners = new ArrayList<>(listeners);
	}
	
	//Metodos de control
	/**
	 * Public method to add a new listener to the compose
	 * @param listener Listener to add
	 */
	public void addMouseListener(MouseListener listener)
	{
		this.listeners.add(listener);
	}
	
	/***
	 * Public method to remove a listener from the compose
	 * 
	 * @param listener Listener to remove
	 * 
	 * @return True if the listeners was removed and false if it wasn't in the compose
	 */
	public boolean remoceMouseListener(MouseListener listener)
	{
		return this.listeners.remove(listener);
	}

	//MouseListener Interface methods
	@Override
	public void mousePressed(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			list.mousePressed(me);
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			list.mouseReleased(me);
		}
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			list.mouseDoubleClicked(me);
		}
	}

	//MouseMotionListener Interface methods
	@Override
	public void mouseDragged(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			if(list instanceof MouseMotionListener) {
				((MouseMotionListener)list).mouseDragged(me);
			}
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			if(list instanceof MouseMotionListener) {
				((MouseMotionListener)list).mouseEntered(me);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			if(list instanceof MouseMotionListener) {
				((MouseMotionListener)list).mouseExited(me);
			}
		}
	}

	@Override
	public void mouseHover(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			if(list instanceof MouseMotionListener) {
				((MouseMotionListener)list).mouseHover(me);
			}
		}
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		for(MouseListener list : this.listeners) {
			if(list instanceof MouseMotionListener) {
				((MouseMotionListener)list).mouseMoved(me);
			}
		}
	}

}
