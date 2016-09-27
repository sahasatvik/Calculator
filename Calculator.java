import java.util.Scanner;

class Calculator {
	public static String[][] variables;
	public static int numberOfVars;
	public static String previousAns;
	public static void main (String[] args) {
		Scanner inp = new Scanner(System.in);
		String expression;
		previousAns = "";
		variables = new String[128][2];
		numberOfVars = 0;
		System.out.print("?> ");
		while (!(expression = inp.nextLine()).trim().equals("exit")) {			
			System.out.print("=> ");
			if (expression.indexOf("=") == -1) {
				previousAns = evaluate(expression);
				System.out.print(previousAns);
			} else {
				String varName = expression.substring(0, expression.indexOf("=")).trim();
				String varValue = evaluate(expression.substring(expression.indexOf("=")+1)).trim();
				addVariable(varName, varValue);
				previousAns = varValue;
				System.out.print(varName + " = " + varValue);
			}
			System.out.print("\n?> ");
		}
	}
	public static void addVariable (String name, String value) {
		for (int i = 0; i < numberOfVars; i++) {
			if (variables[i][0].equals(name)) {
				variables[i][1] = value;
				return;
			}
		}
		variables[numberOfVars][0] = name;
		variables[numberOfVars][1] = value;
		numberOfVars++;
	}
	public static String evaluate (String exp) {
		exp = exp.replaceAll("ans", previousAns);
		for (String[] line : Expression.constants) {
			exp = exp.replaceAll(line[0], line[1]);
		}
		for (int i = 0; i < numberOfVars; i++) {
			exp = exp.replaceAll(variables[i][0], variables[i][1]);
		}
		exp = exp.replaceAll("(-?)\\d+(\\.\\d+)?([eE]\\d+)?", " $0 ");
		try {
			return Expression.evaluate(exp);
		} catch (NumberFormatException e) {
			System.out.print("Incorrectly formed expression !");
		} catch (StringIndexOutOfBoundsException e) {
			System.out.print("Mismatched brackets !");
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.print("Dangling operator !");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
} 
