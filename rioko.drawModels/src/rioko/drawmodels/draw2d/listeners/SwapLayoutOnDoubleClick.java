package rioko.drawmodels.draw2d.listeners;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.zest.layouts.LayoutAlgorithm;

import rioko.utilities.Log;

@Deprecated
/**
 * MouseListener use to change the Layout algorithm in a Zest Editor when double click.
 * 
 * It is actually deprecated, so no more documentation is given.
 * 
 * @author Antonio
 */
public class SwapLayoutOnDoubleClick implements MouseListener {
	
	private int activeLayout = 0;
	
	private ArrayList<LayoutAlgorithm> possibleAlgorithms = new ArrayList<>();
	
	private Object object;
	
	private Method setLayoutAlgorithm;
	
	public SwapLayoutOnDoubleClick(LayoutAlgorithm base, Object object)
	{
		this.activeLayout = 0;
		this.possibleAlgorithms.add(base);
		
		this.object = object;
		
		//We check if the objects has the right methods in it
		Method[] methods = object.getClass().getMethods();
		
		for (Method m : methods) {
			if (m.getName().equals("setLayoutAlgorithm") && m.getParameterTypes().length == 2) {
			  	this.setLayoutAlgorithm = m;
		    	break;
			}
		}
	}
	
	public void addAlgorithm(LayoutAlgorithm algorithm)
	{
		this.possibleAlgorithms.add(algorithm);
	}
	
	public LayoutAlgorithm getActualAlgorithm()
	{
		return this.possibleAlgorithms.get(this.activeLayout);
	}

	@Override
	public void mouseDoubleClick(MouseEvent arg0) {
		//Cambiamos al siguiente
		this.activeLayout = (this.activeLayout + 1)%this.possibleAlgorithms.size();
		
		
		//Aplicamos el nuevo layout al objeto
		try {
			this.setLayoutAlgorithm.invoke(this.object, this.getActualAlgorithm(), true);
		} catch (IllegalAccessException e) {
			Log.exception(e);
		} catch (IllegalArgumentException e) {
			Log.exception(e);
		} catch (InvocationTargetException e) {
			Log.exception(e);
		}
		
	}

	@Override
	public void mouseDown(MouseEvent arg0) { /* Do nothing */ }

	@Override
	public void mouseUp(MouseEvent arg0) { /* Do nothing */ }

}
