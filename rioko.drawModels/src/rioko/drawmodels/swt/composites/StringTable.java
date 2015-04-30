package rioko.drawmodels.swt.composites;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
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

import rioko.drawmodels.swt.StringTableRow;
import rioko.drawmodels.swt.StringTableRowLabelProvider;

public class StringTable extends Composite {
	
	private Table table;
	private TableViewer tableViewer;
	
	public StringTable(Composite parent, int style, String columName) {
		super(parent, style);
		
		//Creamos el layout para el Composite
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
	    this.setLayout(layout);

		TableLayout basicProperties = new TableLayout();
		basicProperties.addColumnData(new ColumnWeightData(1));
		
		this.table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL);
		this.table.setLinesVisible(true);
		this.table.setHeaderVisible(true);
		this.table.setLayout(basicProperties);
	    table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		this.tableViewer = new TableViewer(this.table);
		this.tableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		TableViewerColumn labelColumn = new TableViewerColumn(this.tableViewer, SWT.NONE);
		labelColumn.getColumn().setText(columName);
		this.tableViewer.setContentProvider(new ArrayContentProvider());
		this.tableViewer.setLabelProvider(new StringTableRowLabelProvider());
	}
	
	public void addTableListener(MouseListener ml) {
		this.table.addMouseListener(ml);
	}

	public void setInput(StringTableRow[] tableData) {
		this.tableViewer.setInput(tableData);
	}
	
	public String getSelection()
	{
		TableItem[] selection = this.table.getSelection();
		if(selection.length > 0) {
			return selection[0].getText(0);
		}
		
		return null;
	}
}
