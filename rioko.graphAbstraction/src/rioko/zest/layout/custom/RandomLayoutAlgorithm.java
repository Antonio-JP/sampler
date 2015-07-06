package rioko.zest.layout.custom;

import java.util.ArrayList;
import java.util.Random;

import org.eclipse.zest.layouts.LayoutStyles;

import rioko.utilities.Log;
import rioko.zest.layout.CustomLayoutAlgorithm;
import rioko.zest.layout.Point;

public class RandomLayoutAlgorithm extends CustomLayoutAlgorithm {
	
	private ArrayList<Point> vectors = new ArrayList<>();
	private ArrayList<Boolean> finished = new ArrayList<>();
	
	private int iteration;

	public RandomLayoutAlgorithm() {
		super(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
	}
	
	public RandomLayoutAlgorithm(int styles) {
		super(styles);
	}
	
	@Override
	public RandomLayoutAlgorithm copy() {
		RandomLayoutAlgorithm res = new RandomLayoutAlgorithm(this.getStyle());
		res.setName(this.getName());
		return res;
	}
	
	@Override
	protected void preparation() {
		this.vectors = new ArrayList<>(this.nodes.length);
		this.finished = new ArrayList<>(this.nodes.length);
		
		Random r = new Random();
		
		for(int i = 0; i < this.nodes.length; i++) {
			this.finished.add(false);
			this.vectors.add(this.getRandomVector(r));
		}
		
		this.iteration = 1;
	}

	@Override
	protected void running() {
		for(int i = 0; i < this.nodes.length; i++) {
			if(!this.finished.get(i)) {
				int j;
				for(j = 0; j < i; j++) {
					if(this.getRectangle(this.nodes[i]).collide(this.getRectangle(this.nodes[j]))) {
						this.finished.set(i, false);
						break;
					}
				}
				if(j == i) {
					this.finished.set(i, true);
					Log.sPrint(" -- Node " + i + " finished (" + this.iteration + ")");
				}
			}
		}
		
		for(int i = 0; i < this.nodes.length; i++) {
			if(!this.finished.get(i)) {
				this.setDVector(this.nodes[i], this.vectors.get(i));
			}
		}
		this.iteration++;
	}
	
	@Override
	protected AlgPhase getNextFromRunning() {
		for(int i = 0; i < this.nodes.length; i++) {
			if(!this.finished.get(i)) {
				return AlgPhase.RUNNING;
			}
		}
		
		return AlgPhase.OFF;
	}
	
	@Override
	protected AlgPhase getNextFromPreparation() {
		return AlgPhase.ADJUST;
	}
	
	@Override
	protected AlgPhase getNextFromAdjust() {
		return AlgPhase.RUNNING;
	}

	//Private methods
	private Point getRandomVector(Random r) {
		double x = r.nextDouble()*this.getLayoutArea().getWidth() - this.getLayoutArea().getWidth()/2;
		double y = r.nextDouble()*this.getLayoutArea().getHeight() - this.getLayoutArea().getHeight()/2;
		
		Point res = new Point(x,y);
		
		return res.scale(1/res.norm());
	}
}
