package rioko.zest.layout;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.zest.layout.exceptions.OutOfBundlesException;

public abstract class CustomLayoutAlgorithm extends AbstractLayoutAlgorithm implements ZestLayoutAlgorithm {

	//Fields
	private String name = this.getClass().getSimpleName();
	
	private AlgPhase phase = AlgPhase.OFF;
	
	private Rectangle layoutArea;
	private boolean initialMove = true;
	
	//Enums
	protected enum AlgPhase {OFF, PREPARATION, RUNNING, ADJUST; 
		
		public static AlgPhase getPhase(int phase) {
			switch(phase) {
			case 0:
				return AlgPhase.PREPARATION;
			case 1:
				return AlgPhase.RUNNING;
			case 2:
				return AlgPhase.ADJUST;
			default:
				return AlgPhase.OFF;
			}
		}
	}
	
	//Builders
	public CustomLayoutAlgorithm(int styles) {
		super(styles);
	}
	
	//Getting and Setters
	protected Rectangle getLayoutArea() {
		return this.layoutArea;
	}
	
	protected void setInitialMove(boolean initialMove) {
		this.initialMove = initialMove;
	}
	
	protected boolean getInitialMove() {
		return this.initialMove;
	}
	
	//Methods from AbstractLayoutAlgorithm
	@Override
	protected void applyLayoutInternal(InternalNode[] nodes, InternalRelationship[] relations, double x, double y, double width, double height) {
		AlgPhase currentPhase = AlgPhase.getPhase(this.getCurrentLayoutStep());
		
		switch(currentPhase) {
			case OFF:
				break;
				
			case PREPARATION:
				/* Setting up phase */
				/* Stablish the Layout Area */
				this.setLayoutArea(x, y, width, height);
				try {
					this.layoutArea.setOrigin(this.layoutArea.getCenter());
				} catch (OutOfBundlesException e) {
					// Impossible Exception
					e.printStackTrace();
				}
				
				if(this.initialMove) {
					for(InternalNode node : nodes) {
						/* We set the position of the nodes at the center of the layout Area */
						this.setDVector(node, Point.ZERO);
						this.setInternalLocation(node, Point.ZERO);
						this.setLocation(node, Point.ZERO);
						this.setLocationInLayout(node, Point.ZERO);
					}
				}
				
				this.preparation(nodes, relations);
				break;
				
			case RUNNING:
				/* Main phase of the algorithm */
				this.running(nodes, relations);
				break;
				
			case ADJUST:
				/* Last phase of the Layout Algorithm */
				this.adjust(nodes, relations);
				break;
				
			default:
				break;
		}
	}

	@Override
	protected int getCurrentLayoutStep() {
		switch(this.phase) {
			case ADJUST:
				return 2;
			case OFF:
				return -1;
			case PREPARATION:
				return 0;
			case RUNNING:
				return 1;
			default:
				return -2;
		}
	}

	@Override
	protected int getTotalNumberOfLayoutSteps() {
		return 3;
	}

	@Override
	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
		return (!asynchronous) && (!continuous);
	}

	@Override
	protected void postLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1) {
		this.phase = AlgPhase.PREPARATION;
	}

	@Override
	protected void preLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1, double x, double y, double width, double height) {
		this.phase = AlgPhase.OFF;
	}

	@Override
	public void setLayoutArea(double x, double y, double width, double height) {
		this.layoutArea = new Rectangle(new Point(x,y), width, height);
		try {
			this.layoutArea.setOrigin(this.layoutArea.getCenter());
		} catch (OutOfBundlesException e) {
			// Impossible Exception
			e.printStackTrace();
		}
	}

	//Methods from ZestLayoutAlgorithm
	@Override
	public void setEnable(boolean enable) { }

	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public boolean setName(String name) {
		this.name  = name;
		return true;
	}

	@Override
	public String getName() {
		return new String(this.name);
	}

	@Override
	public abstract CustomLayoutAlgorithm copy();
	
	//Extra methods
	protected void setDVector(InternalNode node, Point p) {
		node.setDx(p.getX());
		node.setDy(p.getY());
	}
	
	protected void setLocation(InternalNode node, Point p) {
		this.setLocationAbsolute(node, this.layoutArea.getAbsolute(p));
	}
	
	private void setLocationAbsolute(InternalNode node, Point p) {
		Point aux = p.sub(new Point(this.layoutArea.getLeft(), this.layoutArea.getTop()));
		node.setLocation(aux.getX(), aux.getY());
	}
	
	protected void setInternalLocation(InternalNode node, Point p) {
		this.setInternalLocationAbsolute(node, this.layoutArea.getAbsolute(p));
	}
	
	private void setInternalLocationAbsolute(InternalNode node, Point p) {
		Point aux = p.sub(new Point(this.layoutArea.getLeft(), this.layoutArea.getTop()));
		node.setInternalLocation(aux.getX(), aux.getY());
	}
	
	protected void setLocationInLayout(InternalNode node, Point p) {
		this.setLocationInLayoutAbsolute(node, this.layoutArea.getAbsolute(p));
	}
	
	private void setLocationInLayoutAbsolute(InternalNode node, Point p) {
		Point aux = p.sub(new Point(this.layoutArea.getLeft(), this.layoutArea.getTop()));
		node.setLocationInLayout(aux.getX(), aux.getY());
	}
	
	/* Specific methods for each phase */
	protected abstract void preparation(InternalNode[] nodes, InternalRelationship[] relations);
	
	protected abstract void running(InternalNode[] nodes, InternalRelationship[] relations);
	
	protected abstract void adjust(InternalNode[] nodes, InternalRelationship[] relations);

}
