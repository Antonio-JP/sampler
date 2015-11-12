package rioko.revent;

public interface RListener<E extends REvent> {
	public Class<? extends E> getClassForListener();
	public boolean checkedEvent(E event);
	public void run(E event);
	public default void listenEvent(REvent rEvent) {
		if(this.getClassForListener().isInstance(rEvent)) {
			@SuppressWarnings("unchecked")
			E event = (E) rEvent;
			if(this.checkedEvent(event)) {
				this.run(event);
			}
		}
		
	}
}
