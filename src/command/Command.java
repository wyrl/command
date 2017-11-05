package command;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.*;
import java.util.Iterator;

public class Command //ArrayList<CommandTemplate>
{	
	private UnknownCommand unknownCommand;
	private Pattern pat;
	private Hashtable<String, CommandTemplate> parents;
	
	public Command() {
		this(null);
	}
	
	public Command(UnknownCommand unknownCommand){
		this.unknownCommand = unknownCommand;
		pat = Pattern.compile("([a-z-]+:\"\")|([a-z-]+:\".+?\")|([a-z-]+:'')|([a-z-]+:'.+?')|([a-z-]+:[\\S]+)|(/[a-z-]+)");
		parents = new Hashtable<String, CommandTemplate>();
	}
	
	public void add(CommandTemplate e) {
		// TODO Auto-generated method stub
		if(e.prepareAttributes() == null){
			throw new IllegalStateException("the implement of CommandTemplate 'takeAttributes' method should have at least attribute.");
		}
		if(parents.containsKey(e.getParentName()))
			throw new IllegalStateException("'" + e.getParentName() + "(" + CommandTemplate.class.getSimpleName() + ")' has already been added.");
		else
			parents.put(e.getParentName(), e);
	}
	
	public CommandTemplate getParentByName(String parentName)
	{
		return parents.get(parentName);
	}
	public void call(String strCommand)
	{
		call(strCommand, null);
	}
	public void call(String strCommand, Object byWho)
	{
		int startIndexPCommand = (strCommand.indexOf("-") & strCommand.indexOf("@"));
		int endIndexPCommand = (strCommand.indexOf(" "));

		String strParentCommand = (startIndexPCommand == 0 && endIndexPCommand > -1) ? strCommand.substring(startIndexPCommand, endIndexPCommand) : "";
		int startIndexCCommand = endIndexPCommand + 1;
		int endIndexCCommand = strCommand.length();
        String strChildCommand = (startIndexCCommand > 0 && strCommand.length() > startIndexCCommand) ? strCommand.substring(startIndexCCommand, endIndexCCommand) : "";
        
		if (startIndexPCommand != 0)
		{
			if (unknownCommand != null) unknownCommand.onUnknown(strCommand, byWho);
			return;
		}
		String parentName = strParentCommand.length() > 1 ? strParentCommand.substring(1, strParentCommand.length()) : "";

		CommandTemplate parent = (CommandTemplate)getParentByName(parentName);
		
		if (parent == null
			|| ((int)parent.getCommandType().toInt() & 1 << 0) != (int) CommandType.GETTER.toInt() && strParentCommand.indexOf("@") > -1
			|| ((int)parent.getCommandType().toInt() & 1 << 1) != (int) CommandType.SETTER.toInt() && strParentCommand.indexOf("-") > -1)
		{
			if (unknownCommand != null) unknownCommand.onUnknown(strCommand, byWho);
			return;
		}
		
		if (parent != null)
		{
			parent.CurrentType = CommandTypeByFirstChar(strParentCommand.charAt(0));
			if (checkAttribute(parent, strChildCommand)){
				parent.begin();
				Matcher mat = pat.matcher(strChildCommand);
				while(mat.find())
				{
					String child = mat.group();
					int index = child.indexOf(':');
					String attr = "";
					String val = "";
					
					if (index > -1)
					{
						attr = child.substring(0, index);
						val = child.substring((index + 1), child.length());
						if (val.indexOf("'") == 0 && val.lastIndexOf("'") == val.length() - 1) 
							val = val.substring(1, val.length() - 1);
					}
					else attr = child;
					
					if (strParentCommand.indexOf("-") == 0)
						parent.toSetCall(attr, val);
					else if (strParentCommand.indexOf("@") == 0)
						parent.toGetCall(attr, val);
				}
				
				parent.setRequestBy(byWho);
				CommandCallback callback = parent.getCallback();
				//parent.beforeCall(parent.CurrentType = CommandTypeByFirstChar(strParentCommand.charAt(0)));
				parent.end(parent.CurrentType);
				if (callback != null) 
					callback.onCallBack(byWho, parent);
				parent.reset();
			}
		}
	}
	
	private boolean checkAttribute(CommandTemplate parent, String strChildCommand){
		Matcher mat = pat.matcher(strChildCommand);
		
		CommandAttribute attributes = parent.prepareAttributes();
		ErrorCallback error_callback = parent.getErrorCallback();
		//CommandType pcommand_type = CommandTypeByFirstChar(parent.getParentName().charAt(0));
		CommandType pcommand_type = parent.CurrentType;
		if(attributes == null){
			if(error_callback != null)
				error_callback.onError("No attribute set.", parent);
			return false;
		}
		
		while(mat.find())
		{
			String child = mat.group();
			int index = child.indexOf(':');
			String attr = "";
			String val = "";
			
			if (index > -1)
			{
				attr = child.substring(0, index);
				val = child.substring((index + 1), child.length());
				if (val.indexOf("'") == 0 && val.lastIndexOf("'") == val.length() - 1) val = val.substring(1, val.length() - 1);
			}
			else attr = child;
			
				
			if(attr.trim().equals(""))
				return false;
			
			if(!attributes.isExists(attr.replace("/", ""))) {
				if(error_callback != null)
					error_callback.onError(Command.MSG_ERROR_ATTRIBUTE1(attr), parent);
				return false;
			}
			else {
				if(!attr.contains("/")){
					if((!attributes.hasValue(attr, CommandType.GETTER) && pcommand_type == CommandType.GETTER) ||
							!attributes.hasValue(attr, CommandType.SETTER) && pcommand_type == CommandType.SETTER){
						
						if(error_callback != null)
							error_callback.onError(Command.MSG_ERROR_ATTRIBUTE2(attr), parent);
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private CommandType CommandTypeByFirstChar(char first_char)
	{
		if (first_char == '-')
			return CommandType.SETTER;
		else if (first_char == '@')
			return CommandType.GETTER;
		else
			return CommandType.NONE;
	}
	public static String MSG_ERROR_ATTRIBUTE1(String child){
		return String.format("Attribute name '%s' is invalid.", child);
	}
	public static String MSG_ERROR_ATTRIBUTE2(String child) {
		return String.format("Attribute name '%s' with value has no available.", child);
	}
	public static String MSG_ERROR_UNKNOWN(String strCommand){
		return String.format("Invalid Command: %s", strCommand);
	}
	
}