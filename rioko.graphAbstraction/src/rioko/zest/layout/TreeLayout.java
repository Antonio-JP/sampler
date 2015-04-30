package rioko.zest.layout;

import java.util.Comparator;
import java.util.List;

import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;
import org.eclipse.zest.layouts.progress.ProgressListener;

public class TreeLayout extends AbstractLayoutAlgorithm {
	
	private TreeLayoutAlgorithm algorithm;

	public TreeLayout() {
		this.algorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}
	
	public TreeLayout(int style) {
		this.algorithm = new TreeLayoutAlgorithm(style);
	}

	@Override
	public void addEntity(LayoutEntity entity) {
		algorithm.addEntity(entity);
	}

	@Override
	public void addProgressListener(ProgressListener listener) {
		algorithm.addProgressListener(listener);
	}

	@Override
	public void addRelationship(LayoutRelationship relationship) {
		algorithm.addRelationship(relationship);
	}

	@Override
	public void applyLayout(LayoutEntity[] entitiesToLayout, LayoutRelationship[] relationshipsToConsider, double x, double y, double width,
			double height, boolean asynchronous, boolean continuous) throws InvalidLayoutConfiguration {
		algorithm.applyLayout(entitiesToLayout, relationshipsToConsider, x, y, width, height, asynchronous, continuous);
	}

	@Override
	public double getEntityAspectRatio() {
		return algorithm.getEntityAspectRatio();
	}

	@Override
	public int getStyle() {
		return algorithm.getStyle();
	}

	@Override
	public boolean isRunning() {
		return algorithm.isRunning();
	}

	@Override
	public void removeEntity(LayoutEntity entity) {
		algorithm.removeEntity(entity);
	}

	@Override
	public void removeProgressListener(ProgressListener listener) {
		algorithm.removeProgressListener(listener);
	}

	@Override
	public void removeRelationship(LayoutRelationship relationship) {
		algorithm.removeRelationship(relationship);
	}

	@Override
	public void removeRelationships(@SuppressWarnings("rawtypes") List relationships) {
		algorithm.removeRelationships(relationships);
	}

	@Override
	public void setComparator(@SuppressWarnings("rawtypes") Comparator comparator) {
		algorithm.setComparator(comparator);
	}

	@Override
	public void setEntityAspectRatio(double ratio) {
		algorithm.setEntityAspectRatio(ratio);
	}

	@Override
	public void setFilter(Filter filter) {
		algorithm.setFilter(filter);
	}

	@Override
	public void setStyle(int style) {
		algorithm.setStyle(style);
	}

	@Override
	public void stop() {
		algorithm.stop();
	}

	@Override
	public void setEnable(boolean enable) {
		return;
	}
	
	@Override
	public boolean isEnable() {
		return true;
	}

	@Override
	public TreeLayout copy() {
		TreeLayout res = new TreeLayout(this.getStyle());
		res.setName(this.getName());
		
		return res;
	}

}
