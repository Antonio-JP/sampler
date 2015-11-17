package rioko.drawmodels.editors.zesteditor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.zest.layouts.LayoutStyles;

import rioko.graphabstraction.diagram.ComposeDiagramNode;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.diagram.ProxyDiagramNode;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.graphabstraction.display.ExpandComposeNodeNestedBuilder;
import rioko.graphabstraction.runtime.registers.NotRegisteredException;
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.drawmodels.draw2d.listeners.ChangeRootMouseListener;
import rioko.drawmodels.editors.AbstractEditorPart;
import rioko.drawmodels.editors.listeners.OpenNewVisualizacionEditorMouseListener;
import rioko.drawmodels.editors.listeners.PropertiesChangeListener;
import rioko.drawmodels.editors.listeners.ZestClickSelectionMouseListener;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.filemanage.GeneralReader;
import rioko.drawmodels.filemanage.Reader;
import rioko.drawmodels.jface.ZestEditorStructuredSelection;
import rioko.drawmodels.layouts.bridge.ZestLayoutAlgorithm;
import rioko.drawmodels.views.listeners.ZestPropertiesListener;
import rioko.revent.REvent;
import rioko.revent.datachange.DataChangeEvent;
import rioko.utilities.Log;
import rioko.zest.ExtendedGraphViewer;

// TODO Tiene el modelo, las propiedades y el visor (ExtendedGraphViewer)
// TODO Las propiedades tienen dos partes: genérica y de algoritmo
// TODO Las propiedades generales afectan a las propiedades de algoritmo
// TODO Las propiedades afectan al visor (actualizan el dibujo del modelo)
// TODO El modelo tiene guardado la lista de filtros posteriores al algoritmo.
// TODO Recibe información de la vista de preferencias y de la vista de filtros

public class ZestEditor extends AbstractEditorPart implements ISelectionProvider {

	private Label label;
	private ModelDiagram<?> model;
	
	private ExtendedGraphViewer viewer;
	private ZestLayoutAlgorithm viewerLayout = new ZestLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	
	private IFile file = null;
	
	private Reader<?> reader = null;
	
	//Hidden attributes
	private ZestProperties properties = new ZestProperties();
	private ZestPropertiesListener propertiesListener;
	private PropertiesChangeListener changeListener;
	
	//Constantes para Flags de actualizacion
	public static final int UPDATE_LAYOUT = 1, UPDATE_NODES = 2, UPDATE_CONNECTIONS = 4, UPDATE_ALL = 7;
	
	//Builders
	public ZestEditor() {

	}
	
	//Getters
	public ModelDiagram<?> getModel() {
		return model;
	}

	public ExtendedGraphViewer getViewer() {
		return viewer;
	}
	
	public ZestProperties getProperties() {
		return this.properties;
	}
	
	//Metodos implementados
	@Override
	public void doSave(IProgressMonitor arg0) {
		// No se guarda
	}

	@Override
	public void doSaveAs() {
		// No se guarda

	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		if(input instanceof FileEditorInput || input instanceof ModelDiagram || input instanceof SpecialInputZestEditor) {
			//Establecemos los parámetros básicos
			this.setSite(site);
			
			this.setInput(input);
			
			//Cargamos el fichero y el metamodelo
			if(input instanceof FileEditorInput) {
				try {
					this.file = ((FileEditorInput) input).getFile();
					this.reader = GeneralReader.getReaderFromFile(this.getFile());
					this.model = this.reader.getModel();
				} catch (IOException e) {
					Log.exception(e);
					MessageDialog.openError(null, "Error loading the File", 
							e.getMessage());
				}
			} else if(input instanceof ModelDiagram) {
	        	this.model = (ModelDiagram<?>)this.getEditorInput();
	        } else if(input instanceof SpecialInputZestEditor) {
				this.properties = ((SpecialInputZestEditor) input).getProperties();
	        	this.model = ModelDiagram.getModelDiagramForGraph(((SpecialInputZestEditor) input).getModel());
			}
			
			//Create the listeners to maintain updated the viewer
			this.createPropertiesListener();
			
			return;
		}		
		
		throw new PartInitException("Rioko ERROR: not input recieved");
	}

