package command;
import java.util.ArrayList;
import java.util.Hashtable;

public class CommandAttribute implements IAttribute{	
	private static class AttributeType{
		public boolean withValue;
		public CommandType command_type;
		public AttributeType(CommandType command_type, boolean withValue) {
			this.command_type = command_type;
			this.withValue = withValue;
		}
	}
	
	
	
	private Hashtable<String, ArrayList<AttributeType>> list;
	private String lastName;
	
	
	
	public CommandAttribute(){
		list = new Hashtable<String, ArrayList<AttributeType>>();
	}
	
	@Override
	public IAttribute add(String name) {
		ArrayList<AttributeType> al = new ArrayList<AttributeType>();
		al.add(new AttributeType(CommandType.SETTER, false));
		al.add(new AttributeType(CommandType.GETTER, false));
		
		list.put(name, al);
		lastName = name;
		return this;
	}
	
	@Override
	public IAttribute in(CommandType command_type, boolean withValue) {
		// TODO Auto-generated method stub
		if(command_type == CommandType.SETTER)
			list.get(lastName).get(0).withValue = withValue;
		else if(command_type == CommandType.GETTER)
			list.get(lastName).get(1).withValue = withValue;
		else
			throw new UnsupportedOperationException("Only SETTER and GETTER supported.");
		
		return this;
	}
	
	public boolean isExists(String name)
	{
		return list.containsKey(name);
	}
	
	public boolean hasValue(String name, CommandType command_type) {
		if(command_type == CommandType.SETTER)
			return list.get(name).get(0).withValue;
		else if(command_type == CommandType.GETTER)
			return list.get(name).get(1).withValue;
		else
			return false;
	}
	
}
