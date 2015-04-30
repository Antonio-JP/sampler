package rioko.graphabstraction.algorithms;

import java.util.ArrayList;

public abstract class Algorithm {
	private static ArrayList<String> ListOfNames = new ArrayList<>();
	
	private String algorithmName = "";
	
	//Builders
	public Algorithm() {
		this.setAlgorithmName(this.getClass().getSimpleName());
	}
	
	public Algorithm(String algorithmName) {
		this.setAlgorithmName(algorithmName);
	}
	
	//Getters & Setters
	public void setAlgorithmName(String algorithmName) {
		this.setAlgorithmName(algorithmName, true);
	}
	
	protected boolean setAlgorithmName(String algorithmName, boolean unique) {
		if(algorithmName != null && !algorithmName.equals("")) {
			if(!this.algorithmName.equals("")) {
				ListOfNames.remove(this.algorithmName);
			}
			
			String name = algorithmName;
			
			if(unique) {
				if(existsName(name)) {
					int i = 1;
					while(existsName(name + " (" + i + ")")) {
						i++;
					}
					
					name += " (" + i + ")";
				}
			}
			
			this.algorithmName = name;
			
			if(!existsName(name)) {
				ListOfNames.add(name);
				return true;
			} else {
				return false;
			}
		}
		
		return false;
	}
	
	public String getAlgorithmName() {
		return this.algorithmName;
	}
	
	//Other methods
	public static boolean existsName(String name) {
		return ListOfNames.contains(name);
	}

	public static boolean removeName(String name) {
		if(existsName(name)) {
			ListOfNames.remove(name);
			return true;
		}
		
		return false;
	}
}
