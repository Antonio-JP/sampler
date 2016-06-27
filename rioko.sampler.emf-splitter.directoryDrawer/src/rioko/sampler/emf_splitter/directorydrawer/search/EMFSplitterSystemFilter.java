package rioko.sampler.emf_splitter.directorydrawer.search;

public class EMFSplitterSystemFilter extends FileNameRegexFilter {
	private static final String SYSTEM_FILES_REGEX = "((\\..*)|(.*\\.aird))";
	
	public EMFSplitterSystemFilter() {
		super(SYSTEM_FILES_REGEX);
	}
}
