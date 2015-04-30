package rioko.drawmodels.filemanage.MVDWriter;

import java.util.Random;

@Deprecated
public class FileAttribute {
	private String name;
	private String value;
	
	private static String DEF_ATTR = "attr";
	private static String DEF_VALUE = "attrValue";

	protected static String ATR_STRING = "atr";
	
	public FileAttribute(String name, String value)
	{
		this.name = name;
		this.value = value;
	}
	
	public FileAttribute(Random r) 
	{
		this(DEF_ATTR+r.nextInt(100), DEF_VALUE+r.nextInt(100));
	}
	
	@Override
	public String toString()
	{
		String output = "\t\t<"+ATR_STRING+" ";
		output += FileNode.NAME_STRING+"=\""+this.name+"\">";
		
		output += this.value;
		
		output += "</"+ATR_STRING+">\n";
		
		return output;		
	}
}
