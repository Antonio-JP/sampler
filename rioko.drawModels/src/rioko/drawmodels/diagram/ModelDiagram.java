package rioko.drawmodels.diagram;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

import rioko.drawmodels.diagram.XMIDiagram.XMIDiagramNode;
import rioko.drawmodels.filemanage.XMIReader;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;



/**
 * Class with the structure and methods to use a Diagram of a Model
 * 
 * @author Antonio
 *
 */
public class ModelDiagram implements IEditorInput{
	private DiagramGraph graph;
	
	private DiagramGraph printable;
	
	private String name = "ZestEditor";
	
	//Builders
	public ModelDiagram()
	{
		this.graph = new DiagramGraph();
		
		this.printable = null;
	}
	
	public ModelDiagram(Class<? extends DiagramEdge<DiagramNode>> edgeClass, Class<? extends DiagramNode> vertexClass, Class<? extends ComposeDiagramNode> composeClass)
	{
		this.graph = new DiagramGraph(edgeClass, vertexClass, composeClass);
		
		this.printable = this.graph;
	}
	
	public ModelDiagram(XMIReader xmiReader) throws IOException
	{
		this.graph = xmiReader.getDiagram();
		
		this.printable = this.graph;
	}
	
	public ModelDiagram(DiagramGraph graph) 
	{
		this.graph = graph;
		
		this.printable = this.graph;
	}
	
	public ModelDiagram(DiagramGraph graph, String name) 
	{
		this(graph);
		
		this.name = name;		
	}
		
	//Getters & Setters
	public DiagramGraph getModelDiagram()
	{
		return this.graph;
	}
	
	public DiagramGraph getPrintDiagram()
	{
		return this.printable;
	}
	
	public void setPrintDiagram(DiagramGraph printable)
	{
		this.printable = printable;
	}
	
	//Meta-model analysis methods
	private Set<EClass> eClassList = null;
	private Set<String> eClassNames = null;
	public Set<EClass> getEClassList() {
		if(this.eClassList == null) {
			this.eClassList = new HashSet<EClass>();
			
			for(DiagramNode node : this.graph.vertexSet()) {
				if(node instanceof XMIDiagramNode) {
					this.eClassList.add(((XMIDiagramNode) node).getEClass());
				}
			}
		}
		
		return this.eClassList;
	}
	
	public Set<String> getEClassListNames() {
		if(this.eClassNames == null) {
			if(this.eClassList == null) {
				this.getEClassList();
			}
			
			this.eClassNames = new HashSet<String>();
			
			for(EClass eClass : this.eClassList) {
				this.eClassNames.add(eClass.getName());
			}
		}
		
		return this.eClassNames;
	}

	//IEditorInput methods
	//Todos están vacíos porque no quiero nada de ellos
	@SuppressWarnings("unchecked")
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class arg0) { return null; }

	@Override
	public boolean exists() { return this.graph!=null; }

	@Override
	public ImageDescriptor getImageDescriptor() { return null; }

	@Override
	public String getName() { return this.name;}

	@Override
	public IPersistableElement getPersistable() { return null; }

	@Override
	public String getToolTipText() { return "Rioko: model custom explorer"; }

	
}
