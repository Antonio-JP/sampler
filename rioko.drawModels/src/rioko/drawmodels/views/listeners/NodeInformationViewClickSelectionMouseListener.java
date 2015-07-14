//package rioko.drawmodels.views.listeners;
//
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.MouseListener;
//
//import rioko.drawmodels.editors.zesteditor.ZestEditor;
//import rioko.drawmodels.jface.ZestEditorStructuredSelection;
//import rioko.drawmodels.views.nodeInformation.NodeInformationView;
//
//public class NodeInformationViewClickSelectionMouseListener implements MouseListener {
//	
//	private ZestEditor controler;
//	private NodeInformationView view;
//	
//	public NodeInformationViewClickSelectionMouseListener(ZestEditor controler, NodeInformationView view)
//	{
//		this.controler = controler;
//		this.view = view;
//	}
//
//	@Override
//	public void mouseDoubleClick(MouseEvent me) {
//		//Nothing happens
//	}
//
//	@Override
//	public void mouseDown(MouseEvent me) {
//		if(me.button == 1) {
//			//Get the selected DataLine from the view
//			this.controler.setSelection(new ZestEditorStructuredSelection(this.view.getLineSelected(), this.view));
//		}
//	}
//
//	@Override
//	public void mouseUp(MouseEvent arg0) {
//		//Nothing happens
//	}
//
//}