	@Override
	public boolean isDirty() {
		return false;
	}

	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void createPartControl(Composite content) {
        //Acutalizamos el nombre de la pestaña del editor        
        this.updateTitle();
		
		//Creación del Layout
		content.setLayout(new GridLayout(1,true));
		
		try{
			//Etiqueta
			label = new Label(content, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER | SWT.CENTER);
			label.setBackground(new Color(label.getDisplay(), 255, 0, 0));
			
	        label.setText(this.getEditorInput().getName());
	        label.setFont(new Font(label.getDisplay(), new FontData("Arial", 16, SWT.BOLD)));
			
			if(this.properties.getRootNode() == null) {
//	        	DiagramNode root = this.model.getModelDiagram().vertexSet().iterator().next();
//	        	this.properties.setRootNode(root);
	        }
	        
			this.properties.getDrawGraph(this.model);
			
			//Visor del grafo
		    viewer = new ExtendedGraphViewer(content, SWT.BORDER);
		    viewer.setLayoutApplicable(false);	//Se evita que se aplique el layout siempre
		    viewer.setContentProvider(new ZestEditorContentProvider(this.model));	
		    viewer.setLabelProvider(new ZestEditorLabelProvider(this.model));
		    viewer.setLayoutAlgorithm(this.viewerLayout);
		    viewer.setInput(this.model.getPrintDiagram().vertexSet());
		    
		    viewer.getControl().addMouseListener(new ChangeRootMouseListener(this));
		    viewer.getControl().addMouseListener(new ZestClickSelectionMouseListener(this, viewer));
		    viewer.getControl().addMouseListener(new OpenNewVisualizacionEditorMouseListener(this, viewer));
		    		   
	        //Organización de los objetos        
	        GridData gd = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
	        gd.grabExcessHorizontalSpace = true;
	        gd.heightHint = 30;
	        
	        this.label.setLayoutData(gd);
	        
	        gd = new GridData(GridData.FILL_BOTH);
	        viewer.getControl().setLayoutData(gd);
	                
	        this.updateView(ZestEditor.UPDATE_LAYOUT);	
	        
	        this.changeListener = new PropertiesChangeListener(this.properties, this);
	        
	        this.getSite().setSelectionProvider(this);
		    
	    } catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}  
	}

	@Override
	public void setFocus() {

	}
	
	@Override
	public void dispose() {
		REvent.removeListener(this.changeListener);
	}

	
	// Proper methods
	public void setFile(IFile file) {
		this.file = file;
	}
	
	public IFile getFile()
	{
		return this.file;
	}
	
	public void setShowingData(boolean showingData)
	{
		this.properties.setShowingData(showingData);
	}
	
	public void toggleShowingData()
	{
		if(this.properties.isShowingData()) {
			this.setShowingData(false);
		} else {
			this.setShowingData(true);
		}
	}
	
	public void toggleShowingConnectionLabels()
	{
		if(this.properties.isShowingConnectionLabels()) {
			this.properties.setShowingConnectionLabels(false);
		} else {
			this.properties.setShowingConnectionLabels(true);
		}
	}
	
	public void changeZestProperties(ZestProperties properties)
	{
		this.properties = properties;
		//We throw an event to tell other parts that we have change drastically the Properties of the editor
		new DataChangeEvent(this);
		
		//We update the listener of the properties
		this.createPropertiesListener();
		REvent.removeListener(this.changeListener);
		try {
			this.changeListener = new PropertiesChangeListener(this.properties, this);
		} catch (Exception e) {
			Log.exception(e);
		}
	}
	
	private void createPropertiesListener() {
		try {
			if(this.propertiesListener != null) {
				REvent.removeListener(this.propertiesListener);
			}
		
			this.propertiesListener = new ZestPropertiesListener(this.properties, this) {
				
				@Override
				protected void doWhenGenericChange() {
					updateView(UPDATE_LAYOUT | UPDATE_NODES | UPDATE_CONNECTIONS);
				}
				
				@Override
				protected void doWhenAlgorithmChange() {
					updateView(UPDATE_ALL);
				}

				@Override
				protected void doWhenFiltersChange() {
					updateView(UPDATE_ALL);
				}
			};
		} catch (Exception e) {
			//Impossible Exception
			Log.exception(e);
		}
	}

	//Updating methods	
	private void updateTitle()
	{
		this.setPartName(this.getEditorInput().getName());
		this.setTitleToolTip(this.getEditorInput().getToolTipText());
	}
	
	public void updateView(int updates)
	{
		//Si hay cambio de todo, creamos el grafo a dibujar
		if(updates == UPDATE_ALL) {
			this.properties.getDrawGraph(this.model);
		}
		//Creamos los nodos que hagan falta
		if((updates & (UPDATE_NODES | UPDATE_CONNECTIONS)) != 0) {
			if((updates & UPDATE_NODES) != 0) { 
				((ZestEditorLabelProvider)this.viewer.getLabelProvider()).setShowingData(this.properties.isShowingData());
				
				for(DiagramNode node : this.model.getPrintDiagram().vertexSet()) {
					//Limpiamos las figuras
					node.clearFigure();
				}
			}
			if((updates & UPDATE_CONNECTIONS) != 0) { 
				((ZestEditorLabelProvider)this.viewer.getLabelProvider()).setShowingConnectionLabel(this.properties.isShowingConnectionLabels());
			}
			
			this.viewer.setInput(this.model.getPrintDiagram().vertexSet());
		}
		
		//Ponemos el layout si hace falta
		if((updates & UPDATE_LAYOUT) != 0) {
			this.viewerLayout.setLayoutAlgorithm(this.properties.getActualLayout());
			this.viewer.forceApplyLayout();
		}
	}
	
	public void updateView()
	{
		this.updateView(UPDATE_ALL);
	}
	
	// Methods of handlers
	public boolean changeRoot(DiagramNode node)
	{
		if(this.properties.getRootNode() != null && node == null)
		{
			this.properties.setRootNode(null);
		}
		if(node != null) {
			if(!node.equals(this.properties.getRootNode())) {
				if(this.model.getModelDiagram().containsVertex(node)) {
					this.properties.setRootNode(node);
				} else if(this.model.getPrintDiagram() != null && this.model.getPrintDiagram().vertexSet().contains(node)) {
					this.properties.setRootNode(node.getRootNode());
				}
			}
		}
		
		return false;
	}
	
	public boolean changeRoot(String nodeName)
	{
		for(DiagramNode node : this.model.getModelDiagram().vertexSet()) {
			if(node.getTitle().equals(nodeName)) {
				return this.changeRoot(node);
			}
		}
		
		return false;
	}
	
	public void resetProperties()
	{
		while(this.properties.popConfiguration());
	}
	
	public void showAll()
	{
		try {
			this.properties.changeNestedAlgorithm(RegisterBuilderAlgorithm.getTrivialAlgorithm());
		} catch (NotRegisteredException e) {
			Log.exception(e);
			Log.print("The algorithm \"Show All\" has not been created");
		}
	}
	
	//Métodos de generación de nueva ventana
	public void createNavigationEditor(Collection<DiagramNode> collection) {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			ModelDiagram<?> newModel = ModelDiagram.getModelDiagramForGraph((DiagramGraph)this.model.getModelDiagram().inducedSubgraph(collection));
			newModel.setName(this.model.getName() + "/" + collection.iterator().next().getTitle());
	        IEditorPart editor = IDE.openEditor(page, 
	        		newModel, 
	        		this.getSite().getId());
	        
	        if(!(editor instanceof ZestEditor)) {
	        	//Impossible exception
	        	throw new PartInitException("Bad editor created");
	        }
	        
	        ((ZestEditor) editor).setFile(this.file);
	        
	    } catch ( PartInitException e ) {
	        Log.exception(e);
	    }
	}
	
	public void createNavigationEditor(ComposeDiagramNode compose)
	{
		this.createNavigationEditor(compose.getNodes());
	}
	
	public void createNavigationEditor(ProxyDiagramNode<?> proxy)
	{
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
			ModelDiagram<?> newModel = this.model.getModelFromProxy(proxy);
			if(newModel == null) {
				throw new Exception("The proxy is not contained in the model");
			}
			
	        IEditorPart editor = IDE.openEditor(page, newModel, this.getSite().getId());
	        
	        if(!(editor instanceof ZestEditor)) {
	        	//Impossible exception
	        	throw new PartInitException("Bad editor created");
	        }
	        
	        ((ZestEditor) editor).setFile(this.file);
	    } catch ( Exception e ) {
	        Log.exception(e);
	    } 
	}
	
	//Métodos  de ampliación de un nodo
	public void extendComposeNode(ComposeDiagramNode compose)
	{
		if(compose.isCompose())
		{
			ExpandComposeNodeNestedBuilder builder = new ExpandComposeNodeNestedBuilder(compose, this.model.getPrintDiagram());
			this.model.setPrintDiagram(this.properties.applyFilters(builder.createNestedGraph(this.model.getModelDiagram(), this.properties.getAlgorithmConfigurable())));
			this.updateView(ZestEditor.UPDATE_NODES | ZestEditor.UPDATE_LAYOUT);
		}
	}
	
	public void extendProxyNode(ProxyDiagramNode<?> proxy)
	{
		if(this.model.resolveProxy(proxy)) {
			this.updateView();
		}
	}
	
	//ISelectionProvider Methods
	
	private ArrayList<ISelectionChangedListener> selectionChangedListeners = new ArrayList<>();
	private ZestEditorStructuredSelection selection=null;

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionChangedListeners.add(listener);
	}

	@Override
	public ISelection getSelection() {
		return selection;
	}

	@Override
	public void removeSelectionChangedListener(ISelectionChangedListener listener) {
		this.selectionChangedListeners.remove(listener);  
	}

	@Override
	public void setSelection(ISelection select) {
		if(select instanceof ZestEditorStructuredSelection) {
			ZestEditorStructuredSelection zstSel = (ZestEditorStructuredSelection)select;
			
			for (ISelectionChangedListener listener : this.selectionChangedListeners) {  
				listener.selectionChanged(new SelectionChangedEvent(this, zstSel));
			}
			
			this.selection = zstSel;
		}
	}
}
