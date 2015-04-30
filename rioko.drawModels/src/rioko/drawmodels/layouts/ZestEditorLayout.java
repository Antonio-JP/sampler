package rioko.drawmodels.layouts;

import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

public class ZestEditorLayout extends CenterLayoutAlgorithm {
		
	//Builders

	public ZestEditorLayout(int styles) {
		super(styles);

		this.layoutStopped = false;
	}
	
	//AbstractLayoutAlgorithm Methods

	@Override
	protected void applyLayoutInternal(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double boundsX,
			double boundsY,	double boundsWidth, double boundsHeight) {
		
		int nEntities = entitiesToLayout.length;
		
		double widthStep = boundsWidth/nEntities;
		
		double x = boundsX;
		
		int i=0;
		//Pintamos todos los nodos en línea
		for(InternalNode node : entitiesToLayout) {
			node.setLocation(x, boundsY + (i+1)*boundsHeight/3);
			node.setLocationInLayout(x, boundsY + (i+1)*boundsHeight/3);
			
			x += widthStep;
			i = (i+1)%2;
		}
		
		//Se acaba el algoritmo
		this.layoutStopped = true;
	}

	@Override
	protected int getCurrentLayoutStep() {
		return 0;
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return 0;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		if(this.internalAsynchronous == asynchronous && this.internalContinuous == continuous) {
			return true;
		}
		
		return false;
	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		/* Do nothing */
	}
}
