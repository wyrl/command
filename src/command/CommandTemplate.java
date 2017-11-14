package command;
public abstract class CommandTemplate
{
	private String parentName;
	CommandCallback callback;
	ErrorCallback error_callback;
	CommandType command_type;
	private String output;
	protected String error;
	protected boolean is_error;
	protected CommandType CurrentType;
	private IRequestor requestor;
	
	private String commandString;

	public CommandTemplate(String parentName, CommandType command_type)
	{
		this.parentName = parentName;
		this.command_type = command_type;
		is_error = false;
		output = "";
		error = "";
		requestor = null;
		CurrentType = null;
	}
	
	public abstract void begin();
	public abstract void toSetCall(String attributeName, String value);
	public abstract void toGetCall(String attributeName, String value);
	public abstract CommandAttribute prepareAttributes();
	public abstract void end(CommandType command_type);
	public abstract void reset();
	
	public void setRequestBy(IRequestor requestor){
		this.requestor = requestor;
	}
	public IRequestor getRequestor() {
		return this.requestor;
	}
	public String getParentName()
	{
		return parentName;
	}
	
	public CommandType getCommandType()
	{
		return command_type;
	}
	
	public void setCallback(CommandCallback callback){
		this.callback = callback;
	}
	
	public CommandCallback getCallback()
	{
		return callback;
	}
	
	public void setErrorCallback(ErrorCallback error_callback){
		this.error_callback = error_callback;
	}
	
	public ErrorCallback getErrorCallback(){
		return this.error_callback;
	}
	
	protected void appendOutput(String output){
		if(this.output.equals(""))
			this.output = output;
		else
			this.output += "\n" + output;
	}
	protected void writeOutput(String output) {
		this.output += output;
	}
	protected void setErrorMessage(String msg) {
		error = msg;
		is_error = true;
	}

	public void setLastCommandString(String commandString) {
		this.commandString = commandString;
	}
	
	public String getLastCommandString() {
		return commandString;
	}
	
	public boolean isError()
	{
		return is_error;
	}
	public String toOutput()
	{
		return output;
	}
	public String toError()
	{
		return error;
	}
	
	protected void default_reset() {
		output = "";
		is_error = false;
		error = "";
	}
}