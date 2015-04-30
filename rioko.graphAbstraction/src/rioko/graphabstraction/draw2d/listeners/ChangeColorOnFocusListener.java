package rioko.graphabstraction.draw2d.listeners;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.swt.graphics.Color;

/**
 * MouseListener related with an IFigure used to change between two background colors in a Figure
 * 
 * @author Antonio
 */
public class ChangeColorOnFocusListener implements MouseListener {
	
	//Static attributes
	private static ArrayList<ChangeColorOnFocusListener> ActualListeners = new ArrayList<>();
	
	//Private attributes
	/**
	 * Color between swap
	 */
	private Color colorOnFocusGained, colorOnFocusLost;
	
	/**
	 * Figure which will change
	 */
	private IFigure figure;
	
	//Builders	
	/**
	 * Generic builder to create the listener. It recieves the colors between swap and the figure that will change.
	 * 
	 * @param gained Color that will be put when the Figure get the focus
	 * @param lost Color that will be put when the Figure lose the focus
	 * @param figure Figure that will be related with this listener
	 */
	public ChangeColorOnFocusListener(Color gained, Color lost, IFigure figure)
	{
		this.colorOnFocusGained = gained;
		this.colorOnFocusLost = lost;
		
		this.figure = figure;
		
		ActualListeners.add(this);
	}
	
	//MouseListener Interface methods
	@Override
	public void mousePressed(MouseEvent me) {
		ChangeColorOnFocusListener.clean();
		
		this.figure.setBackgroundColor(this.colorOnFocusGained);
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		
	}

	@Override
	public void mouseDoubleClicked(MouseEvent me) {
		
	}
	
	//Static methods
	/**
	 * Private static method to restart the color of all listeners to its colorOnFocusLost Color.
	 */
	private static void clean() {
		for(ChangeColorOnFocusListener list : ActualListeners)
		{
			list.figure.setBackgroundColor(list.colorOnFocusLost);;
		}
	}
}
