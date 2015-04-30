package rioko.drawmodels.filemanage;

@Deprecated
public class MVDReaderNode {
	private String id;
	
	private String name;
	
	private Object data;
	
	public MVDReaderNode(String id, String name, Object data) {
		this.id = id;
		this.name = name;
		this.data = data;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
}
