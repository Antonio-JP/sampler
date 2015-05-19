package sampler.junit.tests.performance;

class BridgeTest {
	private static String iFilePath = "";
	private static int sizeTest = 0;
	
	public static void setFilePath(String iFilePath) {
		BridgeTest.iFilePath = iFilePath;
	}
	
	public static String getFilePath() {
		return BridgeTest.iFilePath;
	}
	
	public static void setSizeTest(int size) {
		if(size > 0) {
			BridgeTest.sizeTest = size;
		}
	}
	
	public static int getSizeTest() {
		return BridgeTest.sizeTest;
	}
}
