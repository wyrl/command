package command.demo.commands;

import command.CommandAttribute;
import command.CommandType;
import command.CommandTemplate;
public class PersonCommand extends CommandTemplate{
	private String firstname;
	private String lastname;
	private int age;
	private CommandAttribute attributes;
	
	
	public PersonCommand(CommandType command_type) {
		super("person", command_type);
		// TODO Auto-generated constructor stub
		setAttributes();
		reset();
	}
	
	private void setAttributes() {
		attributes = new CommandAttribute();
		attributes.add("firstname").in(CommandType.SETTER, true);
		attributes.add("lastname").in(CommandType.SETTER, true);
		attributes.add("age").in(CommandType.SETTER, true);
	}
	
	@Override
	public void begin() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void toSetCall(String name, String value) {
		// TODO Auto-generated method stub
		switch(name) {
			case "firstname":
				firstname = value;
				break;
			case "lastname":
				lastname = value;
				break;
			case "age":
				age = Integer.parseInt(value);
				break;
			
		}
	}

	@Override
	public void toGetCall(String child, String value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public CommandAttribute prepareAttributes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void end(CommandType command_type) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		default_reset();
		firstname = "";
		lastname = "";
		age = -1;
	}

}
