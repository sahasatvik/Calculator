
package com.github.sahasatvik.math;

public class ExpressionParser extends MathParser {
	
	public static String numberRegex = "((-?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)"; 
	public static String negNumberRegex = "(-\\d+(\\.\\d+)?([eE](-?)\\d+)?)";
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
	public String evaluate (String exp) throws Exception {
		if (exp.matches(assignmentRegex)) {
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
		return "" + Double.parseDouble(exp);
	}
	public String parseVariables (String exp) throws Exception {
		for (int i = 0; i < numberOfVars; i++) {
			exp = exp.replaceAll("<" + variables[i][0] + ">", variables[i][1]);
		}
		return exp.trim();	
	}
	public String parseParenthesis (String exp) throws Exception {
		while (exp.indexOf("(") != -1) {
			int start = exp.indexOf("(");
			int end = indexOfMatchingBracket(exp, start, '(', ')');
			String result = evaluate(exp.substring(start + 1, end));
			exp = exp.substring(0, start) + " " + result + " " + exp.substring(end + 1);
		}
		return exp.trim();
	}
	public String parseFunctions (String exp) throws Exception {
		exp = exp.replaceAll(numberRegex + "\\s+!", " fct[$1] ");
		while (exp.indexOf("[") != -1) {
			int start = exp.indexOf("[");
			int end = indexOfMatchingBracket(exp, start, '[', ']');
			double x = Double.parseDouble(evaluate(exp.substring(start + 1, end)));
			String func = exp.substring(start - 3, start);
			exp = exp.substring(0, start-3) + " " + solveUnaryFunction(func, x) + " " + exp.substring(end+1);
		}
		return exp.trim();
	}
	public String parseOperators (String exp) throws Exception {
		String[] stack = exp.split(" ");
		for (String op : operators) {
			for (int i = 1; i < stack.length; i++) {
				if (stack[i].equals(op)) {
					int x, y;
					x = y = i;
					while (x >= 0 && !isNumber(stack[x]))
						x--;
					while (y < stack.length && !isNumber(stack[y]))
						y++;
					double a = Double.parseDouble(stack[x]);
					double b = Double.parseDouble(stack[y]);
					stack[i] = solveBinaryOperation(a, op, b);
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
	public static String adjustNumberSpacing (String exp) {
		exp = exp.replaceAll(numberRegex, " $0 ");
		exp = exp.replaceAll(numberRegex + "\\s+" + negNumberRegex, " $1 + $6 ");
		return exp;	
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
}
