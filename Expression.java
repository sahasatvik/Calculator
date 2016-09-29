
class Expression {
	
	public static String number = "((-?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)\\s+"; 
	public static String negNumber = "(-\\d+(\\.\\d+)?([eE](-?)\\d+)?)\\s+";

	public static String[] operators = {"^", "%", "/", "*", "+", "-"};
	public static String[][] constants = {{"_e_", ("" + Math.E)},
					       {"_pi_", ("" + Math.PI)},
					       {"_phi_", ("" + (Math.sqrt(5.0) + 1.0) / 2.0)}};

	public static boolean isNumber (String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	public static String evaluate (String exp) throws Exception {
	
		exp = exp.replaceAll(number + negNumber, " $1 + $6 ");
		
		while (exp.indexOf("(") != -1) {
			int start = exp.indexOf("(");
			int end = indexOfMatchingBracket(exp, start, '(', ')');
			exp = exp.substring(0, start) + " " + evaluate(exp.substring(start + 1, end)) + " " + exp.substring(end + 1);
		}
		
		while (exp.indexOf("[") != -1) {
			exp += " ";
			int start = exp.indexOf("[");
			int end = indexOfMatchingBracket(exp, start, '[', ']') + 1;
			//while (exp.substring(start, exp.indexOf("[")).matches("[a-z]+"))
			//	start--;
			start -= 3;
			exp = exp.substring(0, start) +" "+ funcVal(exp.substring(start, end)) +" "+ exp.substring(end);
		}

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
					stack[i] = binaryExp(stack[x], op, stack[y]);
					stack[x] = "";
					stack[y] = "";
				}
			}
		}
		String result = "";
		for (String s : stack)
			result += s;
		return "" + Double.parseDouble(result.trim());
	}
	public static String binaryExp (String x, String op, String y) throws Exception {
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
	public static String funcVal (String exp) throws Exception {
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
