package rioko.drawmodels.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class CheckButton extends Composite {
	
	private Button button;
	private boolean checked = false;

	public CheckButton(Composite parent, String text) {
		super(parent, SWT.NONE);
		
		this.setLayout(new FillLayout());
		
		this.button = new Button(this, SWT.CHECK);
		this.button.setText(text);
		
		this.button.addMouseListener(new MouseListener() {

			@Override
			public void mouseDoubleClick(MouseEvent arg0) { /* Do nothing */ }


			@Override
			public void mouseDown(MouseEvent arg0) { /* Do nothing */ }

			@Override
			public void mouseUp(MouseEvent arg0) {
				checked = (!checked);
			}
			
		});
	}
	
	public boolean isChecked() {
		return this.checked;
	}
	
	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		
		this.button.setEnabled(enabled);
	}
}
