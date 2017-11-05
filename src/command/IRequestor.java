package command;

public interface IRequestor {
	public void send(String data);
	public boolean isAvailable();
}
