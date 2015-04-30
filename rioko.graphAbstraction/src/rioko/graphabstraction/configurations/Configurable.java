package rioko.graphabstraction.configurations;

import java.util.ArrayList;
import java.util.Collection;

import rioko.utilities.Pair;

public interface Configurable {
	
	public Collection<Pair<String,Configuration>> getConfiguration();

	public default Object getConfiguration(String label) {
		for(Pair<String, Configuration> pair : this.getConfiguration()) {
			if(pair.getFirst() != null && pair.getFirst().equals(label)) {
				return pair.getLast().getConfiguration();
			}
		}
		
		return null;
	}
	
	public void setConfiguration(Collection<Configuration> newConf) throws BadConfigurationException, BadArgumentException;

	public default void setConfiguration(Configuration newConf) throws BadConfigurationException, BadArgumentException {
		Collection<Configuration> col = new ArrayList<Configuration>(1);
		col.add(newConf);
		
		this.setConfiguration(col);
	}
}
