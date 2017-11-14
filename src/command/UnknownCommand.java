package command;
public interface UnknownCommand{
	void onUnknown(String data, int code, Object byWho);
}