package rioko.drawmodels.editors.listeners;

import rioko.drawmodels.editors.zesteditor.ZestEditor;
import rioko.zest.ExtendedGraphViewer;

public abstract class AbstractZestListener {
	private ZestEditor controller;
	private ExtendedGraphViewer viewer;
	
	//Builders
	public AbstractZestListener() {
		this.controller = null;
		this.viewer = null;
	}
	
	public AbstractZestListener(ZestEditor controller, ExtendedGraphViewer viewer) {
		this();
		
		this.setController(controller);
		this.setViewer(viewer);
	}
	
	//Getters & Setters
	public void setController(ZestEditor editor) {
		this.controller = editor;
	}
	
	public void setViewer(ExtendedGraphViewer viewer) {
		this.viewer = viewer;
	}
	
	protected ZestEditor getController() {
		if(this.controller == null) {
			throw new NullPointerException("This attributte has not been filled");
		}
		return this.controller;
	}
	
	protected ExtendedGraphViewer getViewer() {
		if(this.viewer == null) {
			throw new NullPointerException("This attributte has not been filled");
		}
		return this.viewer;
	}
	
	//Proper methods
	public final void addListenerToViewer() {
		if(this.checkValidViewer()) {
			this.doAddListener();
		}
	}
	
	protected abstract void doAddListener();
	
	protected boolean checkValidViewer() {
		return true;
	}
}
