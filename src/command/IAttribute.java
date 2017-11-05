package command;

public interface IAttribute {
	public IAttribute add(String name);
	public IAttribute in(CommandType command_type, boolean withValue);
}
