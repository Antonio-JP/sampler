package rioko.layouts.algorithms.examples;

import java.util.ArrayList;
import java.util.Random;

import rioko.layouts.algorithms.AbstractLayoutAlgorithm;
import rioko.layouts.algorithms.AlgorithmPhase;
import rioko.layouts.geometry.Point;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.utilities.Log;

public class RandomLayoutAlgorithm extends AbstractLayoutAlgorithm {
	
	private ArrayList<Point> vectors = new ArrayList<>();
	private ArrayList<Boolean> finished = new ArrayList<>();
	private ArrayList<LayoutVertex> vertices;
	
	private int iteration;
	
	@Override
	public RandomLayoutAlgorithm copy() {
		RandomLayoutAlgorithm res = new RandomLayoutAlgorithm();
		res.setName(this.getName());
		return res;
	}
	
	@Override
	protected void preparation() {
		this.vectors = new ArrayList<>(this.graph.vertexSet().size());
		this.finished = new ArrayList<>(this.graph.vertexSet().size());
		this.vertices = new ArrayList<>(this.graph.vertexSet());
		
		Random r = new Random();
		
		for(int i = 0; i < this.graph.vertexSet().size(); i++) {
			this.finished.add(false);
			this.vectors.add(this.getRandomVector(r));
		}
		
		this.iteration = 1;
	}

	@Override
	protected void running() {
		double factor_expand = 1.3;
		for(int i = 0; i < this.vertices.size(); i++) {
			if(!this.finished.get(i)) {
				int j;
				for(j = 0; j < i; j++) {
					if(this.getRectangle(this.vertices.get(i)).expandRectangle(factor_expand).collide(
							this.getRectangle(this.vertices.get(j)).expandRectangle(factor_expand))) {
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
		
		for(int i = 0; i < this.vertices.size(); i++) {
			if(!this.finished.get(i)) {
				this.setDVector(this.vertices.get(i), this.vectors.get(i));
			}
		}
		this.iteration++;
	}
	
	@Override
	protected AlgorithmPhase getNextFromRunning() {
		for(int i = 0; i < this.vertices.size(); i++) {
			if(!this.finished.get(i)) {
				return AlgorithmPhase.RUNNING;
			}
		}
		
		return AlgorithmPhase.OFF;
	}
	
	@Override
	protected AlgorithmPhase getNextFromPreparation() {
		return AlgorithmPhase.ADJUST;
	}
	
	@Override
	protected AlgorithmPhase getNextFromAdjust() {
		return AlgorithmPhase.RUNNING;
	}

	//Private methods
	private Point getRandomVector(Random r) {
		double x = r.nextDouble()*this.getLayoutArea().getWidth() - this.getLayoutArea().getWidth()/2;
		double y = r.nextDouble()*this.getLayoutArea().getHeight() - this.getLayoutArea().getHeight()/2;
		
		Point res = new Point(x,y);
		
		return res.scale(1/res.norm());
	}
}
