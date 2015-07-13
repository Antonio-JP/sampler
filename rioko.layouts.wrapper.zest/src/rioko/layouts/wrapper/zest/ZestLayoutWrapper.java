package rioko.layouts.wrapper.zest;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.zest.layouts.LayoutBendPoint;
import org.eclipse.zest.layouts.LayoutEntity;
import org.eclipse.zest.layouts.LayoutRelationship;
import org.eclipse.zest.layouts.constraints.LayoutConstraint;
import org.eclipse.zest.layouts.dataStructures.InternalNode;
import org.eclipse.zest.layouts.dataStructures.InternalRelationship;

import rioko.layouts.algorithms.LayoutAlgorithm;
import rioko.layouts.geometry.Point;
import rioko.layouts.geometry.Rectangle;
import rioko.layouts.graphImpl.LayoutEdge;
import rioko.layouts.graphImpl.LayoutGraph;
import rioko.layouts.graphImpl.LayoutVertex;
import rioko.utilities.Log;

public abstract class ZestLayoutWrapper implements LayoutAlgorithm {
	
	private String name = this.getClass().getSimpleName();

	private SavingCoordsZestLayout auxLayout = new SavingCoordsZestLayout(this.getAlgorithm());
	
	public ZestLayoutWrapper() {
		
	}

	@Override
	public boolean setName(String name) {
		this.name = name;
		return true;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void applyLayout(LayoutGraph graphToLayout, Rectangle layoutArea) {
		HashMap<LayoutVertex, AuxLayoutEntity> mapToInVertices = new HashMap<>();
		
		//Create the Auxiliar vertices
		AuxLayoutEntity[] vertices = new AuxLayoutEntity[graphToLayout.vertexSet().size()];
		
		int i = 0;
		for(LayoutVertex node : graphToLayout.vertexSet()) {
			vertices[i] = new AuxLayoutEntity(node);
			mapToInVertices.put(node, vertices[i]);
						
			i++;
		}
		
		//Create the auxiliar relationships
		AuxLayoutRelationship[] edges = new AuxLayoutRelationship[graphToLayout.edgeSet().size()];
		i = 0;
		for(LayoutEdge edge : graphToLayout.edgeSet()) {
			edges[i] = new AuxLayoutRelationship(mapToInVertices.get(edge.getSource()), mapToInVertices.get(edge.getTarget()));
						
			i++;
		}
		
		//Call the layout algorithm
		try {
			this.applyLayout(vertices, edges, layoutArea.getLeft(), layoutArea.getTop(), layoutArea.getWidth(), layoutArea.getHeight());
			
			ArrayList<Point> coords = this.auxLayout.getCoordinates();
			if(coords != null) {
				i = 0;
				for(LayoutVertex node : graphToLayout.vertexSet()) {
					node.setPosition(coords.get(i));
					i++;
				}
			}
		} catch (Exception e) {
			Log.exception(e);
		}
		
		
	}

	private void applyLayout(AuxLayoutEntity[] vertices, AuxLayoutRelationship[] edges, Double left, Double top,
			Double width, Double height)  throws Exception {
		try { 
			this.auxLayout.applyLayout(vertices, edges, left, top, width, height, true, true);
		} catch (Exception e) {
			try {
			this.auxLayout.applyLayout(vertices, edges, left, top, width, height, false, false);
			} catch (Exception e2) {
				try {
					this.auxLayout.applyLayout(vertices, edges, left, top, width, height, false, true);
				} catch(Exception e3) {
					try {
					} catch(Exception e4) { 
						this.auxLayout.applyLayout(vertices, edges, left, top, width, height, true, false);
						throw e4;
					}
				}
			}
		}
	}

	@Override
	public LayoutAlgorithm copy() {
		ZestLayoutWrapper res;
		try {
			res = this.getClass().newInstance();
		
			res.setName(this.getName());
		
			return res;
		} catch (InstantiationException | IllegalAccessException e) {
			// Impossible Exception
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Abstract methods
	protected abstract org.eclipse.zest.layouts.LayoutAlgorithm getAlgorithm();
	
	//Private classes	
	private class AuxLayoutEntity extends LayoutVertex implements LayoutEntity {
		
		private Object graphData, layoutInformation;

		public AuxLayoutEntity(LayoutVertex vertex) {
			super(vertex.getPosition(), vertex.getWidth(), vertex.getHeight());
			
			this.id = vertex.getId();			
		}
		
		@Override
		public int compareTo(Object arg0) {
			if(arg0 instanceof AuxLayoutEntity) {
				return this.id - ((LayoutVertex) arg0).getId();
			}
			
			return 0;
		}

		@Override
		public Object getGraphData() {
			return this.graphData;
		}

		@Override
		public void setGraphData(Object arg0) { 
			this.graphData = arg0;
		}

		@Override
		public double getHeightInLayout() {
			return super.getHeight();
		}

		@Override
		public Object getLayoutInformation() {
			return this.layoutInformation;
		}

		@Override
		public double getWidthInLayout() {
			return super.getWidth();
		}

		@Override
		public double getXInLayout() {
			return super.getPosition().getX();
		}

		@Override
		public double getYInLayout() {
			return super.getPosition().getY();
		}

		@Override
		public void populateLayoutConstraint(LayoutConstraint arg0) {
			((InternalNode)this.layoutInformation).populateLayoutConstraint(arg0);
		}

		@Override
		public void setLayoutInformation(Object arg0) {
			this.layoutInformation = arg0;
		}

		@Override
		public void setLocationInLayout(double arg0, double arg1) {
			super.setPosition(arg0,arg1);
		}

		@Override
		public void setSizeInLayout(double arg0, double arg1) {
			return;
		}
		
	}
	
	private class AuxLayoutRelationship implements LayoutRelationship {

		private AuxLayoutEntity source, target;
		private Object GraphData;
		private Object layoutInformation;
		@SuppressWarnings("unused")
		private LayoutBendPoint[] bendPoints = new LayoutBendPoint[0];
		
		public AuxLayoutRelationship(AuxLayoutEntity source, AuxLayoutEntity target) {
			this.source = source;
			this.target = target;
		}

		@Override
		public Object getGraphData() {
			return GraphData;
		}

		@Override
		public void setGraphData(Object GraphData) { 
			this.GraphData = GraphData;
		}

		@Override
		public void clearBendPoints() { 
			this.bendPoints = new LayoutBendPoint[0];
		}

		@Override
		public LayoutEntity getDestinationInLayout() {
			return this.target;
		}

		@Override
		public Object getLayoutInformation() {
			return layoutInformation;
		}

		@Override
		public LayoutEntity getSourceInLayout() {
			return this.source;
		}

		@Override
		public void populateLayoutConstraint(LayoutConstraint arg0) {
			((InternalRelationship)this.layoutInformation).populateLayoutConstraint(arg0);
		}
		

		@Override
		public void setBendPoints(LayoutBendPoint[] arg0) { 
			this.bendPoints = arg0;
		}

		@Override
		public void setLayoutInformation(Object arg0) {	
			this.layoutInformation = arg0;
		}
		
	}

}
