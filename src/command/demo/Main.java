package command.demo;

import command.*;
import command.demo.commands.OSCommand;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Command cmd = new Command(new UnknownCommand() {

			@Override
			public void onUnknown(String data, Object byWho) {
				// TODO Auto-generated method stub
				System.out.println(data);
			}
			
		});
		
		final OSCommand os = new OSCommand(CommandType.BOTH);
		os.setCallback(new CommandCallback(){

			@Override
			public void onCallBack(Object byWho, CommandTemplate pCommand) {
				// TODO Auto-generated method stub
				System.out.println(os.toOutput());
			}
			
		});
		/*os.setErrorCallback(new ErrorCallback() {

			@Override
			public void onError(String error) {
				// TODO Auto-generated method stub
				System.out.println(error);
			}
			
		});*/
		
		cmd.add(os);
		
		
		cmd.call("@os /hostname /os-version /os-arch /os-name");
		//cmd.call("-os hostname:'testme' os-version:'4.4.0-96-generic' os-arch:'amd64' os-name:'Linux'");
	}

}
