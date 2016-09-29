
class Expression {
	
	public static String numberRegex = "((-?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)"; 
	public static String negNumberRegex = "(-\\d+(\\.\\d+)?([eE](-?)\\d+)?)";

	public static String[] operators = {"^", "%", "/", "*", "+", "-"};
	
	public static String[][] variables;
	public static int numberOfVars;
	
	static {
		variables = new String[128][2];
		numberOfVars = 0;
		addVariable("_e_", ("" + Math.E));
		addVariable("_pi_", ("" + Math.PI));
		addVariable("_phi_", ("" + (Math.sqrt(5.0) + 1.0) / 2.0));
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
	public static boolean isNumber (String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static String evaluate (String exp) throws Exception {
		for (int i = 0; i < numberOfVars; i++) {
			exp = exp.replaceAll(variables[i][0], variables[i][1]);
		}
		exp = exp.replaceAll(numberRegex, " $0 ");
		exp = exp.replaceAll(numberRegex + "\\s+" + negNumberRegex, " $1 + $6 ");
		exp = parseParenthesis(exp);
		exp = parseFunctions(exp);
		exp = parseOperators(exp);
		return "" + Double.parseDouble(exp);
	}
	public static String parseParenthesis (String exp) throws Exception {
		while (exp.indexOf("(") != -1) {
			int start = exp.indexOf("(");
			int end = indexOfMatchingBracket(exp, start, '(', ')');
			exp = exp.substring(0, start) + " " + evaluate(exp.substring(start + 1, end)) + " " + exp.substring(end + 1);
		}
		return exp.trim();
	}
	public static String parseFunctions (String exp) throws Exception {
		exp = exp.replaceAll(numberRegex + "\\s+!", " fct[$1] ");
		while (exp.indexOf("[") != -1) {
			int start = exp.indexOf("[");
			int end = indexOfMatchingBracket(exp, start, '[', ']') + 1;
			//while (exp.substring(start, exp.indexOf("[")).matches("[a-z]+"))
			//	start--;
			start -= 3;
			exp = exp.substring(0, start) +" "+ parseUnaryFunction(exp.substring(start, end)) +" "+ exp.substring(end);
		}
		return exp.trim();
	}
	public static String parseOperators (String exp) throws Exception {
		
		String[] stack = exp.split(" ");
		
		for (String op : operators) {
			for (int i = 1; i <= stack.length - 2; i++) {
				if (stack[i].equals(op)) {
					int x, y;
					x = y = i;
					while (x >= 0 && !isNumber(stack[x]))
						x--;
					while (y < stack.length && !isNumber(stack[y]))
						y++;
					stack[i] = parseBinaryExpression(stack[x], op, stack[y]);
					stack[x] = "";
					stack[y] = "";
				}
			}
		}
		exp = "";
		for (String s : stack)
			exp += s;
		return exp.trim();
	}
	public static String parseBinaryExpression (String x, String op, String y) throws Exception {
		double a = Double.parseDouble(x);
		double b = Double.parseDouble(y);
		double result = 0.0;
		if (op.equals("^")) {
			result = Math.pow(a, b);
		} else if (op.equals("%")) {
			result = a % b;
		} else if (op.equals("/")) {
			result = a / b;
		} else if (op.equals("*")) {
			result = a * b;
		} else if (op.equals("+")) {
			result = a + b;
		} else if (op.equals("-")) {
			result = a - b;
		}
		return "" + result;
	}
	public static String parseUnaryFunction (String exp) throws Exception {
		int start = exp.indexOf("[");
		int end = exp.length() - 1;
		double x = Double.parseDouble(evaluate(exp.substring(start + 1, end)));
		String func = exp.substring(0, start).trim();
		double result = 0.0;
		if (func.equals("sin")) {
			result = Math.sin(Math.toRadians(x));
		} else if (func.equals("cos")) {
			result = Math.cos(Math.toRadians(x));
		} else if (func.equals("tan")) {
			result = Math.tan(Math.toRadians(x));
		} else if (func.equals("abs")) {
			result = Math.abs(x);
		} else if (func.equals("csc")) {
			result = 1.0/Math.sin(Math.toRadians(x));
		} else if (func.equals("sec")) {
			result = 1.0/Math.cos(Math.toRadians(x));
		} else if (func.equals("ctn")) {
			result = 1.0/Math.tan(Math.toRadians(x));
		} else if (func.equals("fct")) {
			result = factorial(x);
		} 
		return "" + result;
	}
	public static int indexOfMatchingBracket (String str, int pos, char open, char close) {
		int end = pos;
		while (pos++ < str.length()) {
			if (str.charAt(pos) == close)
				return pos;
			if (str.charAt(pos) == open)
				pos = indexOfMatchingBracket(str, pos, open, close);
		}
		return pos;
	}
	public static double factorial (double x) {
		if (x == 0)
			return 1;
		double n = 1;
		while (x > 0)
			n *= x--;
		return n;
	}
}
