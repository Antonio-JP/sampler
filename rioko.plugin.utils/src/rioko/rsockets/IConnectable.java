package rioko.rsockets;

import java.io.IOException;

public interface IConnectable {	
	//Server side methods
	public boolean openServer(int port) throws IOException;
	public boolean waitConnection() throws IOException;
	public boolean waitConnection(int time) throws IOException;
	public boolean closeClient() throws IOException;
	
	//Client side methods
	public boolean connect(String host, int port) throws IOException;
	public boolean close() throws IOException;
}
