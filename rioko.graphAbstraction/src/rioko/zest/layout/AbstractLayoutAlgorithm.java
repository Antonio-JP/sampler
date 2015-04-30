package rioko.zest.layout;

public abstract class AbstractLayoutAlgorithm implements ZestLayoutAlgorithm {

	private String name = this.getClass().getSimpleName();
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean setName(String name)  {
		this.name = name;
		
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if(this.getClass().isInstance(o)) {
			return this.getName().equals(((AbstractLayoutAlgorithm) o).getName());
		}
		
		return false;
	}
}
