package rioko.runtime.registers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import rioko.graphabstraction.display.NestedGraphBuilder;
import rioko.utilities.Log;

public final class RegisterBuilderSteps {
	
	private static final String ID_STEP_EXTENSION = "rioko.graphabstraction.steps";
	private static boolean registered = false;
	private static ArrayList<NestedGraphBuilder> register = new ArrayList<>();
	
	public static ArrayList<String> getRegisteredNames() {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(NestedGraphBuilder aux : register) {
			list.add(aux.getClass().getCanonicalName());
		}
		
		return list;
	}

	public static ArrayList<String> getRegisteredNames(Class<? extends NestedGraphBuilder> superclass) {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<String> list = new ArrayList<>();
		
		for(NestedGraphBuilder aux : register) {
			if(superclass.isInstance(aux)) {
				list.add(aux.getClass().getCanonicalName());
			}
		}
		
		return list;
	}
	
	public static Class<? extends NestedGraphBuilder> getClassFromCanonicalName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedGraphBuilder aux : register) {
			if(aux.getClass().getCanonicalName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	public static Class<? extends NestedGraphBuilder> getClassFromSimpleName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedGraphBuilder aux : register) {
			if(aux.getClass().getSimpleName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}

	public static Class<? extends NestedGraphBuilder> getClassFromName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedGraphBuilder aux : register) {
			if(aux.getClass().getName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	private static void GETREGISTER() {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		IConfigurationElement[] elements = registry.getConfigurationElementsFor(ID_STEP_EXTENSION);

		try {
			for(IConfigurationElement element : elements) {
				registerClass( (NestedGraphBuilder) element.createExecutableExtension("class"));
			}
		} catch (InvalidRegistryObjectException | CoreException e) {
			Log.exception(e);
		}
	}
	

	
	private static boolean registerClass(NestedGraphBuilder builder) {
		if(builder == null) {
			return false;
		}
		
		for(NestedGraphBuilder aux : register) {
			if(aux.getClass().equals(builder.getClass())) {
				return false;
			}
		}
		
		register.add(builder);
		return true;
	}
}
