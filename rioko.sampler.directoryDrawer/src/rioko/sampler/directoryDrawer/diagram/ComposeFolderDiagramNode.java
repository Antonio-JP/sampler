package rioko.sampler.directoryDrawer.diagram;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;

import rioko.draw2d.figures.VerticalFigure;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.grapht.VertexFactory;
import rioko.sampler.directoryDrawer.diagram.factory.ComposeFolderDiagramNodeFactory;

public class ComposeFolderDiagramNode extends ComposeDiagramNode {
	
	private static final int maxRowsInDataFigure = 4;

	public ComposeFolderDiagramNode()
	{
		super();
	}
	
	public ComposeFolderDiagramNode(Collection<? extends DiagramNode> collection)
	{
		super(collection);
	}
	
	@Override
	public VertexFactory<?> getVertexFactory() {
		return new ComposeFolderDiagramNodeFactory();
	}

	//Drawing methods
	@Override
	public IFigure buildDataFigure() {
		if(this.inNodes.size() == 1) {
			return this.inNodes.get(0).getDataFigure();
		} else if(this.inNodes.size() != 0){
			//Inicializamos los datos a utilizar
			IFigure figure = new VerticalFigure();
			HashMap<Class<?>, Integer> amountOfTypes = this.getAmountOfTypes();	
			int nNodes = this.getFullListOfNodes().size();
			
			//Etiqueta principal
			Label contains = new Label("Contains " + nNodes + " resources:");
			Font boldFont = new Font(null, "Arial", 10, SWT.BOLD);
			contains.setFont(boldFont);
			figure.add(contains);
			
			//Etiquetas intermedias
			Iterator<Class<?>> iterator = amountOfTypes.keySet().iterator();
			for(int i = 0; i < amountOfTypes.keySet().size() && i < maxRowsInDataFigure; i++) 
			{
				Class<?> eClass = iterator.next();
				Label aux = new Label("   - "+amountOfTypes.get(eClass)+" " + eClass.getSimpleName());
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
	
	private HashMap<Class<?>, Integer> getAmountOfTypes() {
		HashMap<Class<?>, Integer> amountOfTypes = new HashMap<>();
		amountOfTypes.put(IProject.class, 0);
		amountOfTypes.put(IFolder.class, 0);
		amountOfTypes.put(IFile.class, 0);
		
		for(DiagramNode node : this.inNodes)
		{
			//Caso fácil: es un nodo simple
			if(node instanceof FolderDiagramNode) {
				FolderDiagramNode folderNode = (FolderDiagramNode)node;
				Class<?> classOfNode = getSuperClassOnSet(amountOfTypes.keySet(), folderNode.getResource().getClass());
				if(amountOfTypes.containsKey(classOfNode))
				{
					amountOfTypes.put(classOfNode, amountOfTypes.get(classOfNode)+1);
				} else {
					amountOfTypes.put(classOfNode, 1);
				}
			} else if(node instanceof ComposeFolderDiagramNode) {
				HashMap<Class<?>,Integer> nodeAmounts = ((ComposeFolderDiagramNode)node).getAmountOfTypes();
				
				for(Class<?> clazz : nodeAmounts.keySet()) {
					Class<?> clazzHere = getSuperClassOnSet(amountOfTypes.keySet(), clazz);
					if(amountOfTypes.containsKey(clazzHere))
					{
						amountOfTypes.put(clazzHere, amountOfTypes.get(clazzHere)+nodeAmounts.get(clazz));
					} else {
						amountOfTypes.put(clazzHere, nodeAmounts.get(clazz));
					}
				}
			}
		}
		
		return amountOfTypes;
	}
	
	private Class<?> getSuperClassOnSet(Set<Class<?>> set, Class<?> clazz) {
		Class<?> found = null;
		for(Class<?> aux : set) {
			if(aux.isAssignableFrom(clazz) && (found != null && aux.isAssignableFrom(found))) {
				found = aux;
			}
		}
		
		return (found == null) ? clazz : found;
	}

	@Override
	public AbstractAttribute[] getDrawableData() {
		return new AbstractAttribute[0];
	}

	@Override
	protected AbstractAttribute[] getNonDrawableData() {
		return new AbstractAttribute[0];
	}

}
