import java.util.Scanner;

class Calculator {
	public static String previousAns;
	public static void main (String[] args) {
		Scanner inp = new Scanner(System.in);
		String expression;
		previousAns = "";
		System.out.print("?> ");
		while (!(expression = inp.nextLine()).trim().equals("exit")) {			
			System.out.print("=> ");
			if (expression.indexOf("=") == -1) {
				previousAns = evaluate(expression);
				System.out.print(previousAns);
			} else {
				String varName = expression.substring(0, expression.indexOf("=")).trim();
				String varValue = evaluate(expression.substring(expression.indexOf("=")+1)).trim();
				Expression.addVariable(varName, varValue);
				previousAns = varValue;
				System.out.print(varName + " = " + varValue);
			}
			System.out.print("\n?> ");
		}
	}
	public static String evaluate (String exp) {
		exp = exp.replaceAll("ans", previousAns);
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
