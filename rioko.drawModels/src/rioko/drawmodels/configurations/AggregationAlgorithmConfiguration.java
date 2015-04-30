package rioko.drawmodels.configurations;

import java.util.ArrayList;
import java.util.Collection;

import rioko.graphabstraction.algorithms.NestedBuilderAlgorithm;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;
import rioko.runtime.registers.RegisterBuilderAlgorithm;

public class AggregationAlgorithmConfiguration implements ComboConfiguration {

	private ArrayList<NestedBuilderAlgorithm> algorithms = new ArrayList<>();
	
	private NestedBuilderAlgorithm currentAlgorithm = null;
	
	public AggregationAlgorithmConfiguration() {
		algorithms.addAll(RegisterBuilderAlgorithm.getRegisteredAlgorithms());
		
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
		if(value instanceof NestedBuilderAlgorithm) {
			if(this.contains((NestedBuilderAlgorithm) value)) {
				this.currentAlgorithm = (NestedBuilderAlgorithm) value;
			}
		} else if(value instanceof String) {
			NestedBuilderAlgorithm alg = this.getAlgorithm((String)value);
			
			if(alg == null) {
				throw new BadArgumentException(NestedBuilderAlgorithm.class, value.getClass(), "No aggregation algorithm given");
			} else {
				this.currentAlgorithm = alg;
			}
		} else {
			throw new BadArgumentException(NestedBuilderAlgorithm.class, value.getClass(), "No aggregation algorithm given");
		}
	}

	@Override
	public NestedBuilderAlgorithm getConfiguration() {
		return this.currentAlgorithm;
	}

	@Override
	public String getTextConfiguration() {
		return this.currentAlgorithm.getAlgorithmName();
	}

	@Override
	public Collection<NestedBuilderAlgorithm> getPossibleOptions() {
		return this.algorithms;
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		ArrayList<String> names = new ArrayList<>();
		
		for(NestedBuilderAlgorithm alg : this.getPossibleOptions()) {
			names.add(alg.getAlgorithmName());
		}
		
		return names;
	}

	@Override
	public Class<NestedBuilderAlgorithm> classOfOptions() {
		return NestedBuilderAlgorithm.class;
	}

	public boolean add(NestedBuilderAlgorithm algorithm) {
		if(!this.contains(algorithm)) {
			return this.algorithms.add(algorithm);
		} else {
			return false;
		}
	}

	//Private methods
	private boolean contains(NestedBuilderAlgorithm value) {
		return this.getAlgorithm(value.getAlgorithmName()) != null;
	}
	
	private NestedBuilderAlgorithm getAlgorithm(String value) {
		for(NestedBuilderAlgorithm alg : this.algorithms) {
			if(alg.getAlgorithmName().equals(value)) {
				return alg;
			}
		}
		
		return null;
	}

	@Override
	public AggregationAlgorithmConfiguration copy() {
		AggregationAlgorithmConfiguration res = new AggregationAlgorithmConfiguration();
		
		res.currentAlgorithm = res.algorithms.get(this.algorithms.indexOf(currentAlgorithm));
		
		return res;
	}
}
