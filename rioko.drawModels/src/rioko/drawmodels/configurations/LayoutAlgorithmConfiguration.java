package rioko.drawmodels.configurations;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.zest.layouts.LayoutAlgorithm;

import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;
import rioko.runtime.registers.RegisterLayoutAlgorithm;
import rioko.zest.layout.ZestLayoutAlgorithm;

public class LayoutAlgorithmConfiguration implements ComboConfiguration {

	private ArrayList<ZestLayoutAlgorithm> algorithms = new ArrayList<>();
	
	private ZestLayoutAlgorithm currentAlgorithm = null;
	
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
			if(this.contains((ZestLayoutAlgorithm) value)) {
				this.currentAlgorithm = (ZestLayoutAlgorithm) value;
			}
		} else if(value instanceof String) {
			ZestLayoutAlgorithm alg = this.getAlgorithm((String)value);
			
			if(alg == null) {
				throw new BadArgumentException(ZestLayoutAlgorithm.class, value.getClass(), "No aggregation algorithm given");
			} else {
				this.currentAlgorithm = alg;
			}
		} else {
			throw new BadArgumentException(LayoutAlgorithm.class, value.getClass(), "No aggregation algorithm given");
		}
	}

	@Override
	public ZestLayoutAlgorithm getConfiguration() {
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
	public Collection<ZestLayoutAlgorithm> getPossibleOptions() {
		return this.algorithms;
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		ArrayList<String> names = new ArrayList<>();
		
		for(ZestLayoutAlgorithm alg : this.algorithms) {
			names.add(alg.getName());
		}
		
		return names;
	}

	@Override
	public Class<LayoutAlgorithm> classOfOptions() {
		return LayoutAlgorithm.class;
	}
	
	//Private methods
	private boolean contains(ZestLayoutAlgorithm value) {
		return this.getAlgorithm(value.getName()) != null;
	}
	
	private ZestLayoutAlgorithm getAlgorithm(String value) {
		for(ZestLayoutAlgorithm alg : this.algorithms) {
			if(alg.getName().equals(value)) {
				return alg;
			}
		}
		
		return null;
	}

	@Override
	public LayoutAlgorithmConfiguration copy() {
		LayoutAlgorithmConfiguration res = new LayoutAlgorithmConfiguration();
		
		res.currentAlgorithm = res.algorithms.get(this.algorithms.indexOf(this.currentAlgorithm));
		return res;
	}

}
