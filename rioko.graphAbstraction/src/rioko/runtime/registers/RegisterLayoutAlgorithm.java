package rioko.runtime.registers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.utilities.Log;
import rioko.zest.layout.ZestLayoutAlgorithm;

public final class RegisterLayoutAlgorithm {

	private static final String ID_ALGORITHM_EXTENSION = "rioko.graphabstraction.layoutAlgorithms";
	private static boolean registered = false;
	private static ArrayList<ZestLayoutAlgorithm> register = new ArrayList<>();
	
	public static ArrayList<ZestLayoutAlgorithm> getRegisteredAlgorithms() {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<ZestLayoutAlgorithm> list = new ArrayList<>();
		
		for(ZestLayoutAlgorithm aux : register) {
			list.add(aux.copy());
		}
		
		return list;
	}
	
	public static Class<? extends ZestLayoutAlgorithm> getClassFromCanonicalName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(ZestLayoutAlgorithm aux : register) {
			if(aux.getClass().getCanonicalName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	public static Class<? extends ZestLayoutAlgorithm> getClassFromSimpleName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(ZestLayoutAlgorithm aux : register) {
			if(aux.getClass().getSimpleName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}

	public static Class<? extends ZestLayoutAlgorithm> getClassFromName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(ZestLayoutAlgorithm aux : register) {
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
				registerClass( (ZestLayoutAlgorithm) element.createExecutableExtension("class"), element.getAttribute("name"));
			}
		} catch (InvalidRegistryObjectException | CoreException e) {
			Log.exception(e);
		}
	}
	

	
	private static boolean registerClass(ZestLayoutAlgorithm builder, String name) {
		if(builder == null || name == null) {
			return false;
		}
		
		for(ZestLayoutAlgorithm aux : register) {
			if(aux.getClass().equals(builder.getClass())) {
				return false;
			}
		}
		
		builder.setName(name);
		register.add(builder);
		return true;
	}

	public static void register(ZestLayoutAlgorithm algorithm) throws RegisterRuntimeException {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		if(!algorithm.getClass().equals(NestedBuilderAlgorithm.class)) {
			throw new RegisterRuntimeException(algorithm.getClass(), NestedBuilderAlgorithm.class, "We just can register new basic algorithms");
		}
		
		register.add(algorithm);
	}
}
