package rioko.rsockets;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

import rioko.utilities.Log;

public class RSocket<T extends ISerializable> implements IConnectable {
	
	//Inner Java Socket classes
	private Socket javaSck = null;
	private ServerSocket javaServer = null;
	
	//Flags of state
	private boolean opened = false;
	private boolean connected = false;

	private InputStream inStream = null;
	private OutputStream outStream = null;
	
	//Getters & Setters	
	public boolean isConnected() {
		return this.connected;
	}
	
	public boolean isClosed() {
		return !this.opened;
	}
	
	public boolean isServer() {
		return this.javaServer != null;
	}
	
	//Proper methods
	public boolean sendObject(T object) throws IOException {
		if(!this.isConnected()) {
			throw new IOException("The socket should be connected to send data");
		}
		
		byte[] objectSerialize = object.serialize();
		
		try {
			this.outStream.write(objectSerialize);
			return true;	
		} catch(IOException ioe) {
			Log.exception(new Exception("Error sending the data", ioe));
			return false;
		}	
	}
	
	public boolean receiveObject(T dest) throws IOException {
		if(!this.isConnected()) {
			throw new IOException("The socket should be connected to receive data");
		}
		
		byte[] readedBytes = new byte[dest.sizeOfSer()];
		try {
			for(int i = 0; i < dest.sizeOfSer(); i++) {
				int read = this.inStream.read();
				if(read == -1) {
					throw new IOException("End of input reached before expected");
				}
				
				readedBytes[i] = (byte)(read % 256);
			}
			
			dest.deserialize(readedBytes);
			return true;
		} catch(IOException ioe) {
			Log.exception(new Exception("Error ocurred while reading the object", ioe));
			return false;
		}
	}
	
	
	//IConnectable methods
	@Override
	public boolean openServer(int port) throws IOException {
		if(this.opened) {
			throw new IOException("This socket server is already opened");
		}
		
		try {
			this.javaServer = new ServerSocket(port);
			this.opened = true;
		} catch (IllegalArgumentException iae) {
			Log.exception(new Exception("The port for this socket is not valid", iae));
		}
		
		return this.isServer();
	}

	@Override
	public boolean waitConnection() throws IOException {
		return this.waitConnection(0);
	}
	
	@Override
	public boolean waitConnection(int time) throws IOException {
		if(!this.opened) {
			throw new IOException("This socket server is not opened");
		} else if(!this.isConnected()) {
			throw new IOException("This socket server is already connected");
		}
		
		this.javaServer.setSoTimeout(time);
		try {
			this.javaSck = this.javaServer.accept();
			
			this.inStream = this.javaSck.getInputStream();
			this.outStream = this.javaSck.getOutputStream();
			
			this.connected = true;
		} catch(SocketTimeoutException ste) {
			Log.exception(new Exception("Timeout reached waiting connection for the server"));
		}
		
		return this.isConnected();
	}
	
	@Override
	public boolean closeClient() throws IOException {
		if(!this.isServer()) {
			throw new IOException("This sockect is not a server");
		} else if(!this.isConnected()) {
			throw new IOException("This sockect has no client listening");
		}
		
		this.javaSck.close();
		this.connected = false;
		
		return !this.isConnected();
	}
	
	@Override
	public boolean connect(String host, int port) throws IOException {
		if(this.isServer()) {
			throw new IOException("This socket is already a server and it can not connect to another server");
		} else if(this.isConnected()) {
			throw new IOException("This socket is already connected");
		}
		
		this.javaServer = null;
		this.javaSck = new Socket(host, port);
		
		this.inStream = this.javaSck.getInputStream();
		this.outStream = this.javaSck.getOutputStream();
		
		this.opened = true;
		this.connected = true;
		
		return this.isConnected();
	}

	@Override
	public boolean close() throws IOException {
		if(this.javaSck != null) {
			this.javaSck.close();
		}
		
		if(this.javaServer != null) {
			this.javaServer.close();
		}
		
		this.inStream = null;
		this.outStream = null;
		
		this.opened = false;
		this.connected = false;
		
		return this.isClosed();
	}



}
