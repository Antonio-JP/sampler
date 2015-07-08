package rioko.plugin.utils;

import java.io.File;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "rioko.plugin.utils"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}
	
	/* Static methods to manage the plug-in names and folders */	
	public static File getMetadataFolder()  {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		
		File metadata = new File(workspace.getRoot().getLocation().toOSString() + "\\.metadata");
		if(!metadata.exists()) {
			metadata.mkdir();
		}
		
		File plugins = new File(metadata.getAbsolutePath() + "\\.plugins");
		if(!plugins.exists()) {
			plugins.mkdir();
		}
		
		File pluginFolder = new File(plugins.getAbsolutePath() + "\\" + PLUGIN_ID);
		if(!pluginFolder.exists()) {
			pluginFolder.mkdir();
		}
		
		return pluginFolder;
	}
}
