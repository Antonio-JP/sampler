package rioko.lalg;

public class VectorImpl implements Vector<VectorImpl>{
	
	private double elements[];
	
	//Builders
	public VectorImpl(int size) {
		this.elements = new double[size];
	}
	
	public VectorImpl(double elements[]) {
		this.elements = new double[elements.length];
		
		for(int i = 0; i < elements.length; i++) {
			this.elements[i] = elements[i];
		}
	}

	@Override
	public double getElement(int pos) {
		if(pos >= this.size() || pos < 0) {
			throw new IllegalArgumentException("Position argument incorrect for a Vector");
		}
		return this.elements[pos];
	}

	@Override
	public void setElement(int pos, double value) throws IllegalArgumentException {
		if(pos >= this.size() || pos < 0) {
			throw new IllegalArgumentException("Position argument incorrect for a Vector");
		}
		this.elements[pos] = value;
	}

	@Override
	public int size() {
		return this.elements.length;
	}

	@Override
	public VectorImpl add(Vector<VectorImpl> toAdd) throws ArithmeticException {
		if(this.size() != toAdd.size()) {
			throw new ArithmeticException("Different size to operate Vectors");
		}
		VectorImpl res = new VectorImpl(this.size());
		
		for(int i = 0; i < this.size(); i++) {
			res.setElement(i, this.getElement(i) + toAdd.getElement(i));
		}
		
		return res;
	}

	@Override
	public VectorImpl opposite() {
		return this.scalar(-1);
	}

	@Override
	public VectorImpl scalar(double scalar) {
		VectorImpl res = new VectorImpl(this.size());
		
		for(int i = 0; i < this.size(); i++) {
			res.setElement(i, scalar*this.getElement(i));
		}
		
		return res;
	}

	@Override
	public VectorImpl copy() {
		return new VectorImpl(this.elements);
	}

}
