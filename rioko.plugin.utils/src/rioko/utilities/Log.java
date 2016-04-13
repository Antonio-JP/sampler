package rioko.utilities;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import rioko.plugin.utils.Activator;

/**
 * Static Log class to have an structured and readable text from an execution.
 * 
 * @author Antonio
 */
public final class Log {
	/**
	 * Stream where save the XML Log
	 */
	private static PrintStream xmlLog = null;
	/**
	 * Stream where save the Exceptions Log
	 */
	private static PrintStream exceptionLog = null;
	
	/**
	 * Field with the standard date format used to mark the log entries.
	 */
	private static DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
	
	/**
	 * Field to know how mane messages have been printed in System terminal
	 */
	private static int sLogNum = 1;
	/**
	 * Field to know how mane messages have been printed in XML Log
	 */
	private static int xLogNum = 1;
	/**
	 * Field to know how mane messages have been printed in Exception Log
	 */
	private static int eLogNum = 1;
	
	/**
	 * Field to have the basic text to print in the System Terminal and recognize 
	 * the messages of this Log.
	 */
	private static final String initial = "Rioko LOG";
	
	/**
	 * Flag to know if the Log is closed.
	 */
	private static boolean isClose = true;
	
	/**
	 * Flag to know if the Log has been activated
	 */
	public static boolean isReady = false;
	
	/**
	 * Private method to get the XML Log File.
	 * 
	 * @return Stream with the XML Log.
	 */
	private static PrintStream getXMLFile() {
		return xmlLog;
	}
	
	/**
	 * Private method to get the XML Log File. It creates the file in the path given if it does not exist.
	 * 
	 * @param path Path where create the XML Log.
	 * 
	 * @return Stream with the XML Log.
	 */
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
	
	/**
	 * Private method to get the Exception Log File.
	 * 
	 * @returnStream with the Exception Log
	 */
	private static PrintStream getExceptionFile() {
		return exceptionLog;
	}
	
	/**
	 * Private method to get the Excception Log File. It creates the file in the path given if it 
	 * does not exist.
	 * 
	 * @param path Path where create the XML Log.
	 * 
	 * @return Stream with the Exception Log.
	 */
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
	
	/**
	 * Open the Log files. Use the .metadata/plugins/ folder to save them creating a new folder for 
	 * this plugin if it does not exists.
	 */
	public static void open() {
		File folder = Activator.getMetadataFolder();
		
		String basePath = folder.getAbsolutePath();
		
		open(basePath+"\\xmlLog.log", basePath+"\\exceptionLog.log");
		
		Log.isReady = true;
	}
	
	/**
	 * OPen the Log files using the given paths to save them.
	 * 
	 * @param XMLpath Path where save the XML Log File
	 * @param ExceptionPath Path where save the Exception Log File
	 */
	public static void open(String XMLpath, String ExceptionPath) {
		Log.getXMLFile(XMLpath);
		Log.getExceptionFile(ExceptionPath);
		
		Log.isClose = false;
		Log.xOpen("exe");
		
		Log.purePrint(getExceptionFile(), "<exe>");
	}
	
	/**
	 * Method to close the Log system.
	 */
	public static void close() {	
		if(Log.isReady) {
			Log.xClose("exe");
			Log.purePrint(getExceptionFile(), "</exe>");
			Log.isClose = true;
		}
	}

	/**
	 * Method to print a message in the System Terminal
	 * @param string Message to print.
	 */
	public static void sPrint(String string) {
		if(Log.isReady) {
			Log.print(System.out, string, sLogNum);
			sLogNum++;
		}
	}
	
	/**
	 * Method to print a message in the XML Log File
	 * @param string Message to print
	 */
	public static void xPrint(String string) {
		if(Log.isReady) {
			Log.print(getXMLFile(), string, xLogNum);
			xLogNum++;
		}
	}
	
	//XML File management
	/**
	 * Method that open a new node of the XML Log file.
	 * 
	 * @param label Name of the node to be opened.
	 */
	public static void xOpen(String label) {
		if(Log.isReady) {
			Log.purePrint(getXMLFile(), "<" + label + ">");
		}
	}
	
	/**
	 * Method that close a node of the XML Log file.
	 * 
	 * @param label Name of the node to be closed.
	 */
	public static void xClose(String label) {
		if(Log.isReady) {
			Log.purePrint(getXMLFile(), "</" + label + ">");
		}
	}

	/**
	 * Method that prints in the Exception Log File the Stack Trace of an Exception.
	 * 
	 * @param e Exception to be printed.
	 */
	public static void exception(Exception e) {
		if(Log.isReady) {
			Log.print(getExceptionFile(), "Exception " + e.getClass().getSimpleName() + " thrown", eLogNum);
			eLogNum++;
			
			Log.purePrint(getExceptionFile(), "<exception>");
			e.printStackTrace(getExceptionFile());
			Log.purePrint(getExceptionFile(), "</exception>");
			
			Log.sPrint("Exception " + e.getClass().getSimpleName() + " thrown");
		}
	}

	//Printing methods
	/**
	 * Method to print a message in the Log System. This generic method prints the message in both Terminal and XML Logs, using the methods
	 * {@link rioko.utilities.Log#sPrint(String) sPrint(String)} and {@link rioko.utilities.Log#xPrint(String) xPrint(String)}.
	 * 
	 * @param string Message to print in the Log system.
	 */
	public static void print(String string) {
		Log.sPrint(string);
		Log.xPrint(string);
	}
	
	/**
	 * Method to print with format a message in a PrintStream of this Log system.
	 * 
	 * @param out PrintStream where print the message
	 * @param string Message that will be printed
	 * @param logNum Number of messages that have been printed in this Stream before this one.
	 */
	private static void print(PrintStream out, String string, int logNum) {
		if(Log.isReady) {
			Log.purePrint(out, initial + "(" + logNum + " - " + dateFormat.format(Calendar.getInstance().getTime()) +  "): " + string);
			out.flush();
		}
	}
	
	/**
	 * Print in the Stream given the string provided.
	 * 
	 * @param out PrintStream where print the message.
	 * @param string Message to print in the Stream.
	 */
	private static void purePrint(PrintStream out, String string) {
		if(!Log.isClose && Log.isReady) {
			out.println(string);
		}
	}
}
