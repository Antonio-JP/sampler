package rioko.drawmodels.dialogs;

import java.util.ArrayList;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Point;import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;

import rioko.drawmodels.swt.LabelValueDataLine;
import rioko.drawmodels.swt.composites.LabelDataTable;
import rioko.eclipse.registry.RegistryManagement;

public class SelectReaderDialog extends TitleAreaDialog implements ValuableDialog {
	
	private static final String ID_DIAGRAM_EXTENSION = "rioko.drawmodels.diagrams";

	private IResource file = null;
	
	private String readerClassName = "";
	
	private ArrayList<IConfigurationElement> validExtensions;
	
	//UI Fields
	private LabelDataTable readersTable;
	
	//Logic Fields
	
	public SelectReaderDialog(Shell shell, IResource file, Boolean allReaders) {
		super(shell);
		
		this.file = file;
		
		//Get the valid extensions for this file using allReaders parameter
		IConfigurationElement[] elements = RegistryManagement.getElementsFor(ID_DIAGRAM_EXTENSION);
		validExtensions = new ArrayList<>();
		
		//We search the correct readers for the file extension
		for(IConfigurationElement element : elements) {
			if(allReaders) {
				validExtensions.add(element);
			} else {
				for(IConfigurationElement fileExtension : element.getChildren("fileExtension")) {
					if(fileExtension.getAttribute("fileExtension").equals(file.getFileExtension())) {
						//We have found a correct reader for this file
						validExtensions.add(element);
						break;
					}
				}
			}
		}
	}
	
	//TitleAreaDialog methods
	@Override
	public void create() {
		super.create();
		this.setTitle("Select a Reader for this file");
		this.setMessage("Here you can select a Reader for the file " + file.getName() + "\n(File extension: " + this.file.getFileExtension() + ")", 
				IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);

		//Creamos el layout de la ventana de Diálogo
		this.createUIPart(area);
		
		//Creamos la lógica de la interfaz de la ventana de Diálogo
		this.createLogicPart();

		return area;
	}

	private void createUIPart(Composite area) {
		//Simple Grid Layout with 1 column
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		layout.verticalSpacing = 3;
		
		area.setLayout(layout);
		
		//Title Label announcing the table
		Label title = new Label(area, SWT.NONE);
		title.setText("Plug-in with Diagram Extensions and its Readers:");
		title.setFont(new Font(title.getDisplay(), new FontData("Arial", 16, SWT.BOLD)));
		title.setLayoutData(new GridData(GridData.BEGINNING));
		
		//Table with all the data
		readersTable = new LabelDataTable(area, SWT.BORDER_DASH, "Extension Name", "Reader Class Name");
		readersTable.setLayoutData(new GridData(GridData.FILL_BOTH));
		readersTable.setInput(this.getLinesFromReadersRegistryElements());
	}

	private void createLogicPart() {
		readersTable.addTableListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) {
				this.mouseUp(arg0);
				okPressed();
			}

			@Override
			public void mouseDown(MouseEvent arg0) { /* Do nothing */ }

			@Override
			public void mouseUp(MouseEvent arg0) {
				TableItem[] items = readersTable.getSelection();
				TableItem item = items[0];

				readerClassName = item.getText(1);
			}
			
		});
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Selecting avaible Reader");
	}

	@Override
	protected Point getInitialSize() {
		return new Point(550, 375);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public String getValue() {
		return this.readerClassName;
	}
	

	//Aditional Methods
	private LabelValueDataLine[] getLinesFromReadersRegistryElements() {
		ArrayList<LabelValueDataLine> lines = new ArrayList<>(this.validExtensions.size());
		
		for(IConfigurationElement element : this.validExtensions) {
			//Create the line using the Name of the Diagram Extension and the Reader class name
			
			//Creating the String with the name of the extension
			String extensionName = RegistryManagement.getAttribute(element, "name");
			if(extensionName == null) {
				extensionName = RegistryManagement.getAttribute(element, "id");
				if(extensionName == null) {
					extensionName = element.getNamespaceIdentifier();
				}
			}
			//Create the name of the Reader class
			String readerName = RegistryManagement.getAttribute(element, "reader");
			
			//Creating the line if the reader element exists
			if(readerName != null) {
				lines.add(new LabelValueDataLine(extensionName, readerName));
			}
		}
		
		return lines.toArray(new LabelValueDataLine[0]);
	}

}
