package rioko.drawmodels.dialogs;

import java.util.ArrayList;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.swt.LabelValueDataLine;
import rioko.drawmodels.swt.composites.AddRemoveTable;
import rioko.drawmodels.swt.composites.ComboBox;
import rioko.drawmodels.swt.composites.LabelDataTable;
import rioko.drawmodels.swt.composites.RadioButtonLine;
import rioko.drawmodels.swt.composites.addremovetables.SearchAddRemoveListener;
import rioko.drawmodels.swt.composites.addremovetables.TableConfigurationListener;
import rioko.drawmodels.swt.composites.labeldatatables.ConfigurationTable;
import rioko.events.DataChangeEvent;
import rioko.events.listeners.AbstractDataChangeListener;
import rioko.graphabstraction.diagram.AbstractAttribute;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.filters.AndComposeFilter;
import rioko.graphabstraction.diagram.filters.ComposeFilter;
import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.graphabstraction.diagram.filters.OrComposeFilter;
import rioko.utilities.collections.ListenedArrayList;

public class SearchDialog extends TitleAreaDialog implements ValuableDialog{

	private ModelDiagram<?> model;
	private DiagramNode node;
	
	//Visualization fields
	private ComboBox eClassSelector;
	private AddRemoveTable searchCriteria;
	private RadioButtonLine andOrButtons;
	private ConfigurationTable configurationTable;
	private LabelDataTable nodeFiltered, nodeInfo;
	
	//Control fields
	private ListenedArrayList<FilterOfVertex> listOfCriteria = new ListenedArrayList<>(); 
//	private EClass eClassSelected = null;
	private ComposeFilter filter;
	
	//Builders
	public SearchDialog(Shell parent, ModelDiagram<?> model) {
		super(parent);
		
		this.model = model;
	}
	
