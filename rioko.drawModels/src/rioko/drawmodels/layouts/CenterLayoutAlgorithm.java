package rioko.drawmodels.layouts;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.drawmodels.layouts.geometry.DoubleRectangle;

public abstract class CenterLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	private DoubleRectangle bounds = null;

	public CenterLayoutAlgorithm(int styles) {
		super(styles);
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider) {
		// Centramos el contenido (El dibujo queda dibujado en el interior de un rectángulo contrado en el centro del hueco para el Layout
				double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY=Double.MIN_VALUE;
				
				//Calculamos el mínimo y el máximo de cada nodo:
				for(InternalNode node : entitiesToLayout) {
					if(minX > this.getLeftNode(node)) {
						minX = this.getLeftNode(node);
					}
					
					if(maxX < this.getRightNode(node)) {
						maxX = this.getRightNode(node);
					}
					
					if(minY > this.getUpNode(node)) {
						minY = this.getUpNode(node);
					}
					
					if(maxY < this.getDownNode(node)) {
						maxY = this.getDownNode(node);
					}
				}
				
				//Ahora calculamos el desfase de x e y para centrar el dibujo
				double chX = ((this.bounds.getWidth() - this.bounds.getX()) - (minX + maxX))/2;
				double chY = ((this.bounds.getHeight() - this.bounds.getY()) - (minY + maxY))/2;
				
				//Movemos todos los nodos lo que indique el vector (chX, chY)
				for(InternalNode node : entitiesToLayout) {
					node.setLocation(getLeftNode(node) + chX, this.getUpNode(node) + chY);
				}
	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] entitiesToLayout, InternalRelationship[] relationshipsToConsider, double x, double y,
			double width, double height) {
		//Guardamos el tamaño de la zona de pintado en un Rectángulo
				this.bounds = new DoubleRectangle(x,y,width,height);
	}
	
	//Methods to get the limits of a node
	protected double getRightNode(InternalNode node) {
		return node.getXInLayout() + node.getWidthInLayout();
	}
	
	protected double getLeftNode(InternalNode node) {
		return node.getXInLayout();
	}
	
	protected double getUpNode(InternalNode node) {
		return node.getYInLayout();
	}
	
	protected double getDownNode(InternalNode node) {
		return node.getYInLayout() + node.getHeightInLayout();
	}
}
