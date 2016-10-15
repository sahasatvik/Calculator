
public class CommandNotFoundException extends Exception {
	String command;
	public CommandNotFoundException (String var) {
		super("CommandNotFoundException");
		this.command = command;
	}
	public String getCommand () {
		return command;
	}
} 
