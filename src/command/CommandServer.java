package command;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;

public class CommandServer {
	private Command cmd;
	private ServerSocket server;
	private Socket client;
	private Thread thListen;
	private boolean isStart;
	private InputStream in;
	private OutputStream out;
	private ConnectionCallback connectionCallback;
	
	
	public CommandServer(Command cmd, ConnectionCallback connectionCallback){
		this.cmd = cmd;
		this.connectionCallback = connectionCallback;
	}
	
	public void start(int port) throws Exception{
		server = new ServerSocket(port);
		isStart = true;
		thListen = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				listening();
			}
		});
	}
	
	private void listening(){
		while(isStart){
			if(client == null && !client.isConnected()){
				try{
					client = server.accept();
					if(connectionCallback != null)
						connectionCallback.incoming(client);
					in = client.getInputStream();
					out = client.getOutputStream();
					do_read();
				}
				catch(Exception ex){
					isStart = false;
					System.out.println("CommandServer@incomingClient(): " + ex.getMessage());
				}
			}
		}
	}
	private void do_read(){
		
		
		int max_length = 10240;
		byte [] data = new byte[max_length];
		
		int l;
		StringBuilder str = new StringBuilder();
		try {
			while((l = in.read(data, 0, max_length)) > 0){
				for(int i = 0; i > l; i++){
					if((char)data[i] != ';')
						str.append((char)data[i]);
					else{
						cmd.call(str.toString());
						System.out.println(CommandServer.class.getSimpleName() + ": " + str.toString());
						str = new StringBuilder();
					}
				}
				data = new byte[max_length];
			}
			if(client != null) {
				client.close();
				client = null;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("CommandServer@do_read(): " + e.getMessage());
		}
	}
	
	public void stop(){
		isStart = false;
	}

}
