package rioko.drawmodels.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import rioko.events.DataChangeEvent;

public class RadioButtonLine extends Composite {

	private Button[] buttons;
	
	private int previousSelected = -1;
	
	public RadioButtonLine(Composite parent, int style, int nButtons, String[] text) throws Exception {
		super(parent, style);
		
		//Comprobamos posibles errores
		if(text.length != nButtons) {
			throw new Exception("Rioko ERROR: bad number of buttons");
		}
		
		//Creamos el layout para el Composite
		GridLayout layout = new GridLayout();
		layout.numColumns = nButtons;
		layout.horizontalSpacing = 4;
		this.setLayout(layout);
		
		//Creamos los botones solicitados
		buttons = new Button[nButtons];
		
		RadioButtonLine me = this;
		
		for(int i = 0; i < nButtons; i++) {
			buttons[i] = new Button(this, SWT.RADIO);
			
			if(i == 0) {
				buttons[i].setSelection(true);
			}
			
			buttons[i].setText(text[i]);
			buttons[i].setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING | GridData.FILL_HORIZONTAL));
			
			buttons[i].addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent arg0) { /* Do nothing */ }

				@Override
				public void mouseDown(MouseEvent arg0) { /* Do nothing */ }

				@Override
				public void mouseUp(MouseEvent arg0) {
					if(previousSelected != getSelection()) {
						new DataChangeEvent(me);
					}
					
					previousSelected = getSelection();
				}
				
			});
		}
	}
	
	public int getSelection() {
		for(int i = 0; i < this.buttons.length; i++) {
			if(this.buttons[i].getSelection()) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void addMouseListener(MouseListener ml) {
		super.addMouseListener(ml);
		
		for(int i = 0; i < this.buttons.length; i++) {
			this.buttons[i].addMouseListener(ml);
		}
	}

}
