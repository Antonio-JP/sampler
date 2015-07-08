package sampler.junit.tests.performance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
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
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.utilities.Log;
import rioko.utilities.Timing;

@RunWith(Parameterized.class)
public class AbstractionPerformance {
	
	private int currentSize = 0, currentTest = 0;
	
	private String path = "";
	
	private ModelDiagram model = null;
	private IFile file = null;
	
	//Static methods
	
	//To get the paremeters of different executions
	@Parameters
	public static List<Integer[]> getParameters() {
		List<Integer[]> list = new ArrayList<>();
		
		for(int size = PerformanceTestConstants.MIN_SIZE; size < PerformanceTestConstants.MAX_SIZE; size = PerformanceTestConstants.nextSize(size)) {
			for(int i = 0; i < PerformanceTestConstants.NUM_MODELS; i++) {
				Integer[] pair = new Integer[] {size,i};
				list.add(pair);
			}
		}
		return list;
	}
	
	//To open each test in a new workspace
	public static void suite() {
		//return new PerformanceSessionTestSuite("sampler.JUnit.tests", AbstractionPerformance.class)
	}
	
	//Builders
	public AbstractionPerformance(Integer size, Integer num) {
		this.currentSize = size;
		this.currentTest = num;
		this.path = "result" + num + "_" + size + ".xmi";
	}

	//Setting up methods
	@BeforeClass
	public static void setUpBeforeClass() {
		Timing.setPath(PerformanceTestConstants.PATH_TO_RESULTS);
	}
	
	@Before
	public void setUp() throws Exception {
		try {
		Timing tOpen = Timing.getInstance(PerformanceTestConstants.OPEN_PREFIX+PerformanceTestConstants.FILE_EXTENSION);
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		IProject project  = root.getProject(PerformanceTestConstants.PROJECT_NAME);
		Assert.assertTrue("Not project "+ PerformanceTestConstants.PROJECT_NAME +" created in junit-workspace", project.exists());
		IFolder folder = project.getFolder(PerformanceTestConstants.FOLDER_NAME);
		Assert.assertTrue("Not folder "+ PerformanceTestConstants.FOLDER_NAME +" created in junit-workspace", project.exists());
		
		Log.print("("+ currentSize+") : +++ Reading test file "+ path);
		//Reading part
		this.file = folder.getFile(path);
				
		// ---- Measuring time
		Log.print("("+ currentSize+") : &&&& Measuring the reading...");
		tOpen.begginTiming(currentSize);
			try {
				this.model = XMIReader.getReaderFromFile(file).getModel();
			} catch (IOException e) {
				Log.exception(e);
				Log.print("("+ currentSize + "): Error creating the model for the test " + path + ".");
			}
		tOpen.endTiming();
		Log.print("("+ currentSize + "): &&&& Reading measure finished");

		} catch (IOException e) {
			Log.exception(e);
			Assert.assertTrue("Error while reading the test", false);
		}
	}

	//Tearing down methods
	@After
	public void tearDown() throws Exception {
		Log.print("("+ currentSize + "): +++ Finished the test " + (this.currentTest+1) + "/" + PerformanceTestConstants.NUM_MODELS);
		
		//Cleaning the memory
		this.path = null;
		this.model = null;
		XMIReader.closeFile(this.file);
		System.gc();
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		Timing.closeAll();
	}

	//Running methods
	@Test
	public void test() throws Exception{
		Assert.assertNotNull("Error creating the model", this.model);
		
		Random r = new Random();
		ZestProperties properties = new ZestProperties();
			for(NestedBuilderAlgorithm algorithm : RegisterBuilderAlgorithm.getRegisteredAlgorithms()) {
				if(!(algorithm instanceof SimpleGlobalAndLocalAlgorithm)) {
					Timing currentTime = Timing.getInstance(PerformanceTestConstants.RUN_PREFIX+algorithm.getAlgorithmName() + PerformanceTestConstants.FILE_EXTENSION);
					
					for(int i = 0; i < PerformanceTestConstants.NUM_REPEATS; i++) {
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
									properties.setMaxNodes(r.nextInt(10)+1);
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

}
