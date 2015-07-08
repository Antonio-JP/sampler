//package rioko.zest.layout;
//
//import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
//import org.eclipse.zest.layouts.dataStructures.InternalNode;
//import org.eclipse.zest.layouts.dataStructures.InternalRelationship;
//
//import rioko.zest.layout.exceptions.OutOfBundlesException;
//
//public abstract class CustomLayoutAlgorithm extends AbstractLayoutAlgorithm implements AuxZestLayoutAlgorithm {
//
//	//Fields
//	private String name = this.getClass().getSimpleName();
//	
//	private AlgPhase phase = AlgPhase.OFF;
//	
//	private boolean initialMove = true, doneInitial = false;
//	
//	//Fields to know what are we drawing
//	protected InternalNode[] nodes;
//	protected InternalRelationship[] relations;
//	private Rectangle layoutArea;
//	
//	//Enums
//	protected enum AlgPhase {OFF, PREPARATION, RUNNING, ADJUST; 
//		
//		public static AlgPhase getPhase(int phase) {
//			switch(phase) {
//			case 0:
//				return AlgPhase.PREPARATION;
//			case 1:
//				return AlgPhase.RUNNING;
//			case 2:
//				return AlgPhase.ADJUST;
//			default:
//				return AlgPhase.OFF;
//			}
//		}
//	}
//	
//	//Builders
//	public CustomLayoutAlgorithm(int styles) {
//		super(styles);
//	}
//	
//	//Getting and Setters
//	protected Rectangle getLayoutArea() {
//		return this.layoutArea;
//	}
//	
//	protected void setInitialMove(boolean initialMove) {
//		this.initialMove = initialMove;
//	}
//	
//	protected boolean getInitialMove() {
//		return this.initialMove;
//	}
//	
//	//Methods from AbstractLayoutAlgorithm
//	@Override
//	protected void applyLayoutInternal(InternalNode[] nodes, InternalRelationship[] relations, double x, double y, double width, double height) {
//		
//		while(!this.phase.equals(AlgPhase.OFF)) {
//			
//			switch(this.phase) {
//				case OFF:
//					break;
//					
//				case PREPARATION:
//					/* Setting up phase */
//					if(! this.doneInitial) {
//						this.nodes = nodes;
//						this.relations = relations;
//						
//						/* Stablish the Layout Area */
//						this.setLayoutArea(x, y, width, height);
//						try {
//							this.layoutArea.setOrigin(this.layoutArea.getCenter());
//						} catch (OutOfBundlesException e) {
//							// Impossible Exception
//							e.printStackTrace();
//						}
//						
//						if(this.initialMove) {
//							for(InternalNode node : nodes) {
//								/* We set the position of the nodes at the center of the layout Area */
//								this.setDVector(node, Point.ZERO);
//								this.setLocation(node, Point.ZERO);
//							}
//							
//							this.doneInitial = true;
//						}
//					}
//					
//					this.preparation();
//					this.getNextPhase();
//					break;
//					
//				case RUNNING:
//					/* Main phase of the algorithm */
//					this.running();
//					this.getNextPhase();
//					break;
//					
//				case ADJUST:
//					/* Last phase of the Layout Algorithm */
//					this.adjust();
//					this.getNextPhase();
//					break;
//					
//				default:
//					break;
//			}
//		}
//	}
//
//	@Override
//	protected int getCurrentLayoutStep() {
//		switch(this.phase) {
//			case ADJUST:
//				return 2;
//			case OFF:
//				return -1;
//			case PREPARATION:
//				return 0;
//			case RUNNING:
//				return 1;
//			default:
//				return -2;
//		}
//	}
//
//	@Override
//	protected int getTotalNumberOfLayoutSteps() {
//		return 3;
//	}
//
//	@Override
//	protected boolean isValidConfiguration(boolean asynchronous, boolean continuous) {
//		return (!asynchronous) && (!continuous);
//	}
//
//	@Override
//	protected void postLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1) {
//		this.phase = AlgPhase.OFF;
//	}
//
//	@Override
//	protected void preLayoutAlgorithm(InternalNode[] arg0, InternalRelationship[] arg1, double x, double y, double width, double height) {
//		this.phase = AlgPhase.PREPARATION;
//		this.doneInitial = false;
//	}
//
//	@Override
//	public void setLayoutArea(double x, double y, double width, double height) {
//		this.layoutArea = new Rectangle(new Point(x,y), width, height);
//		try {
//			this.layoutArea.setOrigin(this.layoutArea.getCenter());
//		} catch (OutOfBundlesException e) {
//			// Impossible Exception
//			e.printStackTrace();
//		}
//	}
//
//	//Methods from AuxZestLayoutAlgorithm
//	@Override
//	public void setEnable(boolean enable) { }
//
//	@Override
//	public boolean isEnable() {
//		return true;
//	}
//
//	@Override
//	public boolean setName(String name) {
//		this.name  = name;
//		return true;
//	}
//
//	@Override
//	public String getName() {
//		return new String(this.name);
//	}
//
//	@Override
//	public abstract CustomLayoutAlgorithm copy();
//	
//	//Extra methods
//	protected void setDVector(InternalNode node, Point p) {
//		node.setDx(p.getX());
//		node.setDy(p.getY());
//	}
//	
//	protected void setLocation(InternalNode node, Point p) {
//		this.setLocationAbsolute(node, this.layoutArea.getAbsolute(p));
//	}
//	
//	private void setLocationAbsolute(InternalNode node, Point p) {
//		Point aux = p.sub(new Point(this.layoutArea.getLeft(), this.layoutArea.getTop()));
//		node.setLocation(aux.getX(), aux.getY());
//		node.setInternalLocation(aux.getX(), aux.getY());
//		node.setLocationInLayout(aux.getX(), aux.getY());
//	}
//	
//	/* Specific methods for each phase */
//	protected void preparation() {}
//	
//	protected abstract void running();
//	
//	protected void adjust() {
//		for(InternalNode node : this.nodes) {
//			this.setDVector(node, new Point(-node.getWidthInLayout()/2, -node.getHeightInLayout()/2));
//		}
//	}
//
//	/* Phase controling methods */
//	private void getNextPhase() {
//		switch(this.phase) {
//		case PREPARATION:
//			this.setNewPhase(this.getNextFromPreparation());
//			break;
//		case RUNNING:
//			this.setNewPhase(this.getNextFromRunning());
//			break;
//		case ADJUST:
//			this.setNewPhase(this.getNextFromAdjust());
//			break;
//		case OFF:
//			break;
//		default:
//			break;
//		
//		}
//	}
//
//	protected AlgPhase getNextFromPreparation() {
//		return AlgPhase.RUNNING;
//	}
//	protected AlgPhase getNextFromRunning() {
//		return AlgPhase.ADJUST;
//	}
//	protected AlgPhase getNextFromAdjust() {
//		return AlgPhase.OFF;
//	}
//
//	private void setNewPhase(AlgPhase next) {
//		this.phase = next;
//				
//		/* We update the position of the vertices of the layout */
//		for(InternalNode node : this.nodes) {
//			this.move(node);
//		}
//	}
//
//	private void move(InternalNode node) {
//		node.setInternalLocation(node.getInternalX() + node.getDx(), node.getInternalY() + node.getDy());
//		node.setLocation(node.getCurrentX() + node.getDx(), node.getCurrentY() + node.getDy());
//		node.setLocationInLayout(node.getXInLayout() + node.getDx(), node.getYInLayout() + node.getDy());
//		
//		/* Set the move vector to zero */
//		this.setDVector(node, Point.ZERO);
//	}
//	
//	// Image control methods
//	final protected Rectangle getRectangle(InternalNode node) {
//		return new Rectangle(this.getPosition(node), this.getWidth(node), this.getHeight(node));
//	}
//
//	final protected double getHeight(InternalNode node) {
//		return node.getHeightInLayout();
//	}
//
//	final protected double getWidth(InternalNode node) {
//		return node.getWidthInLayout();
//	}
//
//	final protected Point getPosition(InternalNode node) {
//		return new Point(node.getXInLayout(), node.getYInLayout());
//	}
//}
