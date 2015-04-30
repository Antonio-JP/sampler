package rioko.drawmodels.filemanage.MVDReaderXML;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.core.resources.IFile;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import rioko.drawmodels.filemanage.MVDReader;
import rioko.drawmodels.filemanage.MVDReaderNode;

@Deprecated
public class MVDReaderXML implements MVDReader {
	
	//Static variables
	private static String ATTRIBUTES_NODE_NAME = "attributes";
	private static String ATR_NODE_NAME = "atr";
	private static String CONNECTIONS_NODE_NAME = "connections";
	private static String CONTONODE_NODE_NAME = "conToNode";
	private static String NODE_NODE_NAME = "node";
	
	//Class attributes
	private Document doc;
	
	private HashMap<String,MVDReaderNode> map = null;
	
	public MVDReaderXML(IFile input)
	{
		try {
			File fXmlFile = new File(input.getLocationURI().getPath());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			this.doc = dBuilder.parse(fXmlFile);
			
			doc.getDocumentElement().normalize();
		} catch (Exception e) {
			e.printStackTrace();
	    }		
	}

	@Override
	public MVDReaderNode[] getNodes() throws Exception {
		
		if(this.map == null) {
			
			this.map = new HashMap<>();
	
			NodeList nList = doc.getElementsByTagName(NODE_NODE_NAME);
	
			for (int count = 0; count < nList.getLength(); count++) {
				 
				Node tempNode = nList.item(count);
				
				String out = "";
				String id = "";
			 
				// make sure it's element node.
				if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
			 
					NodeList tempChildren = tempNode.getChildNodes();
					for (int tempCount = 0; tempCount < tempChildren.getLength(); tempCount++) {
						Node child = tempChildren.item(tempCount);
						
						if(child.getNodeName() == ATTRIBUTES_NODE_NAME) {
							out = "";
							
							NodeList nodeMap = child.getChildNodes();
							 
							boolean firstTime = true;
							for (int i = 0; i < nodeMap.getLength(); i++) {
				 
								Node node = nodeMap.item(i);
								
								if(node.getNodeName() == ATR_NODE_NAME){
									if(!firstTime) {
										out += "\n";
									}
									
									out += "+ "+this.getAttributeValue(node, "name");
									out += ": "+node.getTextContent();
									
									firstTime = false;
								}
							}
							
							break;
						}
					}
					
					id = this.getIdNode(tempNode);
					if(id=="") {
						throw new Exception("Rioko ERROR: Error en el fichero XML: Nodo sin id");
					}
					String name = this.getAttributeValue(tempNode, "name");
					
					MVDReaderNode mvdNode = new MVDReaderNode(id, name, out);
					this.map.put(id, mvdNode);
				}
			}
		}
			
		return this.castToArray(this.map.values());
	}

	@Override
	public MVDReaderNode[] getConnections(MVDReaderNode tail) throws Exception {
		NodeList nList = doc.getElementsByTagName(NODE_NODE_NAME);
		
		String id="";
		Node targetNode=null;
		
		ArrayList<MVDReaderNode> list = new ArrayList<>();

		//Buscamos el nodo con la id deseada
		for (int count = 0; count < nList.getLength(); count++) {
			 
			Node tempNode = nList.item(count);
			
			id = this.getIdNode(tempNode);
			if(id=="") {
				throw new Exception("Rioko ERROR: Error en el fichero XML: Nodo sin id");
			}
			if(id==tail.getId()) {
				targetNode = tempNode;
				break;
			}
		}
		
		if(targetNode == null) {
			return null;
		}
		
		//Ahora buscamos a las conexciones de ese nodo
		NodeList tempChildren = targetNode.getChildNodes();
		for (int tempCount = 0; tempCount < tempChildren.getLength(); tempCount++) {
			Node child = tempChildren.item(tempCount);
			
			if(child.getNodeName() == CONNECTIONS_NODE_NAME) {
				NodeList nodeMap = child.getChildNodes();
				 
				for (int i = 0; i < nodeMap.getLength(); i++) {
					
					Node actNode = nodeMap.item(i);
					
					//Comprobamos que está bien construido el nodo y es de tipo conToNode
					if(actNode.getNodeName() == CONTONODE_NODE_NAME)
					{
						list.add(this.map.get(this.getIdNode(actNode)));
					}
				}
				
				break;
			}
		}
		
		return this.castToArray(list);
	}

	@Override
	public Object interpretMVDNode(MVDReaderNode node) {
		return node.getData();
	}

	
	//Private methods
	private String getAttributeValue(Node node, String attrName) {
		return ((Element)node).getAttribute(attrName);
	}
	
	private String getIdNode(Node node) {
		return this.getAttributeValue(node, "id");
	}
	
	private MVDReaderNode[] castToArray(Collection<MVDReaderNode> list) {
		MVDReaderNode[] output = new MVDReaderNode[0];
		
		return list.toArray(output);
	}
}
