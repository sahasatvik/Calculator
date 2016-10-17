
package com.github.sahasatvik.math;

/**
 * ExpressionParser provides methods for evaluating mathematical expressions, specifically
 * tailored for parsing with this library. ExpressionParser supports basic arithmetic operators,
 * parenthesized expressions, variable substitution as well as basic functions.
 *
 * 	@author		Satvik Saha
 * 	@version	1.0, 16/10/2016
 * 	@see		com.github.sahasatvik.math.MathParser
 * 	@since		1.o
 */

public class ExpressionParser extends MathParser {
	
	/**
	 *  Regex which matches a number. It may be signed or use scientific notation.
	 */
	protected static final String numberRegex = "(([+-]?)\\d+(\\.\\d+)?([eE](-?)\\d+)?)";

	/**
	 * Regex which matches a signed number. It may use scientific notation.
	 */
	protected static final String signedNumberRegex = "([+-]\\d+(\\.\\d+)?([eE](-?)\\d+)?)";

	/**
	 * Regex which matches an assignment statement. It is simply a word, followed by an 
	 * equals sign (=) and an expression.
	 */
	protected static final String assignmentRegex = "(\\s+)?(\\w+)(\\s+)(=)(.*)";


	/**
	 * Array of supported operators. The operators are arranged in their order of precedence.
	 * Thus, operators to the left will be evaluated before those to the right.
	 */
	protected static final String[] operators = {"^", "%", "/", "*", "+", "-"};
	

	/**
	 * Array of variables maintained by an ExpressionParser object. The first String in each
	 * line stores the variable name, whicle the second stores the value.
	 */
	public String[][] variables;

	/**
	 * Index of the last variable in the 'variables' array. Elements in the 'variables' array 
	 * after this index are all blank, so they are not parsed during expression evaluation.
	 */
	public int numberOfVars;
	

	/**
	 * Constructor of ExpressionParser. This constructor initializes the variable cache with
	 * the specified maximum size.
	 *
	 * 	@param	maxVars			the maximum number of variables to be stored
	 * 	@since	1.0
	 */

	public ExpressionParser (int maxVars) {
		variables = new String[maxVars][2];
		numberOfVars = 0;
	}

	/**
	 * Adds a variable to the variable cache. This method accepts the variable 
	 * name, as well as the String each occurrence is to be substituted with.
	 *
	 * 	@param	name			the name of the variable 
	 * 	@param	value			the value the variable holds
	 * 	@since	1.0
	 */

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

	/**
	 * Evaluates a String representation of a mathematical expression into a
	 * number (stored as a String).
	 *
	 * 	@param	exp			the expression to be evaluated
	 * 	@return				the result after evaluating the expression
	 *	@throws	com.github.sahasatvik.math.NullExpressionException
	 *					thrown when the expression is empty
	 *	@throws	com.github.sahasatvik.math.ExpressionParserException
	 *					thrown when the expression cannot be parsed
	 *	@see	#addVariable(String, String)
	 *	@see	#parseVariables(String)
	 *	@see	#parseParenthesis(String)
	 *	@see	#parseFunctions(String)
	 *	@see	#parseOperators(String)
	 *	@since	1.0
	 */

