
package com.github.sahasatvik.math;

/**
 * ExpressionParser provides methods for evaluating mathematical expressions, specifically
 * tailored for parsing with this library. ExpressionParser supports basic arihmetic operators,
 * parenthesized expressions, variable substitution as well as basic functions.
 *
 * 	@author		Satvik Saha
 * 	@version	1.0, 16/10/2016
 * 	@see		com.github.sahasatvik.math.MathParser
 * 	@since		1.o
 */

public class ExpressionParser extends MathParser {
	
	protected static final String numberRegex = "(([+-]?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)"; 
	protected static final String signedNumberRegex = "([+-]\\d+(\\.\\d+)?([eE](-?)\\d+)?)";
	protected static final String assignmentRegex = "(\\s+)?(\\w+)(\\s+)(=)(.*)";

	protected static final String[] operators = {"^", "%", "/", "*", "+", "-"};
	
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
	protected String parseVariables (String exp) throws VariableNotFoundException {
		for (int i = 0; i < numberOfVars; i++) {
			exp = exp.replaceAll("<(\\s+)?" + variables[i][0] + "(\\s+)?>", variables[i][1]);
		}
		int start = exp.indexOf("<");
		int end = exp.indexOf(">");
		if (start != -1 && end != -1 && start < end) {
			throw new VariableNotFoundException(exp, exp.substring(start, end + 1));
		}
		exp = adjustNumberSpacing(exp);
		return exp.trim();	
	}
	protected String parseParenthesis (String exp) throws ExpressionParserException {
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
	protected String parseFunctions (String exp) throws ExpressionParserException {
		String func = "";
		exp = " " + exp;
		double x = 0.0;
		String result = "";
		try {
			exp = exp.replaceAll(numberRegex + "\\s+!", " fct[$1] ");
			while (exp.indexOf("[") != -1) {
				int start = exp.indexOf("[");
				int end = indexOfMatchingBracket(exp, start, '[', ']');
				func = exp.substring(start - 3, start);
				x = Double.parseDouble(evaluate(exp.substring(start + 1, end)));
				result = "" + solveUnaryFunction(func, x); 
				if (exp.charAt(start - 4) == '-') {
					result = evaluate(" ( -1 * ( " + result + " ) ) ");
					start--;
				}
				exp = exp.substring(0, start-3) + " " 
						    + result + " "
						    + exp.substring(end+1);
			}
		} catch (FunctionNotFoundException e) {
			throw new FunctionNotFoundException(exp, func);
		} catch (NullExpressionException e) {
			throw new MissingOperandException(exp, func + "[]");
		} catch (ExpressionParserException e) {
			throw e;
		} catch (Exception e) {
			throw new ExpressionParserException(exp);
		}
		exp = adjustNumberSpacing(exp);
		return exp.trim();
	}
	protected String parseOperators (String exp) throws MissingOperandException {
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
	protected static String adjustNumberSpacing (String exp) {
		exp = exp.replaceAll(numberRegex, " $0 ");
		exp = exp.replaceAll(numberRegex + "\\s+" + signedNumberRegex, " $1 + $6 ");
		return exp;	
	}
	protected static int indexOfMatchingBracket (String str, int pos, char open, char close) 
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
