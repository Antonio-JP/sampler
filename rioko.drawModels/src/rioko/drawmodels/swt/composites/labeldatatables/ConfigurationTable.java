package rioko.drawmodels.swt.composites.labeldatatables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

import rioko.drawmodels.diagram.ModelDepending;
import rioko.drawmodels.diagram.ModelDiagram;
import rioko.drawmodels.jface.NodeCellEditor;
import rioko.drawmodels.swt.LabelValueDataLine;
import rioko.drawmodels.swt.composites.LabelDataTable;
import rioko.events.DataChangeEvent;
import rioko.graphabstraction.configurations.BadArgumentException;
import rioko.graphabstraction.configurations.BadConfigurationException;
import rioko.graphabstraction.configurations.ComboConfiguration;
import rioko.graphabstraction.configurations.Configurable;
import rioko.graphabstraction.configurations.Configuration;
import rioko.graphabstraction.configurations.DialogConfiguration;
import rioko.graphabstraction.diagram.DiagramNode;
import rioko.utilities.Log;
import rioko.utilities.Pair;

public class ConfigurationTable extends LabelDataTable {

	private ModelDiagram<?> model;
	
	private Configurable currentConfiguration;
	
	//Builders
	public ConfigurationTable(Composite parent, int style, ModelDiagram<?> model) {
		super(parent, style);
		
		this.model = model;
	}

	public ConfigurationTable(Composite parent, int style, String label, String data, ModelDiagram<?> model) {
		super(parent, style, label, data);
		
		this.model = model;
	}
	
	//Getters & Setters
	public void setModel(ModelDiagram<?> model) {
		this.model = model;
	}

	//Other methods
	public void setNewConfigurable(Configurable newConf) {
		//Cambiamos el contenido de la tabla
		this.updateTable(newConf);
		
		//Cambiamos el editor de la tabla
		this.setEditingSupport(new ConfigurationTableEdditingSupport(this.getValueViewer(), model, newConf, this));
		
		//Cambiamos el valor de currentConfiguration
		this.currentConfiguration = newConf;
	}
	
	protected void updateTable(Configurable conf) {
		ArrayList<LabelValueDataLine> lines = new ArrayList<>();
		
		for(Pair<String, Configuration> pair : conf.getConfiguration()) {
			if(pair.getLast() == null) {
				lines.add(new LabelValueDataLine(pair.getFirst(), ""));
			} else {
				lines.add(new LabelValueDataLine(pair.getFirst(), pair.getLast().getTextConfiguration()));
			}
		}

		this.setInput(lines.toArray(new LabelValueDataLine[0]));
	}
	
	private class ConfigurationTableEdditingSupport extends EditingSupport {
		
		private Collection<Pair<String,Configuration>> configurations;
		
		private HashMap<String, CellEditor> editors = new HashMap<>();

		private ConfigurationTable controlledTable;
		
		public ConfigurationTableEdditingSupport(ColumnViewer viewer, ModelDiagram<?> model, Configurable newConf, ConfigurationTable table) {
			super(viewer);

			this.configurations = newConf.getConfiguration();
			this.controlledTable = table;
			
			for(Pair<String, Configuration> pair: this.configurations) {
				if(model != null) {
					if(pair.getLast() instanceof ModelDepending) {
						if(!((ModelDepending) pair.getLast()).hasModel()) {
							((ModelDepending) pair.getLast()).setModel(model);
						}
					}
				}
				switch(pair.getLast().getType()) {
					case NUMBER_CONFIGURATION:
						this.editors.put(pair.getFirst(),new TextCellEditor((Composite) getViewer().getControl(), SWT.NONE));
						
						break;
					case COMBO_CONFIGURATION:
						ComboBoxViewerCellEditor comboCellEditor = new ComboBoxViewerCellEditor((Composite) getViewer().getControl(), SWT.READ_ONLY);
						comboCellEditor.setLabelProvider(new LabelProvider());
						comboCellEditor.setContentProvider(new ArrayContentProvider());
						comboCellEditor.setInput(((ComboConfiguration)pair.getLast()).getPossibleOptionNames());
						
						this.editors.put(pair.getFirst(), comboCellEditor);
						
						break;
					case TEXT_CONFIGURATION:
						this.editors.put(pair.getFirst(),new TextCellEditor((Composite) getViewer().getControl(), SWT.NONE));
						
						break;
					case DIALOG_CONFIGURATION:
						this.editors.put(pair.getFirst(),new NodeCellEditor((Composite) getViewer().getControl(), SWT.NONE, (DialogConfiguration) pair.getLast()));
						
						break;
					default:
						
						break;
				}
			}
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			if(element instanceof LabelValueDataLine) {
				LabelValueDataLine line = (LabelValueDataLine)element;
				
				for(Pair<String, Configuration> pair : this.configurations) {
					if(line.getLabel().equals(pair.getFirst())) {
						return this.editors.get(pair.getFirst());
					}
				}
			}
			
			return null;
		}

