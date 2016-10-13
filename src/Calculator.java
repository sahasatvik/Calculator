import java.util.Scanner;
import com.github.sahasatvik.math.ExpressionParser;

class Calculator {
	public static String commandRegex = "(\\s+)?(/)(.*)";

	public static String previousAns;
	public static ExpressionParser expParser;
	public static void main (String[] args) {
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
				parseCommand(command);
				continue;
			}
			try {
				previousAns = evaluate(expression);
				System.out.print("=> " + previousAns);
			} catch (NumberFormatException e) {
				System.out.print("!> Incorrectly formed expression !");
			} catch (StringIndexOutOfBoundsException e) {
				System.out.print("!> Mismatched brackets !");
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.print("!> Dangling operator !");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static String evaluate (String exp) throws Exception {
		exp = exp.replaceAll("<(\\s+)?ans(\\s+)?>", previousAns);
		return expParser.evaluate(exp);
	}
	public static void parseCommand (String command) {
		if (command.equals("exit")) {
			System.out.print("$> Exiting !");
			System.exit(0);
		} else if (command.equals("list")) {
			System.out.print("$> Try : \n");
			System.out.print("\n	/list funcs	-	      list available functions");
			System.out.print("\n	/list vars	-	      list available variables");
		} else if (command.equals("list vars")) {
			System.out.print("$> Variables : \n");
			for (int i = 0; i < expParser.numberOfVars; i++) {
				System.out.printf("%n\t%s\t\t=%30s", expParser.variables[i][0], expParser.variables[i][1]);
			}
		} else if (command.equals("list funcs")) {
			System.out.print("$ Functions : \n");
			System.out.print("\n	abs[ x ]	-	      absolute value of <x>");
			System.out.print("\n	fct[ x ] or x!	-	      factorial of <x>");
			System.out.print("\n	deg[ x ]	-	      convert <x> to degrees from radians");
			System.out.print("\n	rad[ x ]	-	      convert <x> to radians from degrees");
			System.out.print("\n	        	_            ");
			System.out.print("\n	sin[ x ]	 |	      ");
			System.out.print("\n	cos[ x ]	 |	      ");
			System.out.print("\n	tan[ x ]	  >-	      trigonometric functions");
			System.out.print("\n	csc[ x ]	 |	         ( <x> in radians )");
			System.out.print("\n	sec[ x ]	 |	      ");
			System.out.print("\n	ctn[ x ]	 |	      ");
			System.out.print("\n	        	~             ");
		} else {
			System.out.print("!> Unknown Command !");
		}
	}
} 