	public String evaluate (String exp) throws ExpressionParserException {
		String result = exp;
		if (exp.trim().length() == 0) {
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

	/**
	 * Substitutes all instances of the variables in the cache with their values.
	 * A variable name present in the expression must be enclosed within angled brackets
	 * (<code>&#60;</code>, <code>&#62;</code>) in order to be recognized. 
	 * For example, if <code>x = 10.0</code>, then all instances of <code>&#60;x&#62;</code> 
	 * will be replaced with <code>10.0</code>
	 *
	 * 	@param	exp			the expression to be parsed
	 * 	@return				the expression after substituting known values
	 * 					of variables stored in the cache
	 * 	@throws	com.github.sahasatvik.math.VariableNotFoundException
	 * 					thrown when an unrecognized variable name is 
	 * 					found in the expression
	 * 	@since	1.0
	 */

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

	/**
	 * Substitutes expressions within parenthesis (<code>(</code>, <code>)</code>) with their results.
	 * This ensures that while evaluating an expression containing parenthesized parts, those
	 * parenthesized parts are evaluated first. This is done so that ExpressionParser follows the
	 * BODMAS rule.
	 *
	 * 	@param	exp			the expression to be parsed
	 * 	@return				the expression such that all parenthesized parts
	 * 					have been evaluated
	 * 	@throws	com.github.sahasatvik.math.UnmatchedBracketsException
	 * 					thrown when brackets in the expression are not
	 * 					closed
	 * 	@throws	com.github.sahasatvik.math.ExpressionParserException
	 * 					thrown if the parenthesized sections cannot be
	 * 					parsed
	 * 	@see	#indexOfMatchingBracket(String, int, char, char)
	 * 	@since	1.0
	 */

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

	/**
	 * Substitutes all occurrences of supported mathematical functions with their result.
	 * A function must be present in the expression in the following format :
	 * <code>function_name[function_argument]</code>, where the function argument can also 
	 * be an expression. The function name must be exactly 3 characters long, and be
	 * immediately followed by a sqaure bracket (<code>[</code>). 
	 * See {@link com.github.sahasatvik.math.MathParser#solveUnaryFunction(String, double)} for a 
	 * list of supported function names.
	 *
	 * 	@param	exp			the expression to be parsed
	 * 	@return				the expression such that all instances of
	 * 					functions are evaluated
	 * 	@throws	com.github.sahasatvik.math.MissingOperandException
	 * 					thrown if there is no function argument
	 * 	@throws	com.github.sahasatvik.math.FunctionNotFoundException
	 * 					thrown when an unrecognized function name
	 * 					is found in the expression
	 * 	@throws	com.github.sahasatvik.math.UnmatchedBracketsException
	 * 					thrown when a square bracket is not closed
	 * 	@throws	com.github.sahasatvik.math.ExpressionParserException
	 * 					thrown if the function argument cannot be
	 * 					parsed
	 * 	@see	com.github.sahasatvik.math.MathParser#solveUnaryFunction(String, double)
	 * 	@since	1.0
	 */

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

	/**
	 * Substitutes all binary expressions involving arithmetic operators with their result.
	 * Operations are performed following the BODMAS rule. The resultant parsed String
	 * is free of all operators, thus containing only numbers.
	 * See {@link com.github.sahasatvik.math.MathParser#solveBinaryOperation(double, String, double)}
	 * for a list of supported operators. See {@link #operators}, which defines the order of 
	 * operations.
	 *
	 * 	@param	exp			the expression to be parsed
	 * 	@return				the expression such that all arithmetic operations
	 * 					have been carried out
	 * 	@throws	com.github.sahasatvik.math.MissingOperandException
	 * 					thrown if a binary operator is missing an operand
	 * 	@see	com.github.sahasatvik.math.MathParser#solveBinaryOperation(double, String, double)
	 * 	@since	1.0
	 */

	protected String parseOperators (String exp) throws MissingOperandException {
		int leftIndex, rightIndex;

		/* Split the expression into a stack of operators and operands */
		String[] stack = exp.split("\\s+");

		for (String op : operators) {
			for (int i = 0; i < stack.length; i++) {
				if (stack[i].equals(op)) {
					leftIndex = rightIndex = i;
					while (leftIndex >= 0 && !isNumber(stack[leftIndex]))
						leftIndex--;
					while (rightIndex < stack.length && !isNumber(stack[rightIndex]))
						rightIndex++;
					try {
						double left = Double.parseDouble(stack[leftIndex]);
						double right = Double.parseDouble(stack[rightIndex]);
						stack[i] = "" + solveBinaryOperation(left, op, right);
					} catch (Exception e) {
						throw new MissingOperandException(exp, op);
					}
					stack[leftIndex] = stack[rightIndex] = "";
				}
			}
		}
		exp = "";

		/* Recombine the stack into the solved expression */
		for (String s : stack) {
			exp += s;
		}
		return exp.trim();
	}

	/**
	 * Adjusts the spacings between numbers, variables, functions, operators, etc in an expression.
	 * Each number will be enclosed withhin a 'buffer' of spaces. Instances of signed numbers 
	 * immediately following anothoer number will be interpreted as their sum.
	 * (<code>	1  -1</code> is simply <code>1 + -1</code>)
	 *
	 * 	@param	exp			the expression to be parsed
	 * 	@return				the expression with adjusted spacing
	 * 	@since	1.0
	 */

	protected static String adjustNumberSpacing (String exp) {
		exp = exp.replaceAll(numberRegex, " $0 ");
		exp = exp.replaceAll(numberRegex + "\\s+" + signedNumberRegex, " $1 + $6 ");
		return exp;	
	}

	/**
	 * Finds the index of a matching closing bracket in a String, given the index of the 
	 * opening one. This method can also be given any characters as opening and closing brackets.
	 * Nesting of brackets has also been dealt with.
	 *
	 * 	@param	str			the String containing the brackets
	 * 	@param	pos			the index of the opening bracket
	 * 	@param	open			the character to be recognized as an opening bracket
	 * 	@param	close			the character to be recognized as a closing bracket
	 * 	@return				the index of the matching closing bracket
	 * 	@throws	com.github.sahasatvik.math.UnmatchedBracketsException
	 * 					thrown if the specified opening bracket is unclosed
	 * 	@since 1.0
	 */

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
