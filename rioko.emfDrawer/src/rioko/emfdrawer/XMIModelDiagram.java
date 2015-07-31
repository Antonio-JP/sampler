package rioko.emfdrawer;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.emfdrawer.xmiDiagram.ComposeXMIDiagramNode;
import rioko.emfdrawer.xmiDiagram.XMIDiagramNode;
import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramEdge;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Pair;

public class XMIModelDiagram extends ModelDiagram<EObject> {

	private Set<EClass> eClassList = null;
	private Set<String> eClassNames = null;
	
	//Builders
	public XMIModelDiagram()
	{
		super();
	}
	
	public XMIModelDiagram(Class<? extends DiagramEdge<DiagramNode>> edgeClass, Class<? extends DiagramNode> vertexClass, Class<? extends ComposeDiagramNode> composeClass)
	{
		super(edgeClass, vertexClass, composeClass);
	}
	
	public XMIModelDiagram(DiagramGraph graph) 
	{
		super(graph);
	}

	//Meta Data Methods
	@Override
	public void buildMetaData() {
		if(this.eClassList == null) {
			this.eClassList = new HashSet<EClass>();
			
			if(this.getModelDiagram() != null) {
				for(DiagramNode node : this.getModelDiagram().vertexSet()) {
					if(node instanceof XMIDiagramNode) {
						this.eClassList.add(((XMIDiagramNode) node).getEClass());
					}
					if(node instanceof ComposeXMIDiagramNode) {
						for(DiagramNode inNode : node.getFullListOfNodes()) {
							if(inNode instanceof XMIDiagramNode) {
								this.eClassList.add(((XMIDiagramNode) inNode).getEClass());
							}
						}
					}
				}
			}
		}
		
		if(this.eClassNames == null) {
			this.eClassNames = new HashSet<String>();
			
			for(EClass eClass : this.eClassList) {
				this.eClassNames.add(eClass.getName());
			}
		}
	}

	@Override
	public Pair<Set<EClass>, Set<String>> getMetaData() {
		this.buildMetaData();
		
		return new Pair<Set<EClass>, Set<String>>(eClassList,eClassNames);
	}
	
	public Set<EClass> getEClassList() {
		this.buildMetaData();
		
		return this.eClassList;
	}
	
	public Set<String> getEClassNames() {
		this.buildMetaData();
		
		return this.eClassNames;
	}
}
