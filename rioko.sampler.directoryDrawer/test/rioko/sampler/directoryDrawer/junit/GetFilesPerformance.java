package rioko.sampler.directoryDrawer.junit;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.input.ReaderInputStream;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import rioko.utilities.Timing;

@RunWith(Parameterized.class)
public class GetFilesPerformance {
	
	//Static elements
	private static final int MIN_SIZE = 100, MAX_SIZE = 5000;
	
	private static final int GET_NEXT_SIZE(int size) {
		int pow = (int)Math.floor(Math.log10(size));
		pow = (pow > 3) ? 3 : pow;
		
		int unit = (int)Math.pow(10, pow);
		int divide = size/unit;
		
		return unit*(divide+1);
	}
	
	//Class fields
	private int currentSize = 0;
	private String projectName = null;
	private IProject project = null;
	
	//To get the paremeters of different executions
	@Parameters
	public static List<Integer> getParameters() {
		List<Integer> list = new ArrayList<>();
		
		for(int size = MIN_SIZE; size <= MAX_SIZE; size = GET_NEXT_SIZE(size)) {
			list.add(size);
		}
		return list;
	}
	
	//Builders
	public GetFilesPerformance(Integer size) {
		this.currentSize = size;
		this.projectName = "DirectoryTestProject" + currentSize;
	}
	
	//Setting up methods
	@BeforeClass
	public static void setUpBeforeClass() {
		//C:\\Users\\All Users\\test_results\\
		Timing.setPath(".\\tests\\");
	}
	
	@Before
	public void setUp() throws Exception {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot root = workspace.getRoot();
		System.out.println("("+ this.currentSize + "): *** Working on project " + this.projectName);
		project  = root.getProject(this.projectName);
		IProgressMonitor monitor = new NullProgressMonitor();
		if(!project.exists()) {
			project.create(monitor);
			project.open(monitor);
			
			for(int i = 1; i < this.currentSize; i++) {
				IFile file = project.getFile("test_" + i + ".txt");
				file.create(new ReaderInputStream(new StringReader("This is the test file number " + i)), true, monitor);
				System.out.println("("+ this.currentSize + "): *** Creating file " + i);
			}
			project.refreshLocal(IProject.DEPTH_INFINITE, monitor);
		} else if(!project.isOpen()) {
			project.open(monitor);
			System.out.println("("+ this.currentSize + "): *** Opening project");
		}
	}
	
	//Tearing down methods
	@After
	public void tearDown() throws Exception {
		System.out.println("("+ this.currentSize + "): +++ Finished the test of size " + this.currentSize + " -- MAX=" + MAX_SIZE);
		
		//Cleaning the memory
		this.project.close(new NullProgressMonitor());
		this.project = null;
		System.gc();
	}
	
	@AfterClass
	public static void tearDownAfterClass() {
		Timing.closeAll();
	}
	
	//Running methods
	@Test
	public void test() throws Exception{
		Assert.assertNotNull(this.project);
		
		Timing currentTime = Timing.getInstance("GetFilePerformance.csv");
					
		System.out.println("("+ this.currentSize + "): +++ Begginnig algorithm");
		
		currentTime.begginTiming(currentSize);
			project.members();
		currentTime.endTiming();
	}
}
