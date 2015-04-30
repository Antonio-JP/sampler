package rioko.drawmodels.filemanage.MVDWriter;

import java.io.PrintWriter;
import java.util.Random;

@Deprecated
public class MVDWriter {	
	private static int N_NODES = 10000;
	
	private static String ROOT_STRING = "root";
	
	public static void main(String[] args) {
		try{
		for(int nNodes=1000; nNodes<= N_NODES; nNodes +=1000) {
			PrintWriter writer = new PrintWriter("random"+nNodes+".mvd", "UTF-8");
			
			System.out.println("Fichero Abierto");
			
			Random r = new Random();
			FileNode node = null;
			
			writer.println("<"+ROOT_STRING+">");
			
			for(int i = 0; i < nNodes; i++) {
				node = new FileNode(r, nNodes);
				
				node.writeNode(writer);
			}
	
			writer.println("</"+ROOT_STRING+">");
			
			writer.close();
			FileNode.clearClass();
			System.out.println("Finalización exitosa! ("+nNodes+")");
		}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
