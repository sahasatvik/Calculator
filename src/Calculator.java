import java.util.Scanner;
import com.github.sahasatvik.math.*;

class Calculator {
	public static String commandRegex = "(\\s+)?(/)(.*)";

	public static String previousAns;
	public static ExpressionParser expParser;
	public static void main (String[] args) {
		
		System.out.print("\nCalculator by Satvik Saha");
		System.out.print("\n-------------------------");
		System.out.print("\n   An up-to-date version of Calculator can be found at : ");
		System.out.print("\n      https://github.com/sahasatvik/Calculator");
		System.out.print("\n");
		System.out.print("\n   Type  /help  to read a guide on how to use this program.");
		System.out.print("\n");
		
		Scanner inp = new Scanner(System.in);
		
		expParser = new ExpressionParser(32);
		expParser.addVariable("e", ("" + Math.E));
		expParser.addVariable("pi", ("" + Math.PI));
		expParser.addVariable("phi", ("" + (Math.sqrt(5.0) + 1.0) / 2.0));
		
		String expression;
		String command;
		previousAns = "";
		while (true) {
			System.out.print("\n?> ");
			expression = inp.nextLine().trim();
			if (expression.matches(commandRegex)) {
				command = expression.substring(expression.indexOf("/") + 1).trim();
				try {
					parseCommand(command);
				} catch (CommandNotFoundException e) {
					System.out.print("!> Command " + e.getCommand() + " not found !");
					System.out.print("\n   Try  /list  for a complete list of available commands.");
				}
				continue;
			}
			try {
				previousAns = evaluate(expression);
				System.out.print("=> " + previousAns);
			} catch (NullExpressionException e) {
				System.out.print("!> Null Expreesion !");
			} catch (MissingOperandException e) {
				System.out.print("!> Missing operand to " + e.getOperator() + " !");
			} catch (VariableNotFoundException e) {
				System.out.print("!> Variable " + e.getVar() + " not found !");
				System.out.print("\n   Try  /list vars  for a complete list of available variables.");
			} catch (FunctionNotFoundException e) {
				System.out.print("!> Function " + e.getFunc() + "[] not found !");
				System.out.print("\n   Try  /list funcs  for a complete list of available functions.");
			} catch (UnmatchedBracketsException e) {
				System.out.print("!> Unmatched brackets in expression !");
				System.out.print("\n   " + e.getFaultyExpression());
				System.out.print("\n   ");
				for (int i = 0; i < e.getIndexOfBracket(); i++) {
					System.out.print(" ");
				}
				System.out.print("^");
			} catch (ExpressionParserException e) {
				System.out.print("!> Invalid Expression !");
			}
		}
	}
	public static String evaluate (String exp) throws ExpressionParserException {
		exp = exp.replaceAll("<(\\s+)?ans(\\s+)?>", previousAns);
		return expParser.evaluate(exp);
	}
	public static void parseCommand (String command) throws CommandNotFoundException {
		if (command.equals("exit")) {
			System.out.print("$> Exiting !");
			System.exit(0);
		} else if (command.equals("help")) {
			System.out.print("$> Calculator Helptext");
			System.out.print("\n ~~~~~~~~~~~~~~~~~~~");
			System.out.print("\n	    Welcome to  'Calculator', a simple java application written to");
			System.out.print("\n	evaluate mathematical expressions.");
			System.out.print("\n	    This program displays a prompt (?>), after which you can enter");
			System.out.print("\n	a mathematical expression. 'Calculator' will display the result,");
			System.out.print("\n	or point out errors in the expression.");
			System.out.print("\n");
			System.out.print("\n	    'Calculator' can evaluate simple arithmetic expressions, using the");
			System.out.print("\n	operators (+, -, *, /, ^(power)), as well as parenthesis ('(', ')').");
			System.out.print("\n	'Calculator' follows the BODMAS rule.");
			System.out.print("\n");
			System.out.print("\n	    Following are some valid expressions : ");
			System.out.print("\n		1 + 1			=>		 2.0");
			System.out.print("\n		1 * (2 + 3)		=>		 5.0");
			System.out.print("\n		10 * (64 ^ -0.5)	=>		1.25");
			System.out.print("\n");
			System.out.print("\n	    For help on more advanced topics, try entering the following : ");
			System.out.print("\n		/help vars		>	help on Variables");
			System.out.print("\n		/help funcs		>	help on Functions");
			System.out.print("\n		/help cmds		>	help on Commands");
			System.out.print("\n");
			System.out.print("\n	    Enter '/list' for a complete list of valid commands.");
		} else if (command.equals("help vars")) {
			System.out.print("\n$> Variables");
			System.out.print("\n   `````````");
			System.out.print("\n	    'Calculator' can also store user-defined variables.");
			System.out.print("\n	The syntax for assigning and using variables is as follows : ");
			System.out.print("\n		var = value		>	assign 'value' to 'var'");
			System.out.print("\n		<var>			>	<var> will be replaced");
			System.out.print("\n						its value.");
			System.out.print("\n");
			System.out.print("\n	    Following are some valid uses of variables : ");
			System.out.print("\n		x = 3			=>		 3.0");
			System.out.print("\n		y = <x> + 1		=>		 4.0");
			System.out.print("\n		(<x>^2 + <y>^2)^0.5	=>		 5.0 ");
			System.out.print("\n");
			System.out.print("\n	    Nesting of assignments is also supported, as follows : ");
			System.out.print("\n		x = 1 + (y = 1)		=>		 2.0");
			System.out.print("\n		<x>			=>		 2.0");
			System.out.print("\n		<y>			=>		 1.0");
			System.out.print("\n");
			System.out.print("\n	    A special variable <ans> stores the previous expression.");
			System.out.print("\n	Thus, the following is valid : ");
			System.out.print("\n		1 * 2 * 3 * 4		=>		24.0");
			System.out.print("\n		<ans> * 5		=>	       120.0");
			System.out.print("\n");
			System.out.print("\n	    Enter '/list vars' for a list of stored variables.");
		} else if (command.equals("help funcs")) {
			System.out.print("\n$> Functions");
			System.out.print("\n   `````````");
			System.out.print("\n	    'Calculator' supports the use of some basic functions.");
			System.out.print("\n	They can be used with the following syntax : ");
			System.out.print("\n		fnc[ value ]		>	evaluate 'fnc' of 'value'");
			System.out.print("\n");
			System.out.print("\n	    Following are some valid uses of functions : ");
			System.out.print("\n		sin[<pi> / 2]		=>		 1.0");
			System.out.print("\n		1 + abs[2 - 3]		=>		 2.0");
			System.out.print("\n		log[<e> ^ 3]		=>		 3.0");
			System.out.print("\n");
			System.out.print("\n 	    Enter '/list funcs' for a list of valid functions.");
		} else if (command.equals("help cmds")) {
			System.out.print("\n$> Commands");
			System.out.print("\n   ````````");
			System.out.print("\n	    'Calculator' interprets expressions starting with '/' as");
			System.out.print("\n	'commands'. These are special expressions which are not parsed as");
			System.out.print("\n	mathematical expressions, but as instructions to the 'Calculator'.");
			System.out.print("\n");
			System.out.print("\n	    Enter '/list' for a complete list of valid commands.");
		} else if (command.equals("list") || command.equals("list cmds")) {
			System.out.print("$> Commands : \n");
			System.out.print("\n	/help				>	general help");
			System.out.print("\n	/help vars			>	help on Variables");
			System.out.print("\n	/help funcs			>	help on Functions");
			System.out.print("\n	/help cmds			>	help on Commands");
			System.out.print("\n	/list vars			>	list variables");
			System.out.print("\n	/list funcs			>	list functions");
			System.out.print("\n	/list cmds  or  /list		>	list commands");
			System.out.print("\n	/exit				>	exit Calculator");
		} else if (command.equals("list vars")) {
			System.out.print("$> Variables : \n");
			for (int i = 0; i < expParser.numberOfVars; i++) {
				System.out.printf("%n\t%s\t\t=%30s", expParser.variables[i][0], expParser.variables[i][1]);
			}
			System.out.printf("%n\t%s\t\t=%30s", "ans", previousAns);
		} else if (command.equals("list funcs")) {
			System.out.print("$ Functions : \n");
			System.out.print("\n	abs[ x ]	>	      absolute value of <x>");
			System.out.print("\n	exp[ x ]	>	      exponent of <x> (<e> ^ <x>)");
			System.out.print("\n	log[ x ]	>	      logarithm of <x> (base <e>)");
			System.out.print("\n	fct[ x ] or x!	>	      factorial of <x>");
			System.out.print("\n	deg[ x ]	>	      convert <x> to degrees from radians");
			System.out.print("\n	rad[ x ]	>	      convert <x> to radians from degrees");
			System.out.print("\n	        	_            ");
			System.out.print("\n	sin[ x ]	 |	      ");
			System.out.print("\n	cos[ x ]	 |	      ");
			System.out.print("\n	tan[ x ]	  >	      trigonometric functions");
			System.out.print("\n	csc[ x ]	 |	         ( <x> in radians )");
			System.out.print("\n	sec[ x ]	 |	      ");
			System.out.print("\n	ctn[ x ]	 |	      ");
			System.out.print("\n	        	~             ");
		} else {
			throw new CommandNotFoundException(command);
		}
	}
} 
