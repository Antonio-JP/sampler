package rioko.drawmodels.filemanage.MVDWriter;

import java.util.Random;

@Deprecated
public class FileConnection {
	private int idTarget;
	
	private static String CON_STRING = "conToNode";
	
	public FileConnection(int idTarget)
	{
		this.idTarget = idTarget;
	}
	
	public FileConnection(Random r, int maxId) 
	{
		this(r.nextInt(maxId));
	}
	
	@Override
	public String toString()
	{
		String output = "\t\t<"+CON_STRING+" ";
		output += FileNode.ID_STRING+"=\""+this.idTarget+"\"";		
		output += "/>\n";
		
		return output;		
	}
}
