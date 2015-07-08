package rioko.layouts.geometry;

public class Point {
	public static Point ZERO = new Point(0,0);
	
	private final double x, y;
	
	public Point() {
		this(0,0);
	}
	
	public Point(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public double getX() {
		return this.x;
	}
	
	public double getY() {
		return this.y;
	}
	
	//Creating new Points
	public Point add(Point p) {
		return new Point(this.x + p.x, this.y + p.y);
	}
	
	public Point inverse() {
		return new Point(-this.x, - this.y);
	}
	
	public Point sub(Point p) {
		return this.add(p.inverse());
	}

	public Point scale(double s) {
		return new Point(this.x * s, this.y*s);
	}

	public double norm() {
		return Math.sqrt(getX()*getX() + getY()*getY());
	}
	
	@Override
	public boolean equals(Object ob) {
		if(ob instanceof Point) {
			return (this.x == ((Point) ob).getX()) && (this.y == ((Point) ob).getY());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (new Double(this.x+this.y)).hashCode();
	}
	
	@Override
	public String toString() {
		return "(" + this.x + ", " + this.y + ")";
	}
}
