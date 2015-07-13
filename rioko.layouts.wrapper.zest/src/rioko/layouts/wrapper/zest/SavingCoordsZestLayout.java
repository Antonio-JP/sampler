package rioko.layouts.wrapper.zest;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.zest.layouts.Filter;
import org.eclipse.zest.layouts.InvalidLayoutConfiguration;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.progress.ProgressListener;

import rioko.layouts.geometry.Point;

public class SavingCoordsZestLayout implements LayoutAlgorithm {
	
	LayoutAlgorithm algorithm = null;
	
	ArrayList<Point> coords;

	public SavingCoordsZestLayout(LayoutAlgorithm algorithm) {
		this.algorithm = algorithm;
	}
	
	//LayoutAlgorithm methods
	@Override
	public void addEntity(LayoutEntity arg0) {
		algorithm.addEntity(arg0);
	}
	
	@Override
	public void addProgressListener(ProgressListener arg0) {
		algorithm.addProgressListener(arg0);
	}
	
	@Override
	public void addRelationship(LayoutRelationship arg0) {
		algorithm.addRelationship(arg0);
	}
	
	@Override
	public void applyLayout(LayoutEntity[] entities, LayoutRelationship[] relations, double x, double y, double width,
			double height, boolean arg6, boolean arg7) throws InvalidLayoutConfiguration{
		this.coords = new ArrayList<>(entities.length);
		
		algorithm.applyLayout(entities, relations, x, y, width, height, arg6, arg7);
		
		long iniTime = System.nanoTime();
		
		while(algorithm.isRunning()) {
			if(this.tooLong(iniTime, entities.length)) {
				throw new InvalidLayoutConfiguration();
			}
		}
		
		//After apply the algorithm, we save its new positions
		for(int i = 0; i < entities.length; i++) {
			this.coords.add(i, new Point(entities[i].getXInLayout(), entities[i].getYInLayout()));
		}
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
	public void removeEntity(LayoutEntity arg0) {
		algorithm.removeEntity(arg0);
	}
	
	@Override
	public void removeProgressListener(ProgressListener arg0) {
		algorithm.removeProgressListener(arg0);
	}
	
	@Override
	public void removeRelationship(LayoutRelationship arg0) {
		algorithm.removeRelationship(arg0);
	}
	
	@Override
	public void removeRelationships(@SuppressWarnings("rawtypes") List arg0) {
		algorithm.removeRelationships(arg0);
	}
	
	@Override
	public void setComparator(@SuppressWarnings("rawtypes") Comparator arg0) {
		algorithm.setComparator(arg0);
	}
	
	@Override
	public void setEntityAspectRatio(double arg0) {
		algorithm.setEntityAspectRatio(arg0);
	}
	
	@Override
	public void setFilter(Filter arg0) {
		algorithm.setFilter(arg0);
	}
	
	@Override
	public void setStyle(int arg0) {
		algorithm.setStyle(arg0);
	}
	
	@Override
	public void stop() {
		algorithm.stop();
	}
	
	//Other methods
	public ArrayList<Point> getCoordinates() {
		return this.coords;
	}
	
	private static double nsPerS = 1000000000.0;
	private static double sPerNode = 1/100.0;
	private boolean tooLong(long iniTime, int nNodes) {
		double maxTime = nNodes*sPerNode;
		
		return ((System.nanoTime() - iniTime)/nsPerS > maxTime);
	}
}