package rioko.rsockets;

import java.io.Serializable;

public interface ISerializable extends Serializable {
	public byte[] serialize();
	public ISerializable deserialize(byte[] data);
	public int sizeOfSer();
}
