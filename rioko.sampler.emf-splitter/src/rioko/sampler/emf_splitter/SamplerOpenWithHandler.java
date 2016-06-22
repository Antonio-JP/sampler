package rioko.sampler.emf_splitter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.mondo.generate.modular.project.ext.ModularHandler;

import rioko.drawmodels.handlers.extensions.OpenFile;

public class SamplerOpenWithHandler extends ModularHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IFile fil = this.getIFile(event);
			(new OpenFile()).openEditorWithFile(fil, event);
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ExecutionException("Error opening the SAMPLER Editor", e);
		}
	}

}
