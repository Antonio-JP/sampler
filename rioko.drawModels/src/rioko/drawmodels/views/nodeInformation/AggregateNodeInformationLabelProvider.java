package rioko.drawmodels.views.nodeInformation;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class AggregateNodeInformationLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof AggregateNodeInformationDataLine) {
			if(columnIndex == 0) {
				return ((AggregateNodeInformationDataLine)element).getLabel();
			} else if(columnIndex == 1) {
				return ((AggregateNodeInformationDataLine)element).getData();
			}
		}
		
		return null;
	}

}
