package rioko.drawmodels.diagram.XMIDiagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import rioko.draw2d.figures.VerticalFigure;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;

public class ComposeXMIDiagramNode extends ComposeDiagramNode {
	
	private static final int maxRowsInDataFigure = 4;
	
	public ComposeXMIDiagramNode()
	{
		super();
	}
	
	public ComposeXMIDiagramNode(Collection<? extends DiagramNode> collection)
	{
		super(collection);
	}
	
	@Override
	public VertexFactory<?> getVertexFactory() {
		return new ComposeXMIDiagramNodeFactory();
	}
	
	@Override
	public void addDiagramNode(DiagramNode node)
	{
		if((node instanceof XMIDiagramNode) || (node instanceof ComposeXMIDiagramNode)) {
			this.inNodes.add(node);
		}
	}

	@Override
	public IFigure buildDataFigure() {
		if(this.inNodes.size() == 1) {
			return this.inNodes.get(0).getDataFigure();
		} else if(this.inNodes.size() != 0){
			//Inicializamos los datos a utilizar
			IFigure figure = new VerticalFigure();
			HashMap<EClass, Integer> amountOfTypes = this.getAmountOfTypes();	
			int nNodes = this.getFullListOfNodes().size();
			
			//Etiqueta principal
			Label contains = new Label("Contains " + nNodes + " nodes:");
			Font boldFont = new Font(null, "Arial", 10, SWT.BOLD);
			contains.setFont(boldFont);
			figure.add(contains);
			
			//Etiquetas intermedias
			Iterator<EClass> iterator = amountOfTypes.keySet().iterator();
			for(int i = 0; i < amountOfTypes.keySet().size() && i < maxRowsInDataFigure; i++) 
			{
				EClass eClass = iterator.next();
				Label aux = new Label("   - "+amountOfTypes.get(eClass)+" " + eClass.getName());
				aux.setLabelAlignment(PositionConstants.LEFT);
				figure.add(aux);
			}
			
			//Los que sobran los sumamos para indicar cuántos quedan
			int otherNodes = 0;
			while(iterator.hasNext()) {
				otherNodes += amountOfTypes.get(iterator.next());
			}
			
			//Si hay de estos ndoos, añadimos otra linea
			if(otherNodes != 0) {
				Label others = new Label("* And " + otherNodes + " other nodes");
				boldFont = new Font(null, "Arial", 9, SWT.BOLD);
				others.setFont(boldFont);
				others.setLabelAlignment(PositionConstants.LEFT);
				
				figure.add(others);				
			}
			
			return figure;
		}
		
		return null;
	}
	
	private HashMap<EClass, Integer> getAmountOfTypes() {
		HashMap<EClass, Integer> amountOfTypes = new HashMap<>();
		
		for(DiagramNode node : this.inNodes)
		{
			//Caso fácil: es un nodo simple
			if(node instanceof XMIDiagramNode) {
				XMIDiagramNode xmiNode = (XMIDiagramNode)node;
				if(amountOfTypes.containsKey(xmiNode.getEClass()))
				{
					amountOfTypes.put(xmiNode.getEClass(), amountOfTypes.get(xmiNode.getEClass())+1);
				} else {
					amountOfTypes.put(xmiNode.getEClass(), 1);
				}
			} else if(node instanceof ComposeXMIDiagramNode) {
				HashMap<EClass,Integer> nodeAmounts = ((ComposeXMIDiagramNode)node).getAmountOfTypes();
				
				for(EClass eClass : nodeAmounts.keySet()) {
					if(amountOfTypes.containsKey(eClass))
					{
						amountOfTypes.put(eClass, amountOfTypes.get(eClass)+nodeAmounts.get(eClass));
					} else {
						amountOfTypes.put(eClass, nodeAmounts.get(eClass));
					}
				}
			}
		}
		
		return amountOfTypes;
	}
}
