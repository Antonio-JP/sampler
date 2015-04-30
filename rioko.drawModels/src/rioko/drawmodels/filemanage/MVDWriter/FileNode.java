package rioko.drawmodels.filemanage.MVDWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;

@Deprecated
public class FileNode {
	private int id;
	private String name;
	
	private FileAttribute[] attr;
	private FileConnection[] con;

	private static String NODE_STRING = "node";
	static String ID_STRING = "id";
	private static String CONNECTION_STRING = "connections";
	private static String ATTRIBUTE_STRING = "attributes";
	static String NAME_STRING = "name";
	
	private static String DEF_NAME = "name";
	
	private static int REF_ID = 0;
	
	public FileNode()
	{
		id = REF_ID;
		REF_ID++;
	}
	
	public FileNode(Random r, int maxId)
	{
		this();
		
		this.name = DEF_NAME+r.nextInt(100);
		
		int nAttributes = r.nextInt(10);
		int nConnections = r.nextInt(Integer.min(20, maxId/2));
		
		ArrayList<FileAttribute> attrs = new ArrayList<>(); 
		for(int i = 0; i < nAttributes; i++)
		{
			attrs.add(new FileAttribute(r));
		}
		this.attr = attrs.toArray(new FileAttribute[0]);
		
		ArrayList<FileConnection> cons = new ArrayList<>(); 
		for(int i = 0; i < nConnections; i++)
		{
			cons.add(new FileConnection(r, maxId));
		}
		this.con = cons.toArray(new FileConnection[0]);
	}

	public void writeNode(PrintWriter writer) {
		writer.print(this.toString());
	}
	
	@Override
	public String toString()
	{
		String output = "<"+NODE_STRING+" ";
		
		//Parte inicial
		output+=ID_STRING+"=\""+this.id+"\" ";
		output+=NAME_STRING+"=\""+this.name+"\">\n";
		
		//Parte de atributos
		output+="\t<"+ATTRIBUTE_STRING+">\n";
		for(FileAttribute attr : this.attr) {
			output+=attr.toString();
		}
		output+="\t</"+ATTRIBUTE_STRING+">\n";
		
		//PArte de conexiones
		output+="\t<"+CONNECTION_STRING+">\n";
		for(FileConnection con : this.con) {
			output+=con.toString();
		}
		output+="\t</"+CONNECTION_STRING+">\n";
		
		//Final
		output+="\t</"+NODE_STRING+">\n";
		
		return output;
	}

	public static void clearClass() {
		REF_ID=0;
	}
}
