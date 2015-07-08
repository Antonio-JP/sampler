package rioko.layouts.geometry;

import rioko.layouts.geometry.exceptions.OutOfBundlesException;

public class Rectangle {
	
	private Point firstPoint;
	private Point origin;
	private double width, height;
	
	public Rectangle(double width, double height) {
		this(new Point(), width, height);
	}
	
	public Rectangle(Point point, double width, double height) {
		this.firstPoint = point;
		this.origin = point;
		
		this.width = width;
		this.height = height;
	}
	
	public Rectangle(Point point, Point origin, double width, double height) throws OutOfBundlesException{
		this(point, width, height);
		
		this.setOrigin(origin);
	}
	
	//Setting methods
	public void setOrigin(Point origin) throws OutOfBundlesException{
		if(!this.containsInAbsolute(origin)) {
			throw new OutOfBundlesException();
		}
		
		this.origin = origin;
	}
	
	//Getting methods
	public double getLeft() {
		return this.firstPoint.getX();
	}
	
	public double getRight() {
		return this.firstPoint.getX() + this.width;
	}
	
	public double getTop() {
		return this.firstPoint.getY();
	}
	
	public double getBottom() {
		return this.firstPoint.getY() + this.height;
	}
	
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public Point getOrigin() {
		return this.origin;
	}
	
	public Point getCenter() {
		return new Point(this.firstPoint.getX() + this.width/2, this.firstPoint.getY() + this.height/2);
	}
	
	//Relative Methods
	public boolean contains(Point p) {
		return this.containsInAbsolute(p.add(this.getOrigin()));
	}

	//Absolute Methods
	public boolean containsInAbsolute(Point p) {
		if(p.getX() < this.getLeft() || p.getX() > this.getRight()) {
			return false;
		} else if(p.getY() < this.getTop() || p.getY() > this.getBottom()) {
			return false;
		}
		
		return true;
	}
	
	//Parsing methods
	public Point getAbsolute(Point p) {
		return p.add(origin);
	}
	
	public Point getRelative(Point p) {
		return p.sub(origin);
	}

	//Geometry methods
	public boolean collide(Rectangle rectangle) {
		if(this.containsInAbsolute(new Point(rectangle.getLeft(), rectangle.getTop()))) {
			return true;
		} else if(this.containsInAbsolute(new Point(rectangle.getLeft(), rectangle.getBottom()))) {
			return true;
		} else if(this.containsInAbsolute(new Point(rectangle.getRight(), rectangle.getTop()))) {
			return true;
		} else if(this.containsInAbsolute(new Point(rectangle.getRight(), rectangle.getBottom()))) {
			return true;
		} else if(rectangle.containsInAbsolute(new Point(this.getLeft(), this.getTop()))) {
			return true;
		} else if(rectangle.containsInAbsolute(new Point(this.getLeft(), this.getBottom()))) {
			return true;
		} else if(rectangle.containsInAbsolute(new Point(this.getRight(), this.getTop()))) {
			return true;
		} else if(rectangle.containsInAbsolute(new Point(this.getRight(), this.getBottom()))) {
			return true;
		}
		
		return false;
	}
	
	public Rectangle expandRectangle(double scale) {
		Point center = this.getCenter();
		Point aux = new Point(this.width*scale, this.height*scale);
		
		return new Rectangle(center.sub(aux.scale(0.5)), aux.getX(), aux.getY());
	}
	
	//Other simple methods
	@Override
	public String toString() {
		return "[" + this.firstPoint.toString() +" -> " + this.width + ", " + this.height + "]";
	}
}
