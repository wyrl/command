package command;
public enum CommandType{
	NONE(0x00),
	GETTER(0x01),
	SETTER(0x02),
	BOTH(GETTER.toInt() | SETTER.toInt());

	private int num;
	CommandType(int n){ this.num = n;}
	int toInt(){ return num;}
}