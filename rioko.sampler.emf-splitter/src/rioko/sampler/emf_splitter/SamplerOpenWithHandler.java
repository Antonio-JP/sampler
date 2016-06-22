package rioko.sampler.emf_splitter;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.mondo.generate.modular.project.ext.ModularHandler;

import rioko.drawmodels.handlers.extensions.OpenFile;

public class SamplerOpenWithHandler extends ModularHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			(new OpenFile()).openEditorWithFile(this.getIFile(event), event);
			return null;
		} catch (Exception e) {
			throw new ExecutionException("Error opening the SAMPLER Editor", e);
		}
	}

}
