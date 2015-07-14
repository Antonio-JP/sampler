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
import rioko.drawmodels.filemanage.GeneralReader;
import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.display.DisplayOptions;
import rioko.graphabstraction.runtime.registers.RegisterBuilderAlgorithm;
import rioko.utilities.Log;
import rioko.utilities.Timing;

@RunWith(Parameterized.class)
public class JDTASTPerformance {
	
	private static final int firstSet = 0, finalSet = 4;
	
	private int currentSet = 0;
	
	private String path = "";
	
	private ModelDiagram<?> model = null;
	private IFile file = null;
	
	//Static methods
	
	//To get the paremeters of different executions
	@Parameters
	public static List<Integer[]> getParameters() {
		List<Integer[]> list = new ArrayList<>();
		
		for(int i = firstSet; i <= finalSet; i++) {
			Integer[] set = new Integer[] {i};
			list.add(set);
		}
		
		return list;
	}
		
	//Builders
	public JDTASTPerformance(Integer set) {
		this.currentSet = set;
		this.path = "set" + set + ".xmi";
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
		IProject project  = root.getProject(PerformanceTestConstants.J_PROJECT_NAME);
		Assert.assertTrue("Not project "+ PerformanceTestConstants.J_PROJECT_NAME +" created in junit-workspace", project.exists());
		IFolder folder = project.getFolder(PerformanceTestConstants.J_FOLDER_NAME);
		Assert.assertTrue("Not folder "+ PerformanceTestConstants.J_FOLDER_NAME +" created in junit-workspace", project.exists());
		
		Log.print("("+ currentSet +") : +++ Reading test file "+ path);
		//Reading part
		this.file = folder.getFile(path);
				
		// ---- Measuring time
		Log.print("("+ currentSet+") : &&&& Measuring the reading...");
		tOpen.begginTiming(currentSet);
			try {
				this.model = GeneralReader.getReaderFromFile(file).getModel();
			} catch (IOException e) {
				Log.exception(e);
				Log.print("("+ currentSet + "): Error creating the model for the test " + path + ".");
			}
		tOpen.endTiming();
		Log.print("("+ currentSet + "): &&&& Reading measure finished");

		} catch (IOException e) {
			Log.exception(e);
			Assert.assertTrue("Error while reading the test", false);
		}
	}

	//Tearing down methods
	@After
	public void tearDown() throws Exception {
		Log.print("("+ currentSet + "): +++ Finished the test " + (this.currentSet+1) + "/" + (Integer.max(finalSet - firstSet + 1,0)));
		
		//Cleaning the memory
		this.path = null;
		this.model = null;
		GeneralReader.closeFile(this.file);
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
					
					Log.print("("+ currentSet + "): &&&& Testing algorithm " + algorithm.getAlgorithmName());
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
					Log.print("("+ currentSet + "): Measuring the algorithm...");
					currentTime.begginTiming(currentSet);
						algorithm.createNestedGraph(model.getModelDiagram(), properties.getAlgorithmConfigurable());
					currentTime.endTiming();
					Log.print("("+ currentSet + "): Run measure finished");
					// ---- End measure
					
					// Drawing part
					Log.print("("+ currentSet + "): Not implemented the measure of drawing...");
					//TODO hacer la parte de pintado
					Log.print("("+ currentSet + "): &&&& Finished the algorithm " + algorithm.getAlgorithmName());
				}
			}
	}

}
