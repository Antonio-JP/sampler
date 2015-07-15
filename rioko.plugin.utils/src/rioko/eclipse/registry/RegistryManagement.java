package rioko.eclipse.registry;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.InvalidRegistryObjectException;
import org.eclipse.core.runtime.Platform;

public class RegistryManagement {	
	//Methods to get and check elements for Extension ID
	/**
	 * Generic method to get the Eclipse Registry Elements given a extension id.
	 * 
	 * @param extensionId Name of the extension we want to get.
	 * 
	 * @return an Array of IConfigurationElement with the elements required
	 */
	public static IConfigurationElement[] getElementsFor(String extensionId) {
		IExtensionRegistry registry = Platform.getExtensionRegistry();
		
		return registry.getConfigurationElementsFor(extensionId);
	}
	
	/**
	 * Method to know if exist some elements for an extension point id.
	 * 
	 * @param extensionId Name of the extension we want to check.
	 * 
	 * @return Boolean with true if there is an element in the registry with that extension ID
	 */
	public static Boolean existsId(String extensionId) {
		IConfigurationElement[] elements = getElementsFor(extensionId);
		
		return elements.length > 0;
	}
	
	//Methods to get the data from a IConfigurationRegistry
	/**
	 * Method to get the attribute of a Eclipse Registry Element. If it does not exist, returns 
	 * null. Avoid the exception thrown by the IConfigurationElement method.
	 * 
	 * @param element Eclipse Registry element to search
	 * @param attribute Name of the attribute we want to get
	 * 
	 * @return String associated with attribute in element or null if it does not exist
	 */
	public static String getAttribute(IConfigurationElement element, String attribute) {
		try {
			return element.getAttribute(attribute);
		} catch(InvalidRegistryObjectException e) {
			return null;
		}
	}
	
	/**
	 * Method to know if a element of the Eclipse Registry have an attribute defined.
	 * 
	 * @param element Element where we are going to look into
	 * @param attribute Name of the attribute we want to search
	 * 
	 * @return Boolean indicating if element exists in element
	 */
	public static Boolean existsAttribute(IConfigurationElement element, String attribute) {
		return getAttribute(element, attribute) != null;
	}
	
	// Methods to get instances of classes defined in an element
	/**
	 * Method to get the names of the attributes which are instanciable
	 * 
	 * @param element Eclipse Registry Element where we want to get the instanciable elements
	 * 
	 * @return A Collection with the names of the instanciable attributes
	 */
	public static Collection<String> getInstanciableElements(IConfigurationElement element) {
		ArrayList<String> instanciableNames = new ArrayList<>();
		for(String name : element.getAttributeNames()) {
			try {
				element.createExecutableExtension(name);
				instanciableNames.add(name);
			} catch (CoreException e) {
				//This attribute is not instanciable. We do nothing
			}
		}
		
		return instanciableNames;
	}

	
	/**
	 * Method to know if an element has any attribute instanciable
	 * 
	 * @param element Eclipse Registry Element where we want to check
	 * 
	 * @return Boolean with true if there is an instanciable attribute
	 */
	public static Boolean isInstanciable(IConfigurationElement element) {
		return (!getInstanciableElements(element).isEmpty());
	}
	
	/**
	 * Method to know if an attribute of an Eclipse Registry Element is instanciable
	 * 
	 * @param element Eclipse Registry Element where we want to check
	 * @param attributeName String with the name of the attribute we want to check
	 * 
	 * @return Boolean with true if the attributeName is instanciable for element
	 */
	public static Boolean isIntanciable(IConfigurationElement element, String attributeName) {
		return getInstanciableElements(element).contains(attributeName);
	}
	
