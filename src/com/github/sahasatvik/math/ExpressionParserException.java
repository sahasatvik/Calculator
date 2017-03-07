
package com.github.sahasatvik.math;

/**
 * Superclass of all Exceptions thrown by ExpressionParser.
 *
 * 	@author		Satvik Saha
 * 	@version	0.1.0, 16/10/2016
 * 	@see		com.github.sahasatvik.math.ExpressionParser
 * 	@since		0.1.0
 */

public class ExpressionParserException extends Exception {
	private String faultyExpression;

	/**
	 * Constructor of ExpressionParserException.
	 *
	 * 	@param	faultyExpression	the expression which could not be parsed
	 * 	@since	0.1.0
	 */

	public ExpressionParserException (String faultyExpression) {
		super("ExpressionParserException");
		/* Store the bad expression */
		this.faultyExpression = faultyExpression;
	}

	/**
	 * Gets the expression which could not be parsed.
	 *
	 * 	@return				the expression which could not be parsed
	 * 	@since	0.1.0
	 */
	public String getFaultyExpression () {
		return faultyExpression;
	}
}
