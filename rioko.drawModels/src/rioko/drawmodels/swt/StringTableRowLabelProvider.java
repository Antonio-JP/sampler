package rioko.drawmodels.swt;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class StringTableRowLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object arg0, int arg1) {
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof StringTableRow) {
			if(columnIndex == 0) {
				return ((StringTableRow)element).getData();
			}
		}
		
		return null;
	}

}
