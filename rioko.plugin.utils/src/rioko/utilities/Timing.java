package rioko.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

import rioko.utilities.collections.ArrayMap;

public class Timing {
	
	private static String pathToFiles = "./tests/";

	private static HashMap<String, Timing> tFiles = new HashMap<>();

	//Attributes to take a measure of time
	private long tBeggin = -1, tEnd = -1;
	private PrintWriter file = null;
	private int size = -1;
	private String text = "";

	private boolean working = false;
	
	//Builders
	private Timing(String name) throws IOException{
		FileWriter file = new FileWriter(pathToFiles + name, true);
	 		
		this.file = new PrintWriter(file);
	}
	
	public static Timing getInstance(String name) throws IOException{
		Timing t = tFiles.get(name);
		
		if(t == null) {
			t = new Timing(name);
			
			tFiles.put(name,t);
		}
		
		return t;
	}
	
	public static void setPath(String pathToFiles) {
		Timing.pathToFiles = pathToFiles;
	}
	
	public void closeInstance() {
		if(file != null) {
			file.close();
		}
		
		tFiles.remove(this);
	}
	
	//Methods to take measures of time
	public synchronized void begginTiming(int size) {
		//We wait until the Timing object is free
		while(working) {
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				Log.exception(e);
			}
		}
		working = true;
		this.size = size;
		
		tBeggin = System.currentTimeMillis();
	}
	
	public synchronized void begginTiming(int size, String text) {
		//We wait until the Timing object is free
		while(working) {
			try {
				this.wait(100);
			} catch (InterruptedException e) {
				Log.exception(e);
			}
		}
		
		//We set up the variables for the execution
		working = true;
		this.size = size;
		this.text = text;
		
		//We beggin to count the time
		tBeggin = System.currentTimeMillis();
	}
	
	public synchronized void endTiming() {
		//We stop to counting time
		tEnd = System.currentTimeMillis();
		
		if(working) {	//We check if the Timing object was opened
			long totalTime = tEnd - tBeggin;	//Calculate the total execution time
			this.file.println(text + size + ";" + totalTime);	//Print in the test file
			this.file.flush();
			
			//We clean all the variebales associated to this Timing object
			this.clear();
		}
	}
	
	public void compactFile() {
//		Log.print("Buscando fichero");
		String name = this.keyOf(this);
		
		if(name != null) {
			try {
				File compact = new File(pathToFiles + "__compact_"+name);
				
				if(!compact.exists()) {
					compact.createNewFile();
				}
				
				PrintWriter writer = new PrintWriter(compact);
				//Read the real file
				FileReader reader = new FileReader(pathToFiles + name);
				BufferedReader buff = new BufferedReader(reader);
				String line = buff.readLine();
				ArrayMap<Integer, ArrayList<Integer>> map = new ArrayMap<>();
				while(line != null) {
					String[] parts = line.split(";");
					if(parts.length != 2) {
						continue;
					}
					
					int size = Integer.parseInt(parts[0]);
					int time = Integer.parseInt(parts[1]);
					
					if(!map.containsKey(size)) {
						map.put(size, new ArrayList<Integer>());
					}
					map.get(size).add(time);
					
					line = buff.readLine();
				}
				
				//We get the means of execution
				ArrayMap<Integer, Double> realTime = new ArrayMap<>();
				for(Integer size : map.keySet()) {
					double totalTime = 0;
					for(Integer time : map.get(size)) {
						totalTime += time;
					}
					
					realTime.put(size, totalTime/map.get(size).size());
				}
				//We print the new File Information
				for(Integer size : realTime.keySet()) {
					writer.println((size + ";" + realTime.get(size)).replaceAll("\\.", ","));
				}
				
				//We close everything
				buff.close();
				reader.close();
				writer.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}

	//Private methods
	private void clear() {
		size = -1;
		text = "";
		working = false;
	}

	private String keyOf(Timing timing) {
		for(String name : tFiles.keySet()) {
			if(tFiles.get(name).equals(timing)) {
				return name;
			}
		}
		
		return null;
	}
	
	//Static auxiliary methods (used to not create the Timing objects in the code)
	public static void closeAll() {
		for(String name : tFiles.keySet()) {
			Timing t = tFiles.get(name);
			t.compactFile();
			t.closeInstance();
		}
	}
	
	public static void compactAll() {
		for(String name : tFiles.keySet()) {
			Timing t = tFiles.get(name);
			t.compactFile();
		}
	}
	
	public static void tBeggin(String name, int size) throws IOException {
		Timing t = Timing.getInstance(name);
		
		t.begginTiming(size);
	}
	
	public static void tBeggin(String name, int size, String text) throws IOException {
		Timing t = Timing.getInstance(name);
		
		t.begginTiming(size, text);
	}
	
	public static void tEnd(String name) throws IOException {
		Timing t = Timing.getInstance(name);
		
		t.endTiming();
	}
	
	public static void tClose(String name) throws IOException {
		Timing t = Timing.getInstance(name);
		
		t.closeInstance();
	}
	
	public static void tCompact(String name) throws IOException {
		Timing t = Timing.getInstance(name);
		
		t.compactFile();
	}
}
