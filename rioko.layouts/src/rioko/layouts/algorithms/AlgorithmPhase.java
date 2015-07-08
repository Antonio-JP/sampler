package rioko.layouts.algorithms;

public enum AlgorithmPhase {
	OFF, PREPARATION, RUNNING, ADJUST; 
	
	public static AlgorithmPhase getPhase(int phase) {
		switch(phase) {
		case 0:
			return AlgorithmPhase.PREPARATION;
		case 1:
			return AlgorithmPhase.RUNNING;
		case 2:
			return AlgorithmPhase.ADJUST;
		default:
			return AlgorithmPhase.OFF;
		}
	}
}
