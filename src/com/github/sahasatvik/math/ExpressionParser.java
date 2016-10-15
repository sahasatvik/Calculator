
package com.github.sahasatvik.math;

public class ExpressionParser extends MathParser {
	
	public static String numberRegex = "(([+-]?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)"; 
	public static String signedNumberRegex = "([+-]\\d+(\\.\\d+)?([eE](-?)\\d+)?)";
	public static String assignmentRegex = "(\\s+)?(\\w+)(\\s+)(=)(.*)";

	public static String[] operators = {"^", "%", "/", "*", "+", "-"};
	
	public String[][] variables;
	public int numberOfVars;
	
	public ExpressionParser (int maxVars) {
		variables = new String[maxVars][2];
		numberOfVars = 0;
	}

	public void addVariable (String name, String value) {
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
	public String evaluate (String exp) throws ExpressionParserException {
		String result = exp;
		if (exp.length() == 0) {
			throw new NullExpressionException();
		} else if (isNumber(exp)) {
			return "" + Double.parseDouble(exp);
		} else if (exp.matches(assignmentRegex)) {
			String varName = exp.substring(0, exp.indexOf("=")).trim();
			String varValue = evaluate(exp.substring(exp.indexOf("=") + 1));
			addVariable(varName, varValue);
			exp = varValue;
		} else {
			exp = parseVariables(exp);
			exp = adjustNumberSpacing(exp);
			exp = parseParenthesis(exp);
			exp = parseFunctions(exp);
			exp = parseOperators(exp);
		} 
		
		try {
			result = "" + Double.parseDouble(exp);
		} catch (Exception e) {
			throw new ExpressionParserException(exp);
		}
		return result;
	}
	public String parseVariables (String exp) throws VariableNotFoundException {
		for (int i = 0; i < numberOfVars; i++) {
			exp = exp.replaceAll("<(\\s+)?" + variables[i][0] + "(\\s+)?>", variables[i][1]);
		}
		int start = exp.indexOf("<");
		int end = exp.indexOf(">");
		if (start != -1 && end != -1 && start < end) {
			throw new VariableNotFoundException(exp, exp.substring(start, end + 1));
		}
		return exp.trim();	
	}
	public String parseParenthesis (String exp) throws ExpressionParserException {
		String result = "";
		exp = " " + exp;
		while (exp.indexOf("(") != -1) {
			int start = exp.indexOf("(");
			int end = indexOfMatchingBracket(exp, start, '(', ')');
			result = evaluate(exp.substring(start + 1, end));
			if (exp.charAt(start - 1) == '-') {
				result = " ( -1 * ( " + result + " ) ) ";
				start--;
			}
			exp = exp.substring(0, start) + " " 
					    + result + " " 
					    + exp.substring(end + 1);
		}
		exp = adjustNumberSpacing(exp);
		return exp.trim();
	}
	public String parseFunctions (String exp) throws ExpressionParserException {
		String func = "";
		double x = 0.0;
		double result = 0.0;
		try {
			exp = exp.replaceAll(numberRegex + "\\s+!", " fct[$1] ");
			while (exp.indexOf("[") != -1) {
				int start = exp.indexOf("[");
				int end = indexOfMatchingBracket(exp, start, '[', ']');
				func = exp.substring(start - 3, start);
				x = Double.parseDouble(evaluate(exp.substring(start + 1, end)));
				result = solveUnaryFunction(func, x); 
				exp = exp.substring(0, start-3) + " " 
						    + result + " "
						    + exp.substring(end+1);
			}
		} catch (FunctionNotFoundException e) {
			throw new FunctionNotFoundException(exp, func);
		} catch (NullExpressionException e) {
			throw new MissingOperandException(exp, func + "[]");
		} catch (Exception e) {
			throw new ExpressionParserException(exp);
		}
		return exp.trim();
	}
	public String parseOperators (String exp) throws MissingOperandException {
		String[] stack = exp.split("\\s+");
		for (String op : operators) {
			for (int i = 0; i < stack.length; i++) {
				if (stack[i].equals(op)) {
					int x, y;
					x = y = i;
					while (x >= 0 && !isNumber(stack[x]))
						x--;
					while (y < stack.length && !isNumber(stack[y]))
						y++;
					try {
						double a = Double.parseDouble(stack[x]);
						double b = Double.parseDouble(stack[y]);
						stack[i] = "" + solveBinaryOperation(a, op, b);
					} catch (Exception e) {
						throw new MissingOperandException(exp, op);
					}
					stack[x] = "";
					stack[y] = "";
				}
			}
		}
		exp = "";
		for (String s : stack) {
			exp += s;
		}
		return exp.trim();
	}
	public static String adjustNumberSpacing (String exp) {
		exp = exp.replaceAll(numberRegex, " $0 ");
		exp = exp.replaceAll(numberRegex + "\\s+" + signedNumberRegex, " $1 + $6 ");
		return exp;	
	}
	public static int indexOfMatchingBracket (String str, int pos, char open, char close) 
								throws UnmatchedBracketsException {
		int tmp = pos;
		while (++pos < str.length()) {
			if (str.charAt(pos) == close)
				return pos;
			if (str.charAt(pos) == open)
				pos = indexOfMatchingBracket(str, pos, open, close);
		}
		if (pos >= str.length()) {
			throw new UnmatchedBracketsException(str, tmp);
		}
		return pos;
	}
}
