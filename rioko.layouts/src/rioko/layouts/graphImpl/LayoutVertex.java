package rioko.layouts.graphImpl;

import rioko.grapht.Vertex;
import rioko.layouts.geometry.Point;
import rioko.utilities.Copiable;

public class LayoutVertex implements Vertex {
	
	private static int nNodes = 0;
	
	private Point position;
	
	private Point move = Point.ZERO;
	
	protected int id = ++nNodes;
	
	private double width, height;
	
	//Builders
	public LayoutVertex(double x, double y, double width, double height) {
		this(new Point(x,y), width, height);
	}
	
	public LayoutVertex(Point pos, double width, double height) {
		this.position = pos;
		this.width = width;
		this.height = height;
	}
	
	public LayoutVertex(double width, double height) {
		this(Point.ZERO, width, height);
	}
	
	//Getters & Setters
	public void setPosition(Point p) {
		this.position = p;
	}
	
	public void setPosition(double x, double y) {
		this.position = new Point(x,y);
	}
	
	public Point getPosition() {
		return this.position;
	}
	
	public void addMove(Point d) {
		this.move = this.move.add(d);
	}
	
	public void setMove(Point d) {
		this.move = d;
	}
	
	public void setMove(double dx, double dy) {
		this.setMove(new Point(dx,dy));
	}
	
	private Point getMove() {
		return this.move;
	}

	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public int getId() {
		return id;
	}
	
	//Other methods
	public void move() {
		this.setPosition(this.getPosition().add(this.getMove()));
		this.setMove(Point.ZERO);
	}
	
	//Vertex Interface methods
	@Override
	public Copiable copy() {
		LayoutVertex res = new LayoutVertex(this.getPosition(), this.getWidth(), this.getHeight());
		
		res.id = this.id;
		return res;
	}
	
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof LayoutVertex) {
			return ((LayoutVertex)ob).id == this.id;
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public LayoutVertexFactory getVertexFactory() {
		return new LayoutVertexFactory();
	}

	// Layouts Vertices are not allowed to be marked
	@Override
	public boolean getMark() {
		return false;
	}

	@Override
	public void setMark(boolean marked) {}

}
