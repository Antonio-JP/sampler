package rioko.layouts.algorithms;

import rioko.layouts.geometry.Point;
import rioko.layouts.geometry.Rectangle;
import rioko.layouts.geometry.exceptions.OutOfBundlesException;
import rioko.layouts.graphImpl.LayoutGraph;
import rioko.layouts.graphImpl.LayoutVertex;

public abstract class AbstractLayoutAlgorithm implements LayoutAlgorithm {
	
	private String name = this.getClass().getSimpleName();
	
	private AlgorithmPhase phase = AlgorithmPhase.OFF;
	
	private boolean initialMove = true, doneInitial = false;
	
	//Fields to know what are we drawing
	protected LayoutGraph graph = null;
	private Rectangle layoutArea = null;
	
	//Getters & Setters
	private void setLayoutArea(Rectangle layoutArea) {
		this.layoutArea = layoutArea;
	}
	
	protected Rectangle getLayoutArea() {
		return this.layoutArea;
	}
	
	protected void setInitialMove(boolean initialMove) {
		this.initialMove = initialMove;
	}
		
	protected boolean getInitialMove() {
		return this.initialMove;
	}

	//LayoutAlgorithm methods
	@Override
	public boolean setName(String name) {
		this.name = name;
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void applyLayout(LayoutGraph graphToLayout, Rectangle layoutArea) {
		this.doneInitial = false;
		this.phase = AlgorithmPhase.PREPARATION;
		
		while(!this.phase.equals(AlgorithmPhase.OFF)) {
			
			switch(this.phase) {
				case OFF:
					break;
					
				case PREPARATION:
					/* Setting up phase */
					if(! this.doneInitial) {
						/* Saving the current graph */
						this.graph = graphToLayout;
						
						/* Stablish the Layout Area */
						this.setLayoutArea(layoutArea);
						try {
							this.layoutArea.setOrigin(this.layoutArea.getCenter());
						} catch (OutOfBundlesException e) {
							// Impossible Exception
							e.printStackTrace();
						}
						
						if(this.initialMove) {
							for(LayoutVertex node : this.graph.vertexSet()) {
								/* We set the position of the nodes at the center of the layout Area */
								this.setDVector(node, Point.ZERO);
								this.setLocation(node, Point.ZERO);
							}
							
							this.doneInitial = true;
						}
					}
					
					this.preparation();
					this.getNextPhase();
					break;
					
				case RUNNING:
					/* Main phase of the algorithm */
					this.running();
					this.getNextPhase();
					break;
					
				case ADJUST:
					/* Last phase of the Layout Algorithm */
					this.adjust();
					this.getNextPhase();
					break;
					
				default:
					break;
			}
		}
	}
	
	/* Movement methods */
	protected void setDVector(LayoutVertex node, Point p) {
		node.setMove(p);
	}
	
	protected void setLocation(LayoutVertex node, Point p) {
		this.setLocationAbsolute(node, this.layoutArea.getAbsolute(p));
	}
	
	private void setLocationAbsolute(LayoutVertex node, Point p) {
		Point aux = p.sub(new Point(this.layoutArea.getLeft(), this.layoutArea.getTop()));
		node.setPosition(aux);
	}
	
	/* Specific methods for each phase */
	protected void preparation() {}
	
	protected abstract void running();
	
	protected void adjust() {
		for(LayoutVertex node : this.graph.vertexSet()) {
			this.setDVector(node, new Point(-node.getWidth()/2, -node.getHeight()/2));
		}
	}

	/* Phase controling methods */
	private void getNextPhase() {
		switch(this.phase) {
		case PREPARATION:
			this.setNewPhase(this.getNextFromPreparation());
			break;
		case RUNNING:
			this.setNewPhase(this.getNextFromRunning());
			break;
		case ADJUST:
			this.setNewPhase(this.getNextFromAdjust());
			break;
		case OFF:
			break;
		default:
			break;
		
		}
	}

	protected AlgorithmPhase getNextFromPreparation() {
		return AlgorithmPhase.RUNNING;
	}
	protected AlgorithmPhase getNextFromRunning() {
		return AlgorithmPhase.ADJUST;
	}
	protected AlgorithmPhase getNextFromAdjust() {
		return AlgorithmPhase.OFF;
	}

	private void setNewPhase(AlgorithmPhase next) {
		this.phase = next;
				
		/* We update the position of the vertices of the layout */
		for(LayoutVertex node : this.graph.vertexSet()) {
			node.move();
		}
	}
	
	// Image control methods
	final protected Rectangle getRectangle(LayoutVertex node) {
		return new Rectangle(node.getPosition(), node.getWidth(), node.getHeight());
	}
}
