package rioko.events.listeners;

import rioko.events.AbstractEvent;
import rioko.events.GlobalChangeEvent;

public abstract class GlobalChangeListener extends AbstractListener {

	public GlobalChangeListener(Object parent) {
		super(parent);
	}
	
	@Override
	protected void dispose() { /* Do nothing */ }

	@Override
	public Class<? extends AbstractEvent> getAssociatedEvent() {
		return GlobalChangeEvent.class;
	}

}
