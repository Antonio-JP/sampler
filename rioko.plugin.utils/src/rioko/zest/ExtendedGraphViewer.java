package rioko.zest;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;

/**
 * Class that extends the natural Zest GraphViewer giving an extra functionality.
 * 
 * @author Antonio
 */
public class ExtendedGraphViewer extends GraphViewer {
	
	/**
	 * Private field that manages if a layout algorithm is applied instantly or not
	 */
	private boolean layoutApplicable = true;
	
	/**
	 * Public builder of an ExtendedGraphViewer.
	 * 
	 * @param composite Composite in which the viewer will be placed. It must have a FillLayout.
	 * @param style SWT style associated to this GraphViewer.
	 */
	public ExtendedGraphViewer(Composite composite, int style) {
		super(composite, style);
	}
	
	//Getters & Setters
	/**
	 * Getter of the field layoutApplicable
	 * 
	 * @return boolean with the current value of the field.
	 */
	public boolean isLayoutApplicable() {
		return layoutApplicable;
	}
	
	/**
	 * Setter of the field layoutApplicable
	 * 
	 * @param layoutApplicable boolean with the new value.
	 */
	public void setLayoutApplicable(boolean layoutApplicable) {
		this.layoutApplicable = layoutApplicable;
	}	
	
	//Others methods
	/**
	 * Public method to get the IFigures in a position Point.
	 * 
	 * @param p Point where this method will search the IFigures.
	 * @return An array of IFigures
	 */
	public IFigure[] getFiguresAtPosition(Point p)
	{
		Rectangle bounds = this.getControl().getBounds();
		ArrayList<IFigure> list = new ArrayList<>();
		
		if(p.x >= bounds.x && p.x <= bounds.x + bounds.width &&
				p.y >= bounds.y && p.y <= bounds.y + bounds.height)
		{
			for(Object ob : this.getNodeElements())
			{
				if(ob instanceof Drawable)
				{
					Drawable node = (Drawable)ob;
					
					if(node.getFigure().getBounds().contains(p))
					{
						list.add(node.getFigure());
					}
				}
			}
		}
		
		return list.toArray(new IFigure[0]);
	}
	
	/**
	 * Public method to get the IFigures in a position given its coordinates. It is equivalent to getFiguresAtposition(new point(x,y)).
	 * 
	 * @param x X coordinate of the point where this method will search.
	 * @param y Y coordinate of the point where this method will search.
	 * @return An array of IFigures
	 */
	public IFigure[] getFiguresAtPosition(int x, int y)
	{
		return this.getFiguresAtPosition(new Point(x,y));
	}

	@Override
	public void applyLayout()
	{
		if(this.layoutApplicable) {
			super.applyLayout();
		}
	}
	
	/**
	 * Public method to apply the layout algorithm even when the configuration does not allow it.
	 */
	public void forceApplyLayout()
	{
		super.applyLayout();
	}

}