	@Override
	public void create() {
		super.create();
		this.setTitle("Search a node in the model");
		this.setMessage("Here you can search some nodes on the model", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		//Creamos el layout: GridLayout de 1 columna
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 3;
		
		area.setLayout(layout);
		
		//Primera fila: Un ComboBox para seleccionar la EClass del nodo a buscar
		eClassSelector = new ComboBox(area, SWT.NONE, "EClass");
		eClassSelector.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		//Segunda fila: Un AddRemoveTable para añadir criterios de búsqueda y un LabelDataTable para configurar cada criterio
		Composite secondRow = new Composite(area, SWT.BORDER);
		secondRow.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout scnRowLayout = new GridLayout();
		scnRowLayout.numColumns = 2;
		scnRowLayout.horizontalSpacing = 2;
		secondRow.setLayout(scnRowLayout);
		
		Composite scnRowFstCol = new Composite(secondRow, SWT.NONE);
		scnRowFstCol.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout scnRowfstColLayout = new GridLayout();
		scnRowfstColLayout.numColumns = 1;
		scnRowFstCol.setLayout(scnRowfstColLayout);		
		
		searchCriteria = new AddRemoveTable(scnRowFstCol, SWT.NONE, "Search Criteria");
		GridData gData = new GridData(GridData.FILL_BOTH);
		gData.heightHint = 200;
		searchCriteria.setLayoutData(gData);
		
		try {
			andOrButtons = new RadioButtonLine(scnRowFstCol, SWT.NONE, 2, new String[] {"And", "Or"});
			andOrButtons.setLayoutData(new GridData());
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
		
		configurationTable = new ConfigurationTable(secondRow, SWT.NONE, this.model);
		configurationTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		//Tercera fila: Una Tabla para nodos y otra tabla para la información de cada nodo
		Composite thirdRow = new Composite(area, SWT.BORDER);
		thirdRow.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout trdRowLayout = new GridLayout();
		trdRowLayout.numColumns = 2;
		trdRowLayout.horizontalSpacing = 3;
		thirdRow.setLayout(trdRowLayout);
		
		nodeFiltered = new LabelDataTable(thirdRow, SWT.NONE, "Id", "EClass name");
		nodeFiltered.setLayoutData(gData);
		
		nodeInfo = new LabelDataTable(thirdRow, SWT.NONE, "Attribute", "Value");
		nodeInfo.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		this.createLogicPart();
		
		this.updateFilteredNodes();

		return area;
	}
	
	private void createLogicPart() {
		//Configuración de la primera fila
		eClassSelector.setInput(new String[0]/*model.getEClassListNames().toArray(new String[0])*/);
//		eClassSelector.addSelectionListener(new SelectionAdapter() {
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				for(EClass eClass : model.getEClassList()) {
//					if(eClass.getName().equals(eClassSelector.getText())) {
//						eClassSelected = eClass;
//						new DataChangeEvent(eClassSelector);
//						return;
//					}
//				}
//
//				if(eClassSelected != null) {
//					eClassSelected = null;
//					new DataChangeEvent(eClassSelector);
//				}
//			}
//		});
//		
		//Configuración de la segunda fila
		searchCriteria.setMouseListener(new SearchAddRemoveListener(searchCriteria, true, listOfCriteria), 
	    		new SearchAddRemoveListener(searchCriteria, false, listOfCriteria));
		searchCriteria.setTableListener(new TableConfigurationListener<FilterOfVertex>(searchCriteria, listOfCriteria, configurationTable));
		
		//Configuración de la tercera fila
		try {
			//Añadimos dos listeners para controlar los cambios de los filtros y de los atributos de los filtros para actualizar la lista de nodos filtrados
			new AbstractDataChangeListener(listOfCriteria, nodeFiltered) {
				@Override
				public void onDataChange(Event arg0) {
					updateFilteredNodes();
				}

				@Override
				protected void dispose() { }
				
			};
			
			new AbstractDataChangeListener(configurationTable, nodeFiltered) {
				@Override
				public void onDataChange(Event arg0) {
					updateFilteredNodes();
				}

				@Override
				protected void dispose() { }
				
			};
			
			new AbstractDataChangeListener(eClassSelector, nodeFiltered) {
				@Override
				public void onDataChange(Event arg0) {
					updateFilteredNodes();
				}

				@Override
				protected void dispose() { }
				
			};
			
			new AbstractDataChangeListener(andOrButtons, nodeFiltered) {
				@Override
				public void onDataChange(Event arg0) {
					updateFilteredNodes();
				}

				@Override
				protected void dispose() { }
				
			};
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
		
		//Añadimos el listener a la tabla de busqueda para que se actualice la tabla de información
		nodeFiltered.addTableListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) { /* Do nothing */ }

			@Override
			public void mouseDown(MouseEvent arg0) { /* Do nothing */ }

			@Override
			public void mouseUp(MouseEvent arg0) {
				TableItem[] items = nodeFiltered.getSelection();
				TableItem item = items[0];
				for(DiagramNode currentNode : model.getModelDiagram().vertexSet()) {
					if(Integer.parseInt(item.getText(0)) == currentNode.getId()) {
						node = currentNode;
						new DataChangeEvent(nodeFiltered);
					}
				}
			}
			
		});
		
		try {
			new AbstractDataChangeListener(nodeFiltered, nodeInfo) {
				@Override
				public void onDataChange(Event arg0) {
					updateNodeInfo();
				}

				@Override
				protected void dispose() { }
				
			};
		} catch (Exception e) {
			// Impossible exception
			e.printStackTrace();
		}
		
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Searching a node");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 600);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void okPressed() {
		super.okPressed();
	}
	
	//Other methods
	public DiagramNode searched() {
		return this.node;
	}
	
	private void updateFilteredNodes() {
		int andOr = andOrButtons.getSelection();
		ComposeFilter firstFilter;
		if(andOr == 0) { // And filter
			firstFilter = new AndComposeFilter();
		} else if(andOr == 1) { //Or filter
			firstFilter = new OrComposeFilter();
		} else { //Error: no search done
			return;
		}
		
		firstFilter.addAllFilters(listOfCriteria);
		
		filter = new AndComposeFilter();
//		ByEClass part = new ByEClass();
//		try {
//			part.setConfiguration(new EClassConfiguration(this.model, eClassSelected));
//		} catch (BadConfigurationException | BadArgumentException e) {
//			// Impossible exception
//			e.printStackTrace();
//		}
//		filter.addFilter(part);
		
		filter.addFilter(firstFilter);
		
		ArrayList<LabelValueDataLine> lines = new ArrayList<>();
		for(DiagramNode node : filter.filterVertex(null, model.getModelDiagram())) {
			lines.add(new LabelValueDataLine(""+node.getId(), node.getLabel()));
		}
		nodeFiltered.setInput(lines.toArray(new LabelValueDataLine[0]));
	}
	
	private void updateNodeInfo() {
		ArrayList<LabelValueDataLine> lines = new ArrayList<>();

		if(node instanceof DiagramNode) {
			for(AbstractAttribute attr : ((DiagramNode) node).getDrawableData()) {
				lines.add(new LabelValueDataLine(attr.getName(), attr.getValue().toString()));
			}
		}
		
		this.nodeInfo.setInput(lines.toArray(new LabelValueDataLine[0]));
	}

	@Override
	public DiagramNode getValue() {
		return this.searched();
	}
}
