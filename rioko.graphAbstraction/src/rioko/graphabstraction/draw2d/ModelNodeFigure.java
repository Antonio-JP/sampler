package rioko.graphabstraction.draw2d;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import rioko.draw2d.figures.CompartimentFigure;
import rioko.draw2d.figures.TitleFigure;
import rioko.draw2d.figures.VerticalFigure;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.draw2d.listeners.ModelNodeFigureMouseListener;

/**
 * Generic Figure used by the DiagramNodes to create a visible Zest image. It has a vertical layout and a complete border.
 * 
 * It is composed by two essential figures: the title figure and the attribute figure. It is possible choose the background color of the figure, 
 * the standard color is blue.
 * 
 * @author Antonio
 */
public class ModelNodeFigure extends VerticalFigure {

	//Static attributes
	/**
	 * The color of background when the figure is clicked.
	 */
	public static Color activeColor = new Color(null,255,235,94);
	
	//Private attributes
	/**
	 * The basic color of background
	 */
	private Color passiveColor;
	
	/**
	 * The figure with the data
	 */
	private CompartimentFigure attributeFigure = new CompartimentFigure();
	
	/**
	 * A reference to the DiagramNode which has created this figure
	 */
	private DiagramNode referredNode = null;
	
	/**
	 * A MouseListener to catch different mouse events. See ModelNodeFigureMouseListener to more information.
	 */
	private ModelNodeFigureMouseListener listener = null;
	

	//Builders with Figures
	/**
	 * Generic builder with a label as title input and without any data input. It is possible choose a color to the background.
	 * 
	 * @param name Title of the figure
	 * @param passiveColor Color to the background
	 */
	public ModelNodeFigure(Label name, Color passiveColor) {
		super();
		
		this.passiveColor = passiveColor;

		this.setBorder(new LineBorder(ColorConstants.black,1));
		this.setBackgroundColor(this.passiveColor);
		this.setOpaque(true);
				
		this.add(new TitleFigure(name));	
		this.add(attributeFigure);
		
		//Añadimos el listener para cambiar de color
		this.listener = new ModelNodeFigureMouseListener(activeColor, this.passiveColor, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);
	}
	
	public ModelNodeFigure(Label name, IFigure data, Color passiveColor) {
		this(name, passiveColor);
		
		this.attributeFigure.add(data);
	}
	
	/**
	 * Basic builder with a Label with the title of the figure. It initializes the listener too.
	 * 
	 * @param name Title of the figure
	 */
	public ModelNodeFigure(Label name) {
		this(name, new Color(null,0,255,210));
	}
	
	/**
	 * Specific builder with a Figure to put in on the data compartiment.
	 * 
	 * @param name Title of the figure
	 * @param data Data tu show on the attribute compartiment
	 */
	public ModelNodeFigure(Label name, IFigure data) {
		this(name);
		
		this.attributeFigure.add(data);
	}
	
	//Builders with DiagramNode
	/**
	 * Generic builder with a DaigramNode as data input and a Color to the background.
	 * 
	 * @param node DiagramNode to show
	 * @param showData Boolean selecting if show or not the data of node
	 * @param passiveColor Color to the background
	 */
	public ModelNodeFigure(DiagramNode node, boolean showData, Color passiveColor)
	{
		super();
		
		this.passiveColor = passiveColor;
		
		if(node.isRoot()) {//Si es un nodo raíz, ponemos el borde un poco más gordo para que destaque:
			this.setBorder(new LineBorder(ColorConstants.red,2));
		} else {
			this.setBorder(new LineBorder(ColorConstants.black,1));
		}
		
		
		this.setBackgroundColor(this.passiveColor);
		this.setOpaque(true);
		
		Label name = new Label(node.getTitle());
		name.setFont(new Font(null,"Arial", 12, SWT.BOLD));
		name.setForegroundColor(new Color(null,9,40,101));
		
		this.add(name);
		
		this.add(attributeFigure);
		
		//Añadimos el listener para cambiar de color
		this.listener = new ModelNodeFigureMouseListener(activeColor, this.passiveColor, this);
		this.addMouseListener(listener);
		this.addMouseMotionListener(listener);	
		
		if(showData) {
			this.attributeFigure.add(node.getDataFigure());
		}
		
		this.referredNode = node;
	}
	
	/**
	 * Builder with a node as input showing its attributes
	 * 
	 * @param node DiagramNode to show
	 * @param passiveColor Color to the background
	 */
	public ModelNodeFigure(DiagramNode node, Color passiveColor)
	{
		this(node, true, passiveColor);
	}
	
	/**
	 * Generic builder which create a new ModelNodeFigure based on a node. It is possible to choose if show or not the data compartiment.
	 * 
	 * @param node DiagramNode to show
	 * @param showData Boolean selecting if show or not the data of node
	 */
	public ModelNodeFigure(DiagramNode node, boolean showData)
	{
		this(node, showData, new Color(null,0,255,210));
	}
	
	/**
	 * Specific builder with a node. It is the same that ModelNodeFigure(node, true)
	 * 
	 * @param node DiagramNode to show
	 */
	public ModelNodeFigure(DiagramNode node)
	{
		this(node, true);
	}
	
	
	
	//Getters
	public CompartimentFigure getAttributesCompartment() {
		return attributeFigure;
	}
	
	public DiagramNode getReferredNode()
	{
		return this.referredNode;
	}
}
