package rioko.runtime.registers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import rioko.graphabstraction.diagram.filters.FilterOfVertex;
import rioko.utilities.Log;

public class RegisterSearchersCriteria {

	private static final String ID_SEARCHERS_EXTENSION = "rioko.graphabstraction.searches";
	private static boolean registered = false;
	private static ArrayList<FilterOfVertex> register = new ArrayList<>();
	
	public static ArrayList<String> getRegisteredNames() {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(FilterOfVertex aux : register) {
			list.add(aux.getClass().getCanonicalName());
		}
		
		return list;
	}

	public static ArrayList<String> getRegisteredNames(Class<? extends FilterOfVertex> superclass) {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(FilterOfVertex aux : register) {
			if(superclass.isInstance(aux)) {
				list.add(aux.getClass().getCanonicalName());
			}
		}
		
		return list;
	}
	
	public static Class<? extends FilterOfVertex> getClassFromCanonicalName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(FilterOfVertex aux : register) {
			if(aux.getClass().getCanonicalName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	public static Class<? extends FilterOfVertex> getClassFromSimpleName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(FilterOfVertex aux : register) {
			if(aux.getClass().getSimpleName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}

	public static Class<? extends FilterOfVertex> getClassFromName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(FilterOfVertex aux : register) {
			if(aux.getClass().getName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	private static void GETREGISTER() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(ID_SEARCHERS_EXTENSION);

		try {
			for(IConfigurationElement element : elements) {
				registerClass( (FilterOfVertex) element.createExecutableExtension("class"));
			}
		} catch (InvalidRegistryObjectException | CoreException e) {
			Log.exception(e);
		}
	}
	

	
	private static boolean registerClass(FilterOfVertex filter) {
		if(filter == null) {
			return false;
		}
		
		for(FilterOfVertex aux : register) {
			if(aux.getClass().equals(filter.getClass())) {
				return false;
			}
		}
		
		register.add(filter);
		return true;
	}

}
