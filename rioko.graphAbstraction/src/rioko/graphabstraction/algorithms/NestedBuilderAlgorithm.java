package rioko.graphabstraction.algorithms;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import rioko.utilities.Copiable;
import rioko.utilities.Log;
import rioko.utilities.Timing;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.diagram.DiagramGraph;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.display.FilterNestedBuilder;
import rioko.graphabstraction.display.GlobalNestedBuilder;
import rioko.graphabstraction.display.GraphBuilder;
import rioko.graphabstraction.display.LocalNestedBuilder;
import rioko.graphabstraction.display.NestedGraphBuilder;

public class NestedBuilderAlgorithm extends Algorithm implements GraphBuilder, Copiable{
	
	//Static variables
	
	
	public static final int PRE_POSITION = 1, MID_FILTER_POSITION = 2, MID_GLOBAL_POSITION = 3, POST_POSITION = 4;
	
	//Private Attributes
	/**
	 * Lists of steps to generate the final view
	 */
	private ArrayList<NestedGraphBuilder> preProcessor = new ArrayList<>();
	private ArrayList<FilterNestedBuilder> filters = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> midProcessorFilter = new ArrayList<>();
	private ArrayList<GlobalNestedBuilder> globalCriteria = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> midProcessorGlobal = new ArrayList<>();
	private ArrayList<LocalNestedBuilder> localCriteria = new ArrayList<>();
	private ArrayList<NestedGraphBuilder> postProcessor = new ArrayList<>();
	
	/**
	 * Global list of steps
	 */
	private ArrayList<NestedGraphBuilder> allSteps = new ArrayList<>();
	
	/**
	 * Flag to check if every steps have their own configuration
	 */
//	private boolean allConfigurated = true;
	
	//Builders
	public NestedBuilderAlgorithm() {
		super();
	}
	
	public NestedBuilderAlgorithm(String algorithmName) {
		super(algorithmName);
	}
	
	//Copiable methods
	@Override
	public NestedBuilderAlgorithm copy() {
		NestedBuilderAlgorithm copy = null;
		try {
			//Create a new instance with the same name
			copy = this.getClass().getConstructor().newInstance();
			copy.setAlgorithmName(this.getAlgorithmName(),false);
			
			//Add all the steps in the same order to the copy algorithm
			copy.removeAll();
			for(NestedGraphBuilder step : this.preProcessor) {
				copy.addOtherStep(step,PRE_POSITION);
			}
			for(FilterNestedBuilder filter : this.filters) {
				copy.addFilter(filter);
			}
			for(NestedGraphBuilder step : this.midProcessorFilter) {
				copy.addOtherStep(step, MID_FILTER_POSITION);
			}
			for(GlobalNestedBuilder global : this.globalCriteria) {
				copy.addGlobalCriteria(global);
			}
			for(NestedGraphBuilder step : this.midProcessorGlobal) {
				copy.addOtherStep(step, MID_GLOBAL_POSITION);
			}
			for(LocalNestedBuilder local : this.localCriteria) {
				copy.addLocalCriteria(local);
			}
			for(NestedGraphBuilder step : this.postProcessor) {
				copy.addOtherStep(step, POST_POSITION);
			}
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException	| NoSuchMethodException | SecurityException e) {
			Log.exception(e);
			Log.print("Error trying to copy an algorithm");
		}
		return copy;
	}
	
	//Control methods
	public boolean addFilter(FilterNestedBuilder filter) {
		this.allSteps.add(filter);
		return filters.add(filter);
	}
	
	public boolean addGlobalCriteria(GlobalNestedBuilder globalStep) {
		this.allSteps.add(globalStep);
		return globalCriteria.add(globalStep);
	}
	
	public boolean addLocalCriteria(LocalNestedBuilder localStep) {
		this.allSteps.add(localStep);
		return localCriteria.add(localStep);
	}
	
	public boolean addOtherStep(NestedGraphBuilder step, int position) {
		if(position >= PRE_POSITION && position <= POST_POSITION) {
			switch(position) {
				case PRE_POSITION:
					this.preProcessor.add(step);				
					break;
				case MID_FILTER_POSITION:
					this.midProcessorFilter.add(step);
					break;
				case MID_GLOBAL_POSITION:
					this.midProcessorGlobal.add(step);
					break;
				case POST_POSITION:
					this.postProcessor.add(step);
					break;
			}

			this.allSteps.add(step);
			
			return true;
		}
		
		return false;
	}
	
