
package com.github.sahasatvik.math;

public class ExpressionParserException extends Exception {
	private String faultyExpression;
	public ExpressionParserException (String faultyExpression) {
		super("ExpressionParserException");
		this.faultyExpression = faultyExpression;
	}
	public String getFaultyExpression () {
		return faultyExpression;
	}
}
