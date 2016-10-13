import java.util.Scanner;
import com.github.sahasatvik.math.ExpressionParser;

class Calculator {
	public static String previousAns;
	public static ExpressionParser expParser;
	public static void main (String[] args) {
		Scanner inp = new Scanner(System.in);
		expParser = new ExpressionParser(32);

		expParser.addVariable("e", ("" + Math.E));
		expParser.addVariable("pi", ("" + Math.PI));
		expParser.addVariable("phi", ("" + (Math.sqrt(5.0) + 1.0) / 2.0));
		
		String expression;
		previousAns = "";
		System.out.print("?> ");
		while (!(expression = inp.nextLine()).trim().equals("exit")) {
			System.out.print("=> ");
			previousAns = evaluate(expression);
			System.out.print(previousAns);
			System.out.print("\n?> ");
		}
	}
	public static String evaluate (String exp) {
		exp = exp.replaceAll("ans", previousAns);
		try {
			return expParser.evaluate(exp);
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
