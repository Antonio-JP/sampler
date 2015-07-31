package rioko.drawmodels.swt.composites;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.LabelValueDataLine;
import rioko.drawmodels.swt.LabelValueDataLineLabelProvider;

public class AddRemoveTable extends Composite {
	
	private Table table;
	private TableViewer tableViewer;
	
	private Button addButton, removeButton;
	
	private MouseListener addListener = null, removeListener = null, tableListener = null;
	

	public AddRemoveTable(Composite parent, int style, String columnName) {
		super(parent, style);

		//Creamos el Layout para este Composite
		GridLayout layout = new GridLayout();
		layout.horizontalSpacing = 5;
		layout.numColumns = 2;
	    this.setLayout(layout);
	    
	    //Creamos la tabla del Composite y lo ponemos en la primera columna
	    TableLayout tablelayout = new TableLayout();
	    tablelayout.addColumnData(new ColumnWeightData(1));
	    tablelayout.addColumnData(new ColumnWeightData(5));
	    table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.V_SCROLL); 
	    table.setLinesVisible(true);
	    table.setHeaderVisible(true);
	    table.setLayout(tablelayout);
	    table.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
	    
	    tableViewer = new TableViewer(table);
	    tableViewer.getControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	    
	    TableViewerColumn filterViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
	    filterViewerColumn.getColumn().setText("#");
	    filterViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
	    filterViewerColumn.getColumn().setText(columnName);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setLabelProvider(new LabelValueDataLineLabelProvider());
		
		//Creamos los botones y los colocamos en columan en la segunda columna
		Composite buttonColumn = new Composite(this, SWT.NONE);
		buttonColumn.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		
		GridLayout butColLayout = new GridLayout();
		butColLayout.numColumns = 1;
		buttonColumn.setLayout(butColLayout);
		
		addButton = new Button(buttonColumn, SWT.PUSH);
		addButton.setText("Add");
		addButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
		
		removeButton = new Button(buttonColumn, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
	}

	public void setMouseListener(MouseListener addListener, MouseListener removeListener) {
		if(addListener != null && removeListener != null) {
			if(this.addListener != null) {
				this.addButton.removeMouseListener(this.addListener);
			}
			if(this.removeListener != null) {
				this.removeButton.removeMouseListener(this.removeListener);
			}
			this.addListener = addListener;
			this.removeListener = removeListener;
			
			this.addButton.addMouseListener(this.addListener);
			this.removeButton.addMouseListener(this.removeListener);
		}
	}
	
	public void removeMouseListener(MouseListener addListener, MouseListener removeListener) {
		this.addButton.removeMouseListener(addListener);
		this.removeButton.removeMouseListener(removeListener);
	}
	
	public void setTableListener(MouseListener mouseListener) {
		if(mouseListener != null) {
			if(this.tableListener != null) {
				this.table.removeMouseListener(this.tableListener);
			}
			
			this.tableListener = mouseListener;
			
			this.table.addMouseListener(this.tableListener);
		}
	}

	public void setInput(List<?> dataList) {
		ArrayList<LabelValueDataLine> list = new ArrayList<>();
		
		int i = 1;
		for(Object obj : dataList) {
			list.add(new LabelValueDataLine(String.valueOf(i), this.getNameForObject(obj)));
			i++;
		}
		
		this.tableViewer.setInput(list);
	}

	public TableItem[] getSelection() {
		return this.table.getSelection();
	}
	

	//Special methods
	protected String getNameForObject(Object obj) {
		return obj.getClass().getSimpleName();
	}
}
