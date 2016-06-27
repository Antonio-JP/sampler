package rioko.sampler.emf_splitter.directorydrawer.algorithm;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.sampler.emf_splitter.directorydrawer.step.FilterEMFSplitterRepeated;
import rioko.sampler.emf_splitter.directorydrawer.step.FilterEMFSplitterSystem;

public class DirectoryEMFSplitterAlgorithm extends NestedBuilderAlgorithm {

	public DirectoryEMFSplitterAlgorithm() {
		this.addFilter(new FilterEMFSplitterRepeated());
		this.addFilter(new FilterEMFSplitterSystem());
	}
}
