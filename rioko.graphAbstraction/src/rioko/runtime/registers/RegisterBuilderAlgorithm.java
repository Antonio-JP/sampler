package rioko.runtime.registers;

import java.util.ArrayList;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.algorithms.TrivialBuilderAlgorithm;
import rioko.runtime.registers.algorithm.AlgorithmComparator;
import rioko.runtime.registers.algorithm.SortedAlgorithmSet;
import rioko.utilities.Log;

public final class RegisterBuilderAlgorithm {
	private static final String ID_ALGORITHM_EXTENSION = "rioko.graphabstraction.abstractionAlgorithms";
	private static boolean registered = false;
	private static SortedAlgorithmSet register = new SortedAlgorithmSet();
	
	public static ArrayList<NestedBuilderAlgorithm> getRegisteredAlgorithms() {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		ArrayList<NestedBuilderAlgorithm> list = new ArrayList<>();
		
		for(NestedBuilderAlgorithm aux : register) {
			list.add(aux.copy());
		}
		
		return list;
	}
	
	public static Class<? extends NestedBuilderAlgorithm> getClassFromCanonicalName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedBuilderAlgorithm aux : register) {
			if(aux.getClass().getCanonicalName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}
	
	public static Class<? extends NestedBuilderAlgorithm> getClassFromSimpleName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedBuilderAlgorithm aux : register) {
			if(aux.getClass().getSimpleName().equals(name)) {
				return aux.getClass();
			}
		}
		
		return null;
	}

	public static Class<? extends NestedBuilderAlgorithm> getClassFromName(String name) {
		if(!registered) {
			GETREGISTER();
		
			registered = true;
		}
		
		for(NestedBuilderAlgorithm aux : register) {
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
				registerClass( (NestedBuilderAlgorithm) element.createExecutableExtension("class"), element.getAttribute("name"));
			}
		} catch (InvalidRegistryObjectException | CoreException e) {
			Log.exception(e);
		}
	}
	

	
	private static boolean registerClass(NestedBuilderAlgorithm builder, String name) {
		if(builder == null || name == null) {
			return false;
		}
		
		for(NestedBuilderAlgorithm aux : register) {
			if(aux.getClass().equals(builder.getClass())) {
				return false;
			}
		}
		
		builder.setAlgorithmName(name);
		register.add(builder);
		return true;
	}

	public static NestedBuilderAlgorithm getTrivialAlgorithm() throws NotRegisteredException {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		for(NestedBuilderAlgorithm alg : register) {
			if(alg instanceof TrivialBuilderAlgorithm) {
				return alg;
			}
		}
		
		throw new NotRegisteredException(TrivialBuilderAlgorithm.class, "");
	}

	public static void register(NestedBuilderAlgorithm algorithm) throws RegisterRuntimeException {
		if(!registered) {
			GETREGISTER();
			
			registered = true;
		}
		
		if(!algorithm.getClass().equals(NestedBuilderAlgorithm.class)) {
			throw new RegisterRuntimeException(algorithm.getClass(), NestedBuilderAlgorithm.class, "We just can register new basic algorithms");
		}
		
		register.add(algorithm);
	}
	
	public static void changeAlgorithmOrder(AlgorithmComparator comparator) {
		register.setComparator(comparator);
	}
}
