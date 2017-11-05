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
	private Object byWho;

	public CommandTemplate(String parentName, CommandType command_type)
	{
		this.parentName = parentName;
		this.command_type = command_type;
		is_error = false;
		output = "";
		error = "";
		byWho = null;
		CurrentType = null;
	}
	
	public abstract void begin();
	//public abstract void beforeCall(CommandType what_type);
	public abstract void toSetCall(String attributeName, String value);
	public abstract void toGetCall(String attributeName, String value);
	public abstract CommandAttribute prepareAttributes();
	public abstract void end(CommandType command_type);
	public abstract void reset();
	
	public void setRequestBy(Object byWho){
		this.byWho = byWho;
	}
	/*public void setRequestBy(IRequest request) {
		
	}*/
	public Object getRequest() {
		return this.byWho;
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
			this.output = String.valueOf(output);
		else
			this.output += String.valueOf("\n" + output);
	}
	protected void writeOutput(String output) {
		this.output += String.valueOf(output);
	}
	protected void setErrorMessage(String msg) {
		error = msg;
		is_error = true;
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