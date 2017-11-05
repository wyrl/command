package command;

import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CommandClient implements IRequestor {
	private Command cmd;
	private InputStream in;
	private OutputStream out;
	private Socket socket;
	private Thread thRead;
	private ConnectionCallback callback;
	
	public boolean isConnected;
	
	
	public CommandClient(Command cmd, ConnectionCallback callback){
		this.cmd = cmd;
		isConnected = false;
		this.callback = callback;
	}
	
	public void connect(String ip, int port) throws UnknownHostException, IOException{
		if(!isConnected){
			System.out.println("Connecting...");
			socket = new Socket(ip, port);
			
			if(callback != null)
				callback.connected(socket);
			
			in = socket.getInputStream();
			out = socket.getOutputStream();
		
			thRead = new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					do_read();
				}
			});
			thRead.start();
			isConnected = true;
		}
	}
	
	
	private void do_read(){
		int max_length = 1024;
		byte [] data = new byte[max_length];
		
		int l;
		StringBuilder str = new StringBuilder();
		try {
			System.out.println("do read...");
			while((l = in.read(data, 0, max_length)) > 0){
				for(int i = 0; i < l; i++){
					if((char)data[i] != ';')
						str.append((char)data[i]);
					else{
						cmd.call(str.toString(), this);
						//System.out.println(CommandClient.class.getSimpleName() + ": " + str.toString());
						str = new StringBuilder();	
					}
				}
				data = new byte[max_length];
			}
			System.out.println("end do read...");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("CommandClient@do_read(): " + e.getLocalizedMessage());
		}
		
		if(isConnected)
			disconnect();
	}
	
	public void disconnect(){
		isConnected = false;
		try {
			if(socket != null) {
				if(callback != null)
					callback.disconnected(socket);
					
				socket.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("CommandClient@disconnect: " + e.getMessage());
		}
	}
	
	
	
	@Override
	public void send(String data){
		byte [] d = data.getBytes();
		
		try {
			out.write(d, 0, data.length());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("CommandClient@send(): " + e.getMessage());
		}
	}
	
	@Override
	public boolean isAvailable() {
		return  isConnected;
	}

}
