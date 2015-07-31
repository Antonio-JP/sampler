package rioko.drawmodels.views.nodeInformation;

import java.util.ArrayList;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbenchPart;

import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.draw2d.ModelNodeFigure;
import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.drawmodels.jface.ZestEditorStructuredSelection;
import rioko.drawmodels.jface.ZestEditorStructuredSelection.TypesOfSelection;
import rioko.drawmodels.views.ZestEditorDependingViewPart;
import rioko.drawmodels.views.listeners.NodeInformationViewClickSelectionMouseListener;
import rioko.drawmodels.views.listeners.NodeInformationViewNavigationMouseListener;


public class NodeInformationView extends ZestEditorDependingViewPart implements ISelectionChangedListener{

	private Table simpleTable, aggregateTable;
	private TableViewer simpleTableViewer, aggregateTableViewer;
	private TableViewerColumn valueColumn;
	
	private DiagramNode actualNode=null;
	
	private NodeInformationViewNavigationMouseListener navListener = null;
	private NodeInformationViewClickSelectionMouseListener clickListener= null;
	


	@Override
	protected void createUIPart(Composite parent) {
		//Creamos las tablas
		
		//Tabla para nodos simples
		TableLayout tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
		
		this.simpleTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		this.simpleTable.setLinesVisible(true);
		this.simpleTable.setHeaderVisible(true);
		this.simpleTable.setLayout(tableLayout);
		
		this.simpleTableViewer = new TableViewer(this.simpleTable);
		this.simpleTableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableViewerColumn labelColumn = new TableViewerColumn(this.simpleTableViewer, SWT.NONE);
		labelColumn.getColumn().setText("Attribute");
		valueColumn = new TableViewerColumn(this.simpleTableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		this.simpleTableViewer.setContentProvider(new ArrayContentProvider());
		this.simpleTableViewer.setLabelProvider(new SimpleNodeInformationLabelProvider());
			
		//Tablas para nodos compuestos
		tableLayout = new TableLayout();
		tableLayout.addColumnData(new ColumnWeightData(1));
		tableLayout.addColumnData(new ColumnWeightData(1));
				
		this.aggregateTable = new Table(parent, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		this.aggregateTable.setLinesVisible(true);
		this.aggregateTable.setHeaderVisible(true);
		this.aggregateTable.setLayout(tableLayout);
				
		this.aggregateTableViewer = new TableViewer(this.aggregateTable);
		this.aggregateTableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		labelColumn = new TableViewerColumn(this.aggregateTableViewer, SWT.NONE);
		labelColumn.getColumn().setText("Atribute");
		valueColumn = new TableViewerColumn(this.aggregateTableViewer, SWT.NONE);
		valueColumn.getColumn().setText("Value");
		this.aggregateTableViewer.setContentProvider(new ArrayContentProvider());
		this.aggregateTableViewer.setLabelProvider(new AggregateNodeInformationLabelProvider());
	}
	
	@Override
	protected void createLogicPart(Composite parent) {
		// Do nothing
	}

	//ZestEditorDependingViewPart methods
	@Override
	protected void doBeforeChange(IWorkbenchPart part) {
		if(this.getCurrentEditor() != null) {
			this.getCurrentEditor().removeSelectionChangedListener(this);
		}
	}
	
	@Override
	protected void doWhenActivate(IWorkbenchPart part) {
		//Cambiamos los wizardListener del ratón al nuevo editor
		if(this.clickListener != null) {
			this.aggregateTableViewer.getControl().removeMouseListener(this.clickListener);
		}
		
		if(this.navListener != null) {
			this.aggregateTableViewer.getControl().removeMouseListener(this.navListener);
		}
		
		this.clickListener = new NodeInformationViewClickSelectionMouseListener(this.getZestEditor(), this);
		this.aggregateTableViewer.getControl().addMouseListener(this.clickListener);
		this.navListener = new  NodeInformationViewNavigationMouseListener(this.getZestEditor(), this);
		this.aggregateTableViewer.getControl().addMouseListener(this.navListener);
		
		//Añadimos la vista como wizardListener de selecciones del editor
		this.getCurrentEditor().addSelectionChangedListener(this);
		
		//Quitamos las tablas que tenemos mostrando
		if(this.aggregateTable != null) {
			this.aggregateTable.setVisible(false);
		}
		if(this.simpleTable != null) {
			this.simpleTable.setVisible(false);
		}
	}

	@Override
	public void updateView() { }
	
	//Other public methods
	public AggregateNodeInformationDataLine getLineSelected() {
		TableItem[] itemSelected = this.aggregateTable.getSelection();
		
		if(itemSelected.length > 0) {
			TableItem item = itemSelected[0];
			
			return new AggregateNodeInformationDataLine(item.getText(0), item.getText(1));
		}
		
		return null;
	}
	
	//ISelectionListener methods
	@Override
	public void selectionChanged(SelectionChangedEvent se) {
		if(se.getSelection() instanceof ZestEditorStructuredSelection) {
			ZestEditorStructuredSelection strSel = (ZestEditorStructuredSelection)se.getSelection();
			//Si la selección se ha hecho fuera de un editor de Zest, ocultamos la información
			if((strSel.getSource() instanceof ZestEditor) && !strSel.isEmpty()) {
				if(strSel.getType().equals(TypesOfSelection.FIGURE)) {
					actualNode = ((ModelNodeFigure)strSel.getFirstElement()).getReferredNode();
					if(actualNode instanceof ComposeDiagramNode && ((ComposeDiagramNode)actualNode).isCompose()) {
						ComposeDiagramNode compose = (ComposeDiagramNode)actualNode; 
						
						this.aggregateTable.setVisible(true);
						this.simpleTable.setVisible(true);
						
						this.aggregateTableViewer.setInput(NodeInformationView.getAggregateInput(compose));
						this.simpleTableViewer.setInput(NodeInformationView.getSimpleInput(compose.getRootNode()));
					} else {
						this.aggregateTable.setVisible(false);
						this.simpleTable.setVisible(true);
						
						this.simpleTableViewer.setInput(NodeInformationView.getSimpleInput(actualNode));
					}
				}
			} else if(strSel.getSource() instanceof NodeInformationView && !strSel.isEmpty()) {
				if(strSel.getType().equals(TypesOfSelection.DATA_LINE)) {
					this.simpleTableViewer.setInput(NodeInformationView.getSimpleInput(
						this.getSelectedNode(((AggregateNodeInformationDataLine)strSel.getFirstElement()).getLabel())));
				}
			} else {		
				this.aggregateTable.setVisible(false);
				this.simpleTable.setVisible(false);
			}
		}
	}

	//Private methods
	public DiagramNode getSelectedNode(String label) {
		if(this.actualNode instanceof ComposeDiagramNode) {
			for(DiagramNode node : ((ComposeDiagramNode)this.actualNode).getFullListOfNodes()) {
				if(String.valueOf(node.getId()).equals(label)) {
					return node;
				}
			}
		}
		
		return null;
	}
	
	private static SimpleNodeInformationDataLine[] getSimpleInput(DiagramNode node) {
		ArrayList<SimpleNodeInformationDataLine> lines = new ArrayList<>();
		
		for(AbstractAttribute attr : node.getData()) {
			lines.add(new SimpleNodeInformationDataLine(attr.getName(),attr.getValue().toString()));
		}
				
		return lines.toArray(new SimpleNodeInformationDataLine[0]);
	}

	private static AggregateNodeInformationDataLine[] getAggregateInput(ComposeDiagramNode compose) {
		ArrayList<AggregateNodeInformationDataLine> lines = new ArrayList<>();
		for(DiagramNode node : compose.getFullListOfNodes()) {
			lines.add(new AggregateNodeInformationDataLine(""+node.getId(), node.getLabel()));
		}
		
		return lines.toArray(new AggregateNodeInformationDataLine[0]);
	}
}
