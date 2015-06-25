package rioko.zest.layout;

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
}
