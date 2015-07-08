package rioko.layouts.runtime.registers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.utilities.Log;

public final class RegisterLayoutAlgorithm {

	private static final String ID_ALGORITHM_EXTENSION = "rioko.layouts.layoutAlgorithms";
	private static boolean registered = false;
	private static ArrayList<LayoutAlgorithm> register = new ArrayList<>();
	
	public static ArrayList<LayoutAlgorithm> getRegisteredAlgorithms() {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<LayoutAlgorithm> list = new ArrayList<>();
		
		for(LayoutAlgorithm aux : register) {
			list.add(aux.copy());
		}
		
		return list;
	}
	
	public static Class<? extends LayoutAlgorithm> getClassFromCanonicalName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(LayoutAlgorithm aux : register) {
			if(aux.getClass().getCanonicalName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	public static Class<? extends LayoutAlgorithm> getClassFromSimpleName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(LayoutAlgorithm aux : register) {
			if(aux.getClass().getSimpleName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}

	public static Class<? extends LayoutAlgorithm> getClassFromName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(LayoutAlgorithm aux : register) {
			if(aux.getClass().getName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	private static void GETREGISTER() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(ID_ALGORITHM_EXTENSION);

		try {
			for(IConfigurationElement element : elements) {
				registerClass( (LayoutAlgorithm) element.createExecutableExtension("class"), element.getAttribute("name"));
			}
		} catch (InvalidRegistryObjectException | CoreException e) {
			Log.exception(e);
		}
	}
	

	
	private static boolean registerClass(LayoutAlgorithm builder, String name) {
		if(builder == null || name == null) {
			return false;
		}
		
		for(LayoutAlgorithm aux : register) {
			if(aux.getClass().equals(builder.getClass())) {
				return false;
			}
		}
		
		builder.setName(name);
		register.add(builder);
		return true;
	}
}