	public boolean removeFilter(FilterNestedBuilder filter) {
		for(int i = 0; i < this.filters.size(); i++) {
			if(filter == this.filters.get(i)) {
				this.filters.remove(i);
				this.removeFromAllSteps(filter);
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeGlobalCriteria(GlobalNestedBuilder globalCriteria) {
		for(int i = 0; i < this.globalCriteria.size(); i++) {
			if(globalCriteria == this.globalCriteria.get(i)) {
				this.globalCriteria.remove(i);
				this.removeFromAllSteps(globalCriteria);
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeLocalCriteria(LocalNestedBuilder localCriteria) {
		for(int i = 0; i < this.localCriteria.size(); i++) {
			if(localCriteria == this.localCriteria.get(i)) {
				this.localCriteria.remove(i);
				this.removeFromAllSteps(localCriteria);
				
				return true;
			}
		}
		
		return false;
	}
	
	public boolean removeOtherSteps(NestedGraphBuilder step) {
		for(int i = 0; i < this.preProcessor.size(); i++) {
			if(step == this.preProcessor.get(i)) {
				this.preProcessor.remove(i);
				this.removeFromAllSteps(step);
				
				return true;
			}
		}
		
		for(int i = 0; i < this.midProcessorFilter.size(); i++) {
			if(step == this.midProcessorFilter.get(i)) {
				this.midProcessorFilter.remove(i);
				this.removeFromAllSteps(step);
				
				return true;
			}
		}
		
		for(int i = 0; i < this.midProcessorGlobal.size(); i++) {
			if(step == this.midProcessorGlobal.get(i)) {
				this.midProcessorGlobal.remove(i);
				this.removeFromAllSteps(step);
				
				return true;
			}
		}
		
		for(int i = 0; i < this.postProcessor.size(); i++) {
			if(step == this.postProcessor.get(i)) {
				this.postProcessor.remove(i);
				this.removeFromAllSteps(step);
				
				return true;
			}
		}
		
		return false;
	}
	
	public void removeAllFilters() {
		for(FilterNestedBuilder filter : this.filters) {
			this.removeFromAllSteps(filter);
		}
		
		this.filters.clear();
	}
	
	public void removeAllGlobalCriteria() {
		for(GlobalNestedBuilder global : this.globalCriteria) {
			this.removeFromAllSteps(global);
		}
		
		this.globalCriteria.clear();
	}
	
	public void removeAllLocalCriteria() {
		for(LocalNestedBuilder local : this.localCriteria) {
			this.removeFromAllSteps(local);
		}
		
		this.localCriteria.clear();
	}
	
	public void removeAllOtherSteps() {
		for(NestedGraphBuilder step : this.preProcessor) {
			this.removeFromAllSteps(step);
		}
		
		for(NestedGraphBuilder step : this.midProcessorFilter) {
			this.removeFromAllSteps(step);
		}
		
		for(NestedGraphBuilder step : this.midProcessorGlobal) {
			this.removeFromAllSteps(step);
		}
		
		for(NestedGraphBuilder step : this.postProcessor) {
			this.removeFromAllSteps(step);
		}
		
		this.preProcessor.clear();
		this.midProcessorFilter.clear();
		this.midProcessorGlobal.clear();
		this.postProcessor.clear();
	}
	
	public void removeAll() {
		this.removeAllFilters();
		this.removeAllGlobalCriteria();
		this.removeAllLocalCriteria();
		this.removeAllOtherSteps();
	}
	
	private void removeFromAllSteps(NestedGraphBuilder step) {
		this.allSteps.remove(step);
	}
	
	//Graph Builder methods
	@Override
	public DiagramGraph createNestedGraph(DiagramGraph data, Configurable properties) {
		// -------- MEASURING TIME --------- //
		Timing t = Timing.getInstance(this.getAlgorithmName() + "_test.txt");
		t.begginTiming(data.vertexSet().size());
		// --------------------------------- //
		
		DiagramGraph target = data;

		Log.print("Ejecutando algoritmo " + this.getAlgorithmName() + "...");
		Log.xOpen("algorithm");
		if(this.checkProperties(properties)) {			
			try {
				Log.xPrint("Propiedades válidas. Comenzando la ejecución...");
				Log.xOpen("nodes");
				//Nodos
				target = this.buildNodes(data, properties);
				Log.xClose("nodes");
				//Aristas
				if(target != data) {
					Log.xOpen("edges");
					this.buildEdges(target, data);
					Log.xClose("edges");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Log.xClose("algorithm");
		Log.print("Fin del algoritmo " + this.getAlgorithmName());
		
		// -------- MEASURING TIME --------- //
		t.endTiming();
		// --------------------------------- //
		
		return target;
	}
	
	@Override
	public DiagramGraph buildNodes(DiagramGraph data, Configurable properties) throws Exception {
		DiagramGraph nextStepGraph;
		DiagramGraph currentGraph = data;
		
		HashSet<DiagramNode> rootNodes = new HashSet<>();
		Log.xOpen("steps");
		Log.xPrint("Aplicando preprocesamiento...");
		Log.xOpen("pre-proc");
		nextStepGraph = applyArray(this.preProcessor, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("pre-proc");
		Log.xPrint("Done");
		
		Log.xPrint("Aplicando filtros...");
		Log.xOpen("filters");		
		nextStepGraph = applyArray(this.filters, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("filters");
		Log.xPrint("Done");
		
		Log.xPrint("Aplicando pasos antes de los criterios globales...");
		Log.xOpen("pre-global");
		nextStepGraph = applyArray(this.midProcessorFilter, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("pre-global");
		Log.xPrint("Done");

		Log.xPrint("Aplicando criterios globales...");
		Log.xOpen("global");
		nextStepGraph = applyArray(this.globalCriteria, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("global");
		Log.xPrint("Done");

		Log.xPrint("Aplicando pases antes de los criterios locales...");
		Log.xOpen("pre-local");
		nextStepGraph = applyArray(this.midProcessorGlobal, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("pre-local");
		Log.xPrint("Done");

		Log.xPrint("Aplicando criterios locales...");
		Log.xOpen("local");
		nextStepGraph = applyArray(this.localCriteria, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("local");
		Log.xPrint("Done");

		Log.xPrint("Aplicando postprocesamiento...");
		Log.xOpen("pos-proc");
		nextStepGraph = applyArray(this.postProcessor, properties, currentGraph, rootNodes);
		currentGraph = nextStepGraph;
		Log.xClose("pos-proc");
		Log.xPrint("Done");

		Log.xPrint("Pintando los nodos raiz...");
		applyRootNodes(currentGraph, rootNodes);
		Log.xPrint("Done");
		Log.xPrint("Ejecucion del algoritmo completa");
		Log.xClose("steps");
		
		return currentGraph;
	}
	
	private static DiagramGraph applyArray(ArrayList<? extends NestedGraphBuilder> steps, Configurable properties, DiagramGraph data, HashSet<DiagramNode> rootNodes) {
		DiagramGraph nextStepGraph;
		DiagramGraph currentGraph = data;
		
		for(NestedGraphBuilder step : steps) {
			Object objConf = step.getConfiguration("Root Node");
			if(objConf instanceof DiagramNode) {
				rootNodes.add((DiagramNode)objConf);
			} else if(properties.getConfiguration(DisplayOptions.ROOT_NODE.toString()) != null){
				rootNodes.add((DiagramNode) properties.getConfiguration(DisplayOptions.ROOT_NODE.toString()));
			}
			Log.print("Ejecutando paso " + step.getClass().getSimpleName() + "...");
			Log.xOpen("step");
			nextStepGraph = step.createNestedGraph(currentGraph, properties);
			Log.xClose("step");
						
			currentGraph = nextStepGraph;
		}
		
		return currentGraph;
	}
	
	private static void applyRootNodes(DiagramGraph graph, HashSet<DiagramNode> rootNodes) {
		for(DiagramNode node : graph.vertexSet()) {
			boolean find = false;
			for(DiagramNode root : rootNodes) {
				if(node.areRelated(root)) {
					node.setBeRoot(true);
					find = true;
					break;
				}
			}
			if(!find) {
				node.setBeRoot(false);
			}
		}
	}
		
	@Override
	public boolean checkProperties(Configurable properties) {
		for(NestedGraphBuilder step : this.allSteps) {
			for(DisplayOptions option : this.getNeededOptions(step)) {
				if(properties.getConfiguration(option.toString()) == null) {
					return false;
				}
			}
		}
		
		return true;
	}

	@Override
	public void buildEdges(DiagramGraph target, DiagramGraph data) {
		//this.builder.buildEdges(target, data);
	}
	
	@Override
	public Collection<DisplayOptions> getConfigurationNeeded() {
		HashSet<DisplayOptions> options = new HashSet<>();
		
		for(NestedGraphBuilder step : this.allSteps) {
			for(DisplayOptions opt : step.getConfigurationNeeded()) {
				options.add(opt);
			}
		}
		
		return options;
	}
	
	//Private method
//	private static DisplayProperties getCorrectConfiguration(NestedGraphBuilder step, DisplayProperties configuration, DisplayProperties generalConf) {
//		DisplayProperties finalConf;
//		if(configuration != null) {
//			finalConf = configuration.copy();
//		} else {
//			finalConf = new DisplayProperties();
//		}
//		for(DisplayOptions option : step.getConfigurationNeeded()) {
//			if(!finalConf.isSet(option)) {
//				finalConf.setAttribute(option, generalConf.getAttribute(option));
//			}
//		}
//		
//		return finalConf;
//	}

	private DisplayOptions[] getNeededOptions(NestedGraphBuilder step) {
		ArrayList<DisplayOptions> list = new ArrayList<>();
		
		for(DisplayOptions option : step.getConfigurationNeeded()) {
				list.add(option);
		}
		
		return list.toArray(new DisplayOptions[0]);
	}
	
}