		@Override
		protected Object getValue(Object element) {
			if (element instanceof LabelValueDataLine) {
				LabelValueDataLine data = (LabelValueDataLine)element;
	            return data.getData();
	        }
	        return null;
		}

		@Override
		protected void setValue(Object element, Object value) {
			boolean change = false;
			if (element instanceof LabelValueDataLine && value instanceof String) {	//Caso de String
				
				LabelValueDataLine data = (LabelValueDataLine) element;
				String newValue = (String) value;
	            /* only set new value if it differs from old one */
	            if (!data.getData().equals(newValue)) {
	            	data.setData(newValue);
	            	//Seleccionamos la configuración cambiada
	            	Configuration conf= null;
	            	for(Pair<String, Configuration> pair : this.configurations) {
	            		if(pair.getFirst().equals(data.getLabel())) {
	            			conf = pair.getLast();
	            			break;
	            		}
	            	}
	            	
	            	if(conf != null) {
	            		try {
		            		switch(conf.getType()) {
								case COMBO_CONFIGURATION:
									conf.setConfiguration(newValue);
									change = true;
									break;
								case NUMBER_CONFIGURATION:
									conf.setConfiguration(Integer.parseInt(newValue));
									change = true;
									break;
								case TEXT_CONFIGURATION:
									conf.setConfiguration(newValue);
									change = true;
									break;
								case DIALOG_CONFIGURATION:
									conf.setConfiguration(newValue);
									change = true;
									break;
								default:
									break;
		            		}
						} catch (BadConfigurationException e) {
							Log.exception(e);
						} catch (BadArgumentException e) {
							Log.exception(e);
						} catch (NumberFormatException e) {
							//No hacemos nada
						}
	            	}
	            	
	            	//Actualizamos la tabla
	            	updateTable(currentConfiguration);
	            	new DataChangeEvent(this.controlledTable);
	            }
	        } else if(element instanceof LabelValueDataLine && value instanceof DiagramNode) {	//Caso de Diagramnode
	        	LabelValueDataLine data = (LabelValueDataLine) element;
				DiagramNode newValue = (DiagramNode) value;
	            /* only set new value if it differs from old one */
	            if (!data.getData().equals(newValue.getTitle())) {
	            	data.setData(newValue.getTitle());
	            	//Seleccionamos la configuración cambiada
	            	Configuration conf= null;
	            	for(Pair<String, Configuration> pair : this.configurations) {
	            		if(pair.getFirst().equals(data.getLabel())) {
	            			conf = pair.getLast();
	            			break;
	            		}
	            	}
	            	
	            	if(conf != null) {
	            		try {
		            		switch(conf.getType()) {
								case DIALOG_CONFIGURATION:
									conf.setConfiguration(newValue);
									change = true;
									break;
								default:
									break;
		            		}
						} catch (BadConfigurationException e) {
							Log.exception(e);
						} catch (BadArgumentException e) {
							Log.exception(e);
						} catch (NumberFormatException e) {
							//No hacemos nada
						}
	            	}
	            }
	        }
			
			if(change) {
        		try {
					Collection<Configuration> updatedConf = new ArrayList<Configuration>();
	        		for(Pair<String, Configuration> pair: this.configurations) {
	        			updatedConf.add(pair.getLast());
	        		}
	        		
					currentConfiguration.setConfiguration(updatedConf);
				} catch (BadConfigurationException | BadArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            	
            	//Actualizamos la tabla
            	updateTable(currentConfiguration);
            	new DataChangeEvent(this.controlledTable);
			}
		}		
	}
}
