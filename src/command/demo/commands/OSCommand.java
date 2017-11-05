package command.demo.commands;

import command.CommandAttribute;
import command.CommandTemplate;
import command.CommandType;

public class OSCommand extends CommandTemplate {

	private CommandAttribute attributes;
	private String hostname;
	private String OSName;
	private String OSArch;
	private String OSVersion;
	
	
	public OSCommand(CommandType command_type) {
		super("os", command_type);
		// TODO Auto-generated constructor stub
		setAttributes();
		reset();
	}
	
	private void setAttributes(){
		attributes = new CommandAttribute();
		attributes.add("hostname").in(CommandType.SETTER, true);
		attributes.add("os-arch").in(CommandType.SETTER, true);
		attributes.add("os-version").in(CommandType.SETTER, true);
		attributes.add("os-name").in(CommandType.SETTER, true);
	}
	
	@Override
	public void begin() {
		// TODO Auto-generated method stub
		appendOutput("------------------------------------");
	}
	

	@Override
	public void toSetCall(String attributeName, String value) {
		// TODO Auto-generated method stub
		switch(attributeName) {
			case "hostname":
				hostname = value;
				appendOutput("hostname: " + value);
				break;
			case "os-arch":
				appendOutput("os-arch: " + value);
				OSArch = value;
				break;
			case "os-name":
				appendOutput("os-name: " + value);
				OSName = value;
				break;
			case "os-version":
				appendOutput("os-version: " + value);
				break;
		}
	}

	@Override
	public void toGetCall(String attributeName, String value) {
		// TODO Auto-generated method stub
		switch(attributeName){
			case "/hostname":
				appendOutput("hostname...");
				hostname = System.getProperty("user.name");
				break;
			case "/os-arch":
				appendOutput("os-arch...");
				OSArch = System.getProperty("os.arch");
				break;
			case "/os-name":
				appendOutput("os-name...");
				OSName = System.getProperty("os.name");
				break;
			case "/os-version":
				appendOutput("os-version...");
				OSVersion = System.getProperty("os.version");
				break;
				
		}
	}

	@Override
	public CommandAttribute prepareAttributes() {
		// TODO Auto-generated method stub
		return attributes;
	}

	@Override
	public void end(CommandType command_type) {
		// TODO Auto-generated method stub
		if(command_type == CommandType.GETTER) {
			String strSend = String.format("-os hostname:'%s' os-arch:'%s' os-name:'%s' os-version:'%s'", hostname,OSArch,OSName,OSVersion);
			//request.send(strSend);
			appendOutput("Response Command: " + strSend);
			appendOutput("----------------SENT----------------");
		}
		else if(command_type == CommandType.SETTER) {
			//response.onRecieved(null);
			appendOutput("--------------RESULTS---------------");
		}
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		
		default_reset();
		
		hostname = "";
		OSArch = "";
		OSName = "";
		OSVersion = "";
		
	}

}
