package rioko.drawmodels.filemanage;

@Deprecated
public interface MVDReader {	
	/**
	 * M�todo que permite obtener los nodos principales del fichero MVD
	 * @return Un array de MVDReaderNode
	 */
	public MVDReaderNode[] getNodes() throws Exception ;	
	
	/**
	 * M�todo que permite obtener los nodos a los que se llega desde un nodo dado.
	 * 
	 * @param tail Nodo desde el que se buscar� las conexiones.
	 * 
	 * @return Un array de MVDReaderNode
	 * @throws Exception 
	 */
	public MVDReaderNode[] getConnections(MVDReaderNode tail) throws Exception;
	
	/**
	 * M�todo que interpreta los datos de un nodo MVD.
	 * 
	 * @param node Nodo MVD que queremos interpretar.
	 * @return Object con los datos que tiene el nodo.
	 */
	public Object interpretMVDNode(MVDReaderNode node);
}
