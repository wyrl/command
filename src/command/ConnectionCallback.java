package command;

import java.net.Socket;

public interface ConnectionCallback {
	public void connected(Socket address);
	public void incoming(Socket client);
	public void disconnected(Socket address);
}
