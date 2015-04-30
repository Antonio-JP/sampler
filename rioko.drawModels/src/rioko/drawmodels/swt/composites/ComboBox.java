package rioko.drawmodels.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class ComboBox extends Composite {

	Combo comboBox;
	
	public ComboBox(Composite parent, int style, String label) {
		super(parent, style);
		
		//Creamos el layout para el ComboBox: GridLayout de 2 columnas
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 2;
		
		this.setLayout(layout);
		
		//Creamos la etiqueta
		Label comboLabel = new Label(this, SWT.RIGHT);
		comboLabel.setText(label);
		comboLabel.setFont(new Font(null, "Arial", 12, SWT.BOLD));
		
		comboLabel.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING));
		
		//Creamos el ComboBox
		comboBox = new Combo(this, SWT.NONE);
		comboBox.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.HORIZONTAL_ALIGN_BEGINNING));
	}

	public void setInput(String[] input) {
		String[] list = new String[input.length+1];
		
		
		list[0] = "";
		for(int i = 0; i < input.length; i++) {
			list[i+1] = input[i];
		}
		this.comboBox.setItems(list);
	}
	
	public void addSelectionListener(SelectionListener listener) {
		this.comboBox.addSelectionListener(listener);
	}
	
	public String getText() {
		return this.comboBox.getText();
	}
}
