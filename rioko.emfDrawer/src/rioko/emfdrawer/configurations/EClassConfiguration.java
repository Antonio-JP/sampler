package rioko.emfdrawer.configurations;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

import org.eclipse.emf.ecore.EClass;

import rioko.drawmodels.diagram.ModelDepending;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.emfdrawer.XMIModelDiagram;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.TypeOfConfiguration;

public class EClassConfiguration implements ModelDepending, ComboConfiguration {
	
	private XMIModelDiagram model;
	
	private EClass value = null;
	
	public EClassConfiguration() { }

	public EClassConfiguration(XMIModelDiagram model) throws BadConfigurationException {
		if(model == null) {
			throw new BadConfigurationException("No null model accepted");
		}
		
		this.model = model;
	}
	
	public EClassConfiguration(XMIModelDiagram model, EClass value) throws BadConfigurationException {
		this(model);
		
		try {
			this.setValueConfiguration(value);
		} catch (BadArgumentException e) {
			// Impossible exception
			e.printStackTrace();
		}
	}

	@Override
	public TypeOfConfiguration getType() {
		return TypeOfConfiguration.COMBO_CONFIGURATION;
	}

	@Override
	public void setValueConfiguration(Object value) throws BadArgumentException, BadConfigurationException {
		if(value == null) {
			this.value = null;
			return;
		}
		
		if(!this.hasModel()) {
			throw new BadConfigurationException("No model given");
		}
		
		EClass res = null;
		
		if(!(value instanceof EClass || value instanceof String)) {
			throw new BadArgumentException(EClass.class, value.getClass());
		} else if(value instanceof String) {
			Collection<Object> aux = this.getPossibleOptions();
			for(Object obj : aux) {
				if(((EClass)obj).getName().equals((String)value)) {
					res = (EClass)obj;
				}
			}
		} else {
			res = (EClass) value;
		}
		
		if(!this.model.getEClassList().contains(res)) {
			throw new BadConfigurationException("No valid EClass assigned");
		}
		
		this.value = res;
	}

	@Override
	public Object getConfiguration() {
		return this.value;
	}

	@Override
	public Collection<Object> getPossibleOptions() {
		Collection<Object> res = new HashSet<Object>();
		
		if(this.hasModel()) {
			for(EClass option : this.model.getEClassList()) {
				res.add(option);
			}
		}
		return res;
	}

	@Override
	public Collection<String> getPossibleOptionNames() {
		Collection<Object> aux = this.getPossibleOptions();
		
		Collection<String> res = new ArrayList<String>(aux.size());
		for(Object obj : aux) {
			EClass eClass = (EClass)obj;
			res.add(eClass.getName());
		}
		
		return res;
	}

	@Override
	public Class<?> classOfOptions() {
		return EClass.class;
	}

	@Override
	public String getTextConfiguration() {
		if(this.value == null) {
			return "";
		}
		
		return this.value.getName();
	}
	
	@Override
	public boolean isValid() {
		return (this.model == null) && (this.value == null);
	}

	//ModelDependingConfiguration methods
	@Override
	public void setModel(ModelDiagram<?> model) {
		if(model instanceof XMIModelDiagram) {
			this.model = (XMIModelDiagram)model;
		}
	}

	@Override
	public boolean hasModel() {
		return (this.model != null);
	}

	@Override
	public EClassConfiguration copy() {
		EClassConfiguration res = new EClassConfiguration();
		
		res.model = this.model;
		res.value = this.value;
		
		return res;
	}

	@Override
	public String getNameOfConfiguration() {
		return "EClass to Filter";
	}

}
