package sampler.junit.tests.performance;

import java.io.IOException;
import java.util.Collection;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import rioko.drawmodels.algorithms.display.test.SimpleGlobalAndLocalAlgorithm;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.editors.zesteditor.zestproperties.ZestProperties;
import rioko.drawmodels.filemanage.XMIReader;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.runtime.registers.RegisterBuilderAlgorithm;
import rioko.utilities.Log;
import rioko.utilities.Timing;

@RunWith(Parameterized.class)
public class AbstractionPerformance {
	
	private static final int numRepeats = 1;
	
	private static ModelDiagram model = null;

	private NestedBuilderAlgorithm algorithm = null;
	
	private static int currentSize = 0;
	
	@Parameters
	public static Collection<NestedBuilderAlgorithm> getParameters() {
		return RegisterBuilderAlgorithm.getRegisteredAlgorithms();
	}
	
	public AbstractionPerformance(NestedBuilderAlgorithm algorithm) {
		this.algorithm = algorithm;
	}

	@BeforeClass
	public void setUpBeforeClass() throws Exception {
		String path = BridgeTest.getFilePath();
		currentSize = BridgeTest.getSizeTest();
		
		Timing tOpen = Timing.getInstance(PerformanceTestConstants.OPEN_PREFIX+PerformanceTestConstants.FILE_EXTENSION);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project  = root.getProject(PerformanceTestConstants.PROJECT_NAME);
		IFolder folder = project.getFolder(PerformanceTestConstants.FOLDER_NAME);
		
		Log.print("("+ currentSize+") : +++ Reading test file "+ path);
		//Reading part
		IFile file = folder.getFile(path);
				
		// ---- Measuring time
		Log.print("("+ currentSize+") : &&&& Measuring the reading...");
		tOpen.begginTiming(currentSize);
			try {
				model = new ModelDiagram(XMIReader.getReaderFromFile(file));
			} catch (IOException e) {
				Log.print("("+ currentSize + "): Error creating the model for the test " + path + ".");
			}
		tOpen.endTiming();
		Log.print("("+ currentSize + "): &&&& Reading measure finished");
		
		XMIReader.closeFile(file);
	}

	@After
	public void tearDown() throws Exception {
		Log.print("("+ currentSize + "): +++ Finished the test");
	}

	@Test
	public void test() {
		ZestProperties properties = new ZestProperties();

		if(!(this.algorithm instanceof SimpleGlobalAndLocalAlgorithm)) {
			Timing currentTime = Timing.getInstance(PerformanceTestConstants.RUN_PREFIX+algorithm.getAlgorithmName() + PerformanceTestConstants.FILE_EXTENSION);
			
			for(int i = 0; i < numRepeats; i++) {
				Log.print("("+ currentSize + "): &&&& Testing algorithm " + algorithm.getAlgorithmName());
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
							properties.setMaxNodes((new Random()).nextInt(10)+1);
							break;
						case ROOT_NODE:
							properties.setRootNode(model.getModelDiagram().vertexSet().iterator().next());
							break;
						default:
							break;
					}
				}
				// ---- Measuring time
				Log.print("("+ currentSize + "): Measuring the algorithm...");
				currentTime.begginTiming(currentSize);
					algorithm.createNestedGraph(model.getModelDiagram(), properties.getAlgorithmConfigurable());
				currentTime.endTiming();
				Log.print("("+ currentSize + "): Run measure finished");
				// ---- End measure
				
				// Drawing part
				Log.print("("+ currentSize + "): Not implemented the measure of drawing...");
				//TODO hacer la parte de pintado
				Log.print("("+ currentSize + "): &&&& Finished the algorithm " + algorithm.getAlgorithmName());
			}
		}
	}

}
