package rioko.drawmodels.editors;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.ui.part.EditorPart;

public abstract class AbstractEditorPart extends EditorPart {

	protected Rectangle getEditorWindowSize() {
		Point size = this.getSite().getShell().getSize();
		Point position = this.getSite().getShell().getLocation();
		
		return new Rectangle(position.x, position.y, size.x, size.y);
	}

}
