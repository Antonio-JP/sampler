package rioko.drawmodels.configurations;

import java.util.ArrayList;
import java.util.Collection;


import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;
import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.runtime.registers.RegisterLayoutAlgorithm;

public class LayoutAlgorithmConfiguration implements ComboConfiguration {

	private ArrayList<LayoutAlgorithm> algorithms = new ArrayList<>();
	
	private LayoutAlgorithm currentAlgorithm = null;
	
	public LayoutAlgorithmConfiguration() {
		algorithms.addAll(RegisterLayoutAlgorithm.getRegisteredAlgorithms());
		
		try {
			this.setValueConfiguration(algorithms.get(0));
		} catch (BadArgumentException | BadConfigurationException e) {
			// Impossible Exception
			e.printStackTrace();
		}
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.COMBO_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		if(value instanceof LayoutAlgorithm) {
			if(this.contains((LayoutAlgorithm) value)) {
				this.currentAlgorithm = (LayoutAlgorithm) value;
			}
		} else if(value instanceof String) {
			LayoutAlgorithm alg = this.getAlgorithm((String)value);
			
			if(alg == null) {
				throw new BadArgumentException(LayoutAlgorithm.class, value.getClass(), "No aggregation algorithm given");
			} else {
				this.currentAlgorithm = alg;
			}
		} else {
			throw new BadArgumentException(LayoutAlgorithm.class, value.getClass(), "No aggregation algorithm given");
		}
	}

	@Override
	public LayoutAlgorithm getConfiguration() {
		return this.currentAlgorithm;
	}

	@Override
	public String getTextConfiguration() {
		if(this.currentAlgorithm == null) {
			return "null";
		}
		
		return this.currentAlgorithm.getName();
	}

	@Override
	public Collection<LayoutAlgorithm> getPossibleOptions() {
		return this.algorithms;
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		ArrayList<String> names = new ArrayList<>();
		
		for(LayoutAlgorithm alg : this.algorithms) {
			names.add(alg.getName());
		}
		
		return names;
	}

	@Override
	public Class<LayoutAlgorithm> classOfOptions() {
		return LayoutAlgorithm.class;
	}
	
	//Private methods
	private boolean contains(LayoutAlgorithm value) {
		return this.getAlgorithm(value.getName()) != null;
	}

	private int indexOf(LayoutAlgorithm value) {
		return this.algorithms.indexOf(this.getAlgorithm(value.getName()));
	}

	
	private LayoutAlgorithm getAlgorithm(String value) {
		for(LayoutAlgorithm alg : this.algorithms) {
			if(alg.getName().equals(value)) {
				return alg;
			}
		}
		
		return null;
	}

	@Override
	public LayoutAlgorithmConfiguration copy() {
		LayoutAlgorithmConfiguration res = new LayoutAlgorithmConfiguration();
		
		res.currentAlgorithm = res.algorithms.get(this.indexOf(this.currentAlgorithm));
		return res;
	}
}
