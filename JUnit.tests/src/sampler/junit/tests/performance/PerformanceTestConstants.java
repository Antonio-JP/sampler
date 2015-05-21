package sampler.junit.tests.performance;

class PerformanceTestConstants {
	
	//File management
	public static final String PATH_TO_RESULTS = "C:\\Users\\All Users\\test_results\\";
	public static final String FILE_EXTENSION = ".csv";
	
	public static final String OPEN_PREFIX = "__opening_file";
	public static final String RUN_PREFIX = "__run_";
	
	//Project Management
	public static final String PROJECT_NAME = "Testing_Sampler";
	public static final String FOLDER_NAME = "tests";

	//Execution Management
	public static final int MIN_SIZE = 100;
	public static final int MAX_SIZE = 1000;
	public static final int STEP_SIZE = 100;
	public static final int NUM_MODELS = 1;
	
	public static final int NUM_REPEATS = 5;
	
	public static int nextSize(int size) {
		if(size < 1000) {
			return size + 100;
		} else if(size < 10000) {
			return size + 500;
		} else {
			return size+1000;
		}
	}

}
