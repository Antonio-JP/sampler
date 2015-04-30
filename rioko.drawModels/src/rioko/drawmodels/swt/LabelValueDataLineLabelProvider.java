package rioko.drawmodels.swt;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class LabelValueDataLineLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof LabelValueDataLine) {
			if(columnIndex == 0) {
				return ((LabelValueDataLine)element).getLabel();
			} else if(columnIndex == 1) {
				return ((LabelValueDataLine)element).getData();
			}
		}
		
		return null;
	}

}
