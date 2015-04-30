package rioko.graphabstraction.draw2d.listeners;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.graphics.Color;

import rioko.draw2d.listeners.ComposeMouseListener;
import rioko.draw2d.listeners.DraggingMouseListener;

/**
 * Specific ComposeMouseListener which put together the ChangeColorOnFocusListener and DraggingMouseListener. It is used by 
 * ModelNodeFigure as standard MouseListener.
 * 
 * @author Antonio
 */
public class ModelNodeFigureMouseListener extends ComposeMouseListener {
	/**
	 * Generic builder with the parameters needed by ChangeColorOnFocusListener and DraggingMouseListener
	 * 
	 * @param gained Color that will be put when the Figure get the focus
	 * @param lost Color that will be put when the Figure lose the focus
	 * @param figure Figure that will be related with this listener
	 */
	public ModelNodeFigureMouseListener(Color gained, Color lost, IFigure figure)
	{
		super();
		
		this.addMouseListener(new ChangeColorOnFocusListener(gained,lost,figure));
		this.addMouseListener(new DraggingMouseListener(figure));
	}
}
