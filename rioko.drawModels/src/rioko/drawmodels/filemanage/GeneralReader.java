package rioko.drawmodels.filemanage;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

/**
 * Static class with the methods needed to open a generic IFile with a Reader without know which Readers are implemented.
 * 
 * @author Antonio
 */
public class GeneralReader {
	
	private static final String ID_DIAGRAM_EXTENSION = "rioko.drawmodels.diagrams";
	
	private static HashMap<IFile, Reader<?>> mapFilesToReader = new HashMap<>();

	/**
	 * This method take an IFile and using its extension, search a good Reader class to use it.
	 * 
	 * @param file IFile we want to read
	 * 
	 * @return Reader with the reader of the file
	 * 
	 * @throws IOException If any error happens. The message gove more information of the error.
	 */
	public static Reader<?> getReaderFromFile(IFile file) throws IOException {
		//We check that the file is not already opened
		Reader<?> auxReader = mapFilesToReader.get(file);
		if(auxReader != null) {
			//If it is, we return the current Reader
			return auxReader;
		}
		
		//Else, we search in the Eclipse Registry the Diagram extensions
		IConfigurationElement[] elements = getDiagramsExtensions();
		ArrayList<IConfigurationElement> possibleReaders = new ArrayList<>();
		
		//We search the correct readers for the file extension
		for(IConfigurationElement element : elements) {
			for(IConfigurationElement fileExtension : element.getChildren("fileExtension")) {
				if(fileExtension.getAttribute("fileExtension").equals(file.getFileExtension())) {
					//We have found a correct reader for this file
					possibleReaders.add(element);
					break;
				}
			}
		}
		
		//If there is no valid reader, throw an exception
		if(possibleReaders.isEmpty()) {
			throw new IOException("There is no possible reader registered for extension " + file.getFileExtension());
		} else {
			//Else, there is a valid reader for the file. We use the first in the list
			try {
				Reader<?> fooReader = (Reader<?>) possibleReaders.get(0).createExecutableExtension("reader");
				//We use the Constructor with the IFile parameter and throw and exception if it does not exist
				Reader<?> reader = fooReader.getClass().getConstructor(IFile.class).newInstance(file);
				mapFilesToReader.put(file, reader);
				return reader;
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException | NoSuchMethodException | SecurityException | CoreException e) {
				throw new IOException("Reader " + possibleReaders.get(0).getAttribute("reader") + " not have a simple IFile Constructor or a empty Constructor");
			}
		}
	}

	/**
	 * This method takes an IFile and a Reader class and try to create a new Reader of that class for the IFile provided.
	 *  
	 * @param <T> Class of the Reader we want to create to that file.
	 *  
	 * @param file The file we want to read
	 * @param readerClass The Reader class we want to use
	 * 
	 * @return A Reader of the class given for the file parameter. If it was previously opened, return that. Else return a new Reader.
	 * 
	 * @throws IOException If any error happens. The message gove more information of the error.
	 */
	public static <T extends Reader<?>> T getReaderFromFile(IFile file, Class<T> readerClass) throws IOException {
		/* TODO: En esta función hay que mirar si la clase está en el registro de Reader y crear
		 * el lector correspondiente utilizando el IFile del parámetro */
		Reader<?> auxReader = mapFilesToReader.get(file);
		if(auxReader != null) {
			if(readerClass.isInstance(auxReader)) {
				@SuppressWarnings("unchecked")
				T finalReader = (T)auxReader;
				return finalReader;
			} else {
				throw new IOException("This file is currently opened with other Reader");
			}
		}
		
		IConfigurationElement[] elements = getDiagramsExtensions();
		
		for(IConfigurationElement element : elements) {
			if(element.getAttribute("reader").equals(readerClass.getCanonicalName())) {
				try {
					T reader = readerClass.getConstructor(IFile.class).newInstance(file);
					mapFilesToReader.put(file, reader);
					return reader;
				} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException | NoSuchMethodException | SecurityException e) {
					throw new IOException("Reader " + readerClass.getSimpleName() + " not have a simple IFile Constructor or file extension is not valid for that reader");
				}
			}
		}
		
		// If the reader is not registered, we throw an Exception
		throw new IOException("The Reader is not registered");
	}
	
	/**
	 * This method close an IFile and quit it from the map of opened readers. This let tho open a new Reader for that Resource.
	 *  
	 * @param file The IFile we want to close
	 */
	public static void closeFile(IFile file) {
		if(mapFilesToReader.containsKey(file)) {
			mapFilesToReader.remove(file);
		}
	}

	/**
	 * Auxiliar method to get the Eclipse Registry for the Diagram Extension Point
	 * @return
	 */
	private static IConfigurationElement[] getDiagramsExtensions() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		return registry.getConfigurationElementsFor(ID_DIAGRAM_EXTENSION);
	}
}