	/**
	 * Method to get a new instance of the attribute. Return null if it is not instanciable.
	 * 
	 * @param element The Eclipse Registry Element we want to use
	 * @param attributeName String with the name of the attribute we want to instantiate.
	 * 
	 * @return Object with the new instance or null if attributeName is not instantiable in element
	 */
	public static Object getInstance(IConfigurationElement element, String attributeName) {
		if(isIntanciable(element, attributeName)) {
			try {
				return element.createExecutableExtension(attributeName);
			} catch (CoreException e) {
				// Impossible Exception because we have checked it before. So we do nothing
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get all the objects instantiables of an element of Eclipse Regsitry
	 * 
	 * @param element Eclipse Registry Element we want to fully instantiate
	 * 
	 * @return Collection of Object with the instances possible. It has no null elements and 
	 * can be an empty collection
	 */
	public static Collection<Object> getInstances(IConfigurationElement element) {
		ArrayList<Object> instances = new ArrayList<>();
		
		for(String name : getInstanciableElements(element)) {
			instances.add(getInstance(element, name));
		}
		
		return instances;
	}
	
	//Methods to get an instance of a determined class
	/**
	 * Method to get the first instance of a class from an element. It search throught the 
	 * instanciable elements and return the first element of the class provided.
	 * 
	 * It is possible give an abstract class and this method returns the first object that 
	 * extends that abstract class, i.e., this method does not use the constructor of the 
	 * class given.
	 * 
	 * @param element Eclipse Registry Element we want to use
	 * @param clazz Class we want to instantiate.
	 * 
	 * @return The instance of the object casted to the class given or null if there is no 
	 * instance of that class.
	 */
	public static <T> T getInstanceOf(IConfigurationElement element, Class<T> clazz) {
		for(Object obj : getInstances(element)) {
			if(clazz.isInstance(obj)) {
				@SuppressWarnings("unchecked")
				T casted = (T)obj;
				return casted;
			}
		}
		
		return null;
	}
	
	/**
	 * Method to know if there is any instantiable object of a certain class.
	 * 
	 * @param element Eclipse Registry Element we want to check
	 * @param clazz Class we want to search
	 * 
	 * @return Boolean with true if there is a instance of the class given
	 */
	public static <T> Boolean haveInstancesOf(IConfigurationElement element, Class<T> clazz) {
		return getInstanceOf(element, clazz) != null;
	}
	
	/**
	 * Method to get all the instances that extend a given class. 
	 * 
	 * @param element Eclipse Registry Element we want to use
	 * @param clazz Class we want to get
	 * 
	 * @return A Collection of Objects that extends clazz and are instantiated by element.
	 */
	public static <T> Collection<T> getInstancesOf(IConfigurationElement element, Class<T> clazz) {
		ArrayList<T> instances = new ArrayList<>();
		for(Object obj : getInstances(element)) {
			if(clazz.isInstance(obj)) {
				@SuppressWarnings("unchecked")
				T casted = (T)obj;
				instances.add(casted);
			}
		}
		
		return instances;
	}
	
	//Methods to search an instance in a extension point
	/**
	 * Method to get an instance of a class in an extension point using its class canonical name
	 * 
	 * @param extensionId Identifier of the extension point where we want to search
	 * @param className Canonical name of the class we want to instantiate
	 * 
	 * @return Object of the instance desired if exists and null otherwise
	 */
	public static Object getInstance(String extensionId, String className) {
		for(IConfigurationElement element : getElementsFor(extensionId)) {
			for(Object obj : getInstances(element)) {
				if(obj.getClass().getCanonicalName().equals(className)) {
					return obj;
				}
			}
		}
		
		return null;
	}
	
	/**
	 * Method to get all the instances of a certain class in an extension point.
	 * 
	 * @param extensionId Identifier of the extension point where we want to search
	 * @param className Canonical name of the class we want to instantiate
	 * 
	 * @return Collection of Objects that are instances of the desired class existing in the 
	 * extensionId provided
	 */
	public static Collection<Object> getInstances(String extensionId, String className) {
		ArrayList<Object> instances = new ArrayList<>();
		for(IConfigurationElement element : getElementsFor(extensionId)) {
			for(Object obj : getInstances(element)) {
				if(obj.getClass().getCanonicalName().equals(className)) {
					instances.add(obj);
				}
			}
		}
		
		return instances;
	}
	
	/**
	 * Method analogous to getInstance(String, String) but now recieves the Class object instead 
	 * its canonical name. This allows to return subclasses of the given class.
	 * 
	 * @param extensionId Identifier of the extension point where we want to search
	 * @param clazz Class we want to find
	 * 
	 * @return An object that extends the class given.
	 */
	public static <T> T getInstanceOf(String extensionId, Class<T> clazz) {
		for(IConfigurationElement element : getElementsFor(extensionId)) {
			T instance = getInstanceOf(element, clazz);
			if(instance != null) {
				return instance;
			}
		}
		
		return null;
	}
	
	/**
	 * Method that returns all the objects that are instantiable in an extension point and extend
	 * the class given.
	 * 
	 * @param extensionId Identifier of the extension point where we want to search
	 * @param clazz Class we want to find
	 * 
	 * @return Collection of Objects that extend the class given that are instantiable in the extensionId
	 */
	public static <T> Collection<T> getInstancesOf(String extensionId, Class<T> clazz) {
		ArrayList<T> instances = new ArrayList<>();
		for(IConfigurationElement element : getElementsFor(extensionId)) {
			instances.addAll(getInstancesOf(element, clazz));
		}
		
		return instances;
	}
}
