package rioko.utilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public final class Log {
	private static PrintStream xmlLog = null;
	private static PrintStream exceptionLog = null;
	
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	private static int sLogNum = 1;
	private static int xLogNum = 1;
	private static int eLogNum = 1;
	
	private static final String initial = "Rioko LOG";
	
	private static boolean isClose = true;
	
	private static PrintStream getXMLFile() {
		return xmlLog;
	}
	
	private static PrintStream getXMLFile(String path) {
		if(xmlLog == null) {
			//Abrir el fichero
			try{
				File file = new File(path);
				 
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				
				xmlLog = new PrintStream(file);
				Log.xOpen("?xml version=\"1.0\" encoding=\"UTF-8\"?");
			} catch(IOException e) {
				Log.sPrint(e.getMessage());
			}
		}

		return xmlLog;
	}
	
	private static PrintStream getExceptionFile() {
		return exceptionLog;
	}
	
	private static PrintStream getExceptionFile(String path) {
		if(exceptionLog == null) {
			//Abrir el fichero
			try{
				File file = new File(path);
				 
				// if file doesnt exists, then create it
				if (!file.exists()) {
					file.createNewFile();
				}
				
				exceptionLog = new PrintStream(file);
				Log.purePrint(exceptionLog, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			} catch(IOException e) {
				Log.sPrint(e.getMessage());
			}
		}

		return exceptionLog;
	}
	
	public static void open() {
		open("./xmlLog.log", "./exceptionLog.log");
	}
	
	public static void open(String XMLpath, String ExceptionPath) {
		Log.getXMLFile(XMLpath);
		Log.getExceptionFile(ExceptionPath);
		
		Log.isClose = false;
		Log.xOpen("exe");
		
		Log.purePrint(getExceptionFile(), "<exe>");
	}
	
	public static void close() {	
		Log.xClose("exe");
		Log.purePrint(getExceptionFile(), "</exe>");
		Log.isClose = true;
	}

	public static void sPrint(String string) {
		Log.print(System.out, string, sLogNum);
		sLogNum++;
	}
	
	public static void xPrint(String string) {
		Log.print(getXMLFile(), string, xLogNum);
		xLogNum++;
	}
	
	public static void xOpen(String label) {
		Log.purePrint(getXMLFile(), "<" + label + ">");
	}
	public static void xClose(String label) {
		Log.purePrint(getXMLFile(), "</" + label + ">");
	}

	public static void exception(Exception e) {
		Log.print(getExceptionFile(), "Exception " + e.getClass().getSimpleName() + " thrown", eLogNum);
		eLogNum++;
		
		Log.purePrint(getExceptionFile(), "<exception>");
		e.printStackTrace(getExceptionFile());
		Log.purePrint(getExceptionFile(), "</exception>");
		
		Log.sPrint("Exception " + e.getClass().getSimpleName() + " thrown");
	}

	public static void print(String string) {
		Log.sPrint(string);
		Log.xPrint(string);
	}
	
	private static void print(PrintStream out, String string, int logNum) {
		Log.purePrint(out, initial + "(" + logNum + " - " + dateFormat.format(Calendar.getInstance().getTime()) +  "): " + string);
		out.flush();
	}
	
	private static void purePrint(PrintStream out, String string) {
		if(!Log.isClose) {
			out.println(string);
		}
	}
}
