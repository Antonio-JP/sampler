package rioko.drawmodels.swt.composites;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.LabelValueDataLine;
import rioko.drawmodels.swt.LabelValueDataLineLabelProvider;

public class LabelDataTable extends Composite {

	private Table table;
	private TableViewer tableViewer;
	private TableViewerColumn labelColumn, dataColumn;
	
	public LabelDataTable(Composite parent, int style) {
		super(parent, style);
		
		//Creamos el layout para el Composite
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
	    this.setLayout(layout);

		TableLayout basicProperties = new TableLayout();
		basicProperties.addColumnData(new ColumnWeightData(1));
		basicProperties.addColumnData(new ColumnWeightData(2));
		
		this.table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		this.table.setLinesVisible(true);
		this.table.setHeaderVisible(true);
		this.table.setLayout(basicProperties);
	    table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.tableViewer = new TableViewer(this.table);
		this.tableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		labelColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		labelColumn.getColumn().setText("Property");
		dataColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		dataColumn.getColumn().setText("Value");
		this.tableViewer.setContentProvider(new ArrayContentProvider());
		this.tableViewer.setLabelProvider(new LabelValueDataLineLabelProvider());
	}
	
	public LabelDataTable(Composite parent, int style, String label, String data) {
		this(parent, style);
		
		this.labelColumn.getColumn().setText(label);
		this.dataColumn.getColumn().setText(data);
	}

	public void setInput(LabelValueDataLine[] tableData) {
		this.tableViewer.setInput(tableData);
	}
	
	public void setEditingSupport(EditingSupport editingSupport) {
		this.dataColumn.setEditingSupport(editingSupport);
	}
	
	public void addTableListener(MouseListener listener) {
		this.table.addMouseListener(listener);
	}
	
	public TableItem[] getSelection() {
		return this.table.getSelection();
	}

	public ColumnViewer getValueViewer() {
		return this.dataColumn.getViewer();
	}
}
