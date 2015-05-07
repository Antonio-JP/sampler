package rioko.drawmodels.handlers;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.filemanage.XMIReader;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.runtime.registers.RegisterBuilderAlgorithm;
import rioko.utilities.Log;
import rioko.utilities.Timing;

public class RunTests extends AbstractGenericHandler {
		
	private static final int nTests = 1;
	
	private static final int minSize = 100, maxSize = 100;
	
	//Result file names
	private static final String open = "__opening_file";
	private static final String run = "__run_";
//	private static final String draw = "draw_model";
	
	private static final String extension = ".csv";
	
	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {
		//We run the tests for this project
		//There are three measures: reading files, algorithm execution and drawing time
		//The reading time and algorithm time is easy to test, but to drawing we are going to open 
		//  different editors pages and executing its drawing algorithms.
		
		//It will be several methods to make easier the coding and reading of teh tests.
		Log.print("- Running tests");
		int size = minSize;
		
		Timing tOpen = Timing.getInstance(open+extension);
//		Timing tDraw = Timing.getInstance(draw+extension);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project  = root.getProject("Testing_Sampler");
		IFolder folder = project.getFolder("tests");
		
		Random r = new Random();
		
		Log.print("- Initialization done");
		while(size <= maxSize) { //Iterations over the size
			Log.print("("+ size + "): ** Begin with size " + size);
			for(int test = 0; test < nTests; test++) { //Iterations over the numbers of tests
				Log.print("("+ size + "): +++ Beggin with test "+ (test+1) + "/" + nTests);
				//Reading part
				IFile file = folder.getFile("result" + test + "_" + size + ".xmi");
				ModelDiagram model = null;
				
				// ---- Measuring time
				Log.print("("+ size + "): &&&& Measuring the reading...");
				tOpen.begginTiming(size);
					try {
						model = new ModelDiagram(XMIReader.getReaderFromFile(file));
					} catch (IOException e) {
						Log.print("("+ size + "): Error creating the model for the test " + test + " of size " + size + ".");
					}
				tOpen.endTiming();
				Log.print("("+ size + "): &&&& Reading measure finished");
				// ---- End measure
				
				//We create now the properties to iterate over its algorithms
				ZestProperties properties = new ZestProperties();
				Collection<NestedBuilderAlgorithm> algorithms = RegisterBuilderAlgorithm.getRegisteredAlgorithms();
				
				for(NestedBuilderAlgorithm algorithm : algorithms) {
					Log.print("("+ size + "): &&&& Testing algorithm " + algorithm.getAlgorithmName());
					properties.changeNestedAlgorithm(algorithm);
					//Configuring the algorithm
					for(DisplayOptions option : algorithm.getConfigurationNeeded()) {
						switch(option) {
							case ECLASS_FILTER:
								//TODO poner una clase aleatoria del grafo
								break;
							case LEVELS_TS:
								break;
							case MAX_NODES:
								properties.setMaxNodes(r.nextInt(10)+1);
								break;
							case ROOT_NODE:
								properties.setRootNode(model.getModelDiagram().vertexSet().iterator().next());
								break;
							default:
								break;
						}
					}
					Timing currentTime = Timing.getInstance(run+algorithm.getAlgorithmName() + extension);
					// ---- Measuring time
					Log.print("("+ size + "): Measuring the algorithm...");
					currentTime.begginTiming(size);
						algorithm.createNestedGraph(model.getModelDiagram(), properties.getAlgorithmConfigurable());
					currentTime.endTiming();
					Log.print("("+ size + "): Run measure finished");
					// ---- End measure
					
					// Drawing part
					Log.print("("+ size + "): Not implemented the measure of drawing...");
					//TODO hacer la parte de pintado
					Log.print("("+ size + "): &&&& Finished the algorithm " + algorithm.getAlgorithmName());
					
				}
				Log.print("("+ size + "): +++ Finished the test " + (test+1) + "/" + nTests);
				
				XMIReader.closeFile(file);
				
				System.gc();
				
			}
			Log.print("("+ size + "): ** Finished the size " + size);
			
			size = nextSize(size);
		}
		
		Log.print("- Finished the tests");
		Log.print("- Compctifying files...");
		Timing.closeAll();
		Log.print("- End of compactification");
		Log.print(" ------------------- End of Running Tests --------------------------");
		return null;
	}

	
	private static int nextSize(int size) {
		if(size >= 10000) {
			return size+1000;
		} else if(size >= 1000) {
			return size+500;
		} else if(size >= 100) {
			return size+100;
		} else {
			return 100;
		}
	}
}
