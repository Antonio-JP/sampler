//package rioko.layouts.wrapper.zest;
//
//import java.util.Comparator;
//import java.util.List;
//
//public abstract class GeneralLayoutAlgorithm implements AuxZestLayoutAlgorithm{
//
//	private String name = this.getClass().getSimpleName();
//	
//	private LayoutAlgorithm algorithm;
//	
//	public GeneralLayoutAlgorithm() {
//		this.algorithm = this.getLayoutAlgorithm();
//	}
//	
//	public GeneralLayoutAlgorithm(int style) {
//		this.algorithm = this.getLayoutAlgorithm(style);
//	}
//	
//	//LayoutAlgorithm methods
//	@Override
//	public void addEntity(LayoutEntity arg0) {
//		algorithm.addEntity(arg0);
//	}
//
//	@Override
//	public void addProgressListener(ProgressListener arg0) {
//		algorithm.addProgressListener(arg0);
//	}
//
//	@Override
//	public void addRelationship(LayoutRelationship arg0) {
//		algorithm.addRelationship(arg0);
//	}
//
//	@Override
//	public void applyLayout(LayoutEntity[] arg0, LayoutRelationship[] arg1, double arg2, double arg3, double arg4,
//			double arg5, boolean arg6, boolean arg7) throws InvalidLayoutConfiguration {
//		algorithm.applyLayout(arg0, arg1, arg2, arg3, arg4, arg5, arg6, arg7);
//	}
//
//	@Override
//	public double getEntityAspectRatio() {
//		return algorithm.getEntityAspectRatio();
//	}
//
//	@Override
//	public int getStyle() {
//		return algorithm.getStyle();
//	}
//
//	@Override
//	public boolean isRunning() {
//		return algorithm.isRunning();
//	}
//
//	@Override
//	public void removeEntity(LayoutEntity arg0) {
//		algorithm.removeEntity(arg0);
//	}
//
//	@Override
//	public void removeProgressListener(ProgressListener arg0) {
//		algorithm.removeProgressListener(arg0);
//	}
//
//	@Override
//	public void removeRelationship(LayoutRelationship arg0) {
//		algorithm.removeRelationship(arg0);
//	}
//
//	@Override
//	public void removeRelationships(@SuppressWarnings("rawtypes") List arg0) {
//		algorithm.removeRelationships(arg0);
//	}
//
//	@Override
//	public void setComparator(@SuppressWarnings("rawtypes") Comparator arg0) {
//		algorithm.setComparator(arg0);
//	}
//
//	@Override
//	public void setEntityAspectRatio(double arg0) {
//		algorithm.setEntityAspectRatio(arg0);
//	}
//
//	@Override
//	public void setFilter(Filter arg0) {
//		algorithm.setFilter(arg0);
//	}
//
//	@Override
//	public void setStyle(int arg0) {
//		algorithm.setStyle(arg0);
//	}
//
//	@Override
//	public void stop() {
//		algorithm.stop();
//	}
//
//	//ZestLayoutAlgorith Methods
//	@Override
//	public String getName() {
//		return this.name;
//	}
//	
//	@Override
//	public boolean setName(String name)  {
//		this.name = name;
//		
//		return true;
//	}
//	
//	@Override
//	public boolean equals(Object o) {
//		if(this.getClass().isInstance(o)) {
//			return this.getName().equals(((GeneralLayoutAlgorithm) o).getName());
//		}
//		
//		return false;
//	}
//
//	//Other abstract methods
//	protected abstract LayoutAlgorithm getLayoutAlgorithm();
//	protected abstract LayoutAlgorithm getLayoutAlgorithm(int style);
//}
