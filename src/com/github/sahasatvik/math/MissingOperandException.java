
package com.github.sahasatvik.math;

public class MissingOperandException extends ExpressionParserException {
	String op;
	public MissingOperandException (String faultyExpression, String op) {
		super(faultyExpression);
		this.op = op;
	}
	public String getOperator () {
		return op;
	}
} 
