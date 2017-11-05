package command;

public interface CommandCallback
{
	void onCallBack(Object byWho, CommandTemplate pCommand);
}