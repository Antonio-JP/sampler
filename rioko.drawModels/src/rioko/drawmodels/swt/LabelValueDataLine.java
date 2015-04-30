package rioko.drawmodels.swt;

public class LabelValueDataLine extends StringTableRow{
	private String label;
	
	public LabelValueDataLine(String label, String data)
	{
		super(data);
		this.label = label;
	}

	public String getLabel() {
		return label;
	}
}
