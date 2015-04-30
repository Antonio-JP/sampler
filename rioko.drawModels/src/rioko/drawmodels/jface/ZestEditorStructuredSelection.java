package rioko.drawmodels.jface;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchPart;

import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.swt.LabelValueDataLine;

public class ZestEditorStructuredSelection extends StructuredSelection {
	public enum TypesOfSelection {FIGURE, DATA_LINE}
	
	private TypesOfSelection type;
	
	private IWorkbenchPart source;
	
	public ZestEditorStructuredSelection(ModelNodeFigure figure, IWorkbenchPart source)
	{
		super(figure);
		
		this.type = TypesOfSelection.FIGURE;
		
		this.source = source;
	}
	
	public ZestEditorStructuredSelection(LabelValueDataLine data, IWorkbenchPart source)
	{
		super(data);
		
		this.type = TypesOfSelection.DATA_LINE;
		
		this.source = source;
	}
	
	public TypesOfSelection getType()
	{
		return this.type;
	}
	
	public IWorkbenchPart getSource()
	{
		return this.source;
	}
}
