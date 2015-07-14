//package rioko.drawmodels.views.listeners;
//
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.events.MouseListener;
//
//import rioko.drawmodels.editors.zesteditor.ZestEditor;
//import rioko.drawmodels.views.nodeInformation.AggregateNodeInformationDataLine;
//import rioko.drawmodels.views.nodeInformation.NodeInformationView;
//
//public class NodeInformationViewNavigationMouseListener implements MouseListener {
//
//	private ZestEditor controler;
//	private NodeInformationView view;
//	
//	public NodeInformationViewNavigationMouseListener(ZestEditor controler, NodeInformationView view)
//	{
//		this.controler = controler;
//		this.view = view;
//	}
//	
//	@Override
//	public void mouseDoubleClick(MouseEvent me) {
//		if(me.button == 1) {
//			AggregateNodeInformationDataLine line = view.getLineSelected();
//			this.controler.changeRoot(line.getLabel() + ": " + line.getData());
//		}
//	}
//
//	@Override
//	public void mouseDown(MouseEvent arg0) {
//		//Nothing happens
//	}
//
//	@Override
//	public void mouseUp(MouseEvent arg0) {
//		//Nothing happens
//	}
//
//}
