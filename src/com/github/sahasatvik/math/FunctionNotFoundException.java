
package com.github.sahasatvik.math;

/**
 * Exception thrown when an unparsable function is passed to ExpressionParser.
 *
 * 	@author		Satvik Saha
 * 	@version	1.0, 16/10/2016
 * 	@see		com.github.sahasatvik.math.ExpressionParserException
 * 	@since		1.0
 */

public class FunctionNotFoundException extends ExpressionParserException {
	private String func;

	/**
	 * Constructor of FunctionNotFoundException. This constructor accepts the 
	 * invalid expression as well as the unrecognized function name.
	 *
	 * 	@param	faultyExpression	the expression which could not be parsed
	 * 	@param	func			the unrecognized function name 	
	 */

	public FunctionNotFoundException (String faultyExpression, String func) {
		super(faultyExpression);
		this.func = func;
	}

	/**
	 * Constructor of FunctionNotFoundException. This constructor accepts only
	 * the unrecognized function name.
	 *
	 * 	@param	func			the unrecognized function name
	 * 	@since	1.0
	 *
	 */ 

	public FunctionNotFoundException (String func) {
		this("", func);
	}

	/**
	 * Gets the unrecognized function name.
	 *
	 * 	@return 			the unrecognized function name
	 * 	@since	1.0
	 */

	public String getFunc () {
		return func;
	}
} 
