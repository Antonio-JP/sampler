package rioko.drawmodels.editors.zesteditor;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Color;
import org.eclipse.zest.core.viewers.EntityConnectionData;
import org.eclipse.zest.core.viewers.IEntityConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IFigureProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramEdge.typeOfConnection;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.drawmodels.diagram.ModelDiagram;

public class ZestEditorLabelProvider extends LabelProvider implements IFigureProvider, IEntityConnectionStyleProvider {
	
	private ModelDiagram<?> model = null;
	
	private boolean showingData = true;

	private boolean showingConnectionLabels = false;
	
	public ZestEditorLabelProvider(ModelDiagram<?> model) {
		this.model = model;
	}
	
	//Funcion de texto
	@Override
	public String getText(Object element) {
	    if (element instanceof DiagramNode) {
	      if(this.showingData) {
	    	DiagramNode myNode = (DiagramNode) element;
	    	return myNode.getTitle();
	      }
	      else return "";
	    }
	    // Not called with the IGraphEntityContentProvider
	    if (element instanceof DiagramEdge) {
	    	return "";
	    }

	    if (element instanceof EntityConnectionData) {
	      if(this.showingConnectionLabels) {
	    	  return this.model.getPrintDiagram().getEdge((EntityConnectionData)element).getText();
	      } else {
	    	  return "";
	      }
	    }
	    
	    throw new RuntimeException("Wrong type: "+ element.getClass().toString());
	  }

	//Funcion de imágenes de nodos
	@Override
	public IFigure getFigure(Object element) {
		IFigure result = null;
		
		if (element instanceof DiagramNode) {
			DiagramNode node = (DiagramNode)element;
			result = node.getFigure(this.showingData);
		}
		// Not called with the IGraphEntityContentProvider
//		else if (element instanceof GraphConnection) {
//			result = null;
//		}

		else if (element instanceof EntityConnectionData) {
			return null;
		}
		    
		else {
			throw new RuntimeException("Wrong type: "+ element.getClass().toString());
		}
		
		if(result != null) {
			result.setSize(-1, -1);
		}
		
		return result;
	}
	
	//Funciones de imágenes de aristas
	@Override
	public Color getColor(Object source, Object target) {
		return new Color(null, 0,0,0);
	}

	@Override
	public int getConnectionStyle(Object source, Object target) {
		DiagramEdge<DiagramNode> edge = this.model.getPrintDiagram().getEdge((DiagramNode)source, (DiagramNode)target);
		
		if(edge==null) {
			return 0;
		} else if (edge.getType() == null) {
			return ZestStyles.CONNECTIONS_SOLID;
		} else if (edge.getType().equals(typeOfConnection.REFERENCE)) {
			return ZestStyles.CONNECTIONS_DOT | ZestStyles.CONNECTIONS_DIRECTED;
		} else if (edge.getType().equals(typeOfConnection.CONTAINMENT)) {
			return ZestStyles.CONNECTIONS_DASH | ZestStyles.CONNECTIONS_DIRECTED;
		}
		
		return ZestStyles.CONNECTIONS_SOLID | ZestStyles.CONNECTIONS_DIRECTED;
	}

	@Override
	public Color getHighlightColor(Object source, Object target) {
		return new Color(null,28,4,121);
	}

	@Override
	public int getLineWidth(Object source, Object target) {
		return 2;
	}

	@Override
	public IFigure getTooltip(Object source) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setShowingData(boolean showingData) {
		this.showingData = showingData;
	}
	
	public void setShowingConnectionLabel(boolean show) {
		this.showingConnectionLabels  = show;
	}
}
