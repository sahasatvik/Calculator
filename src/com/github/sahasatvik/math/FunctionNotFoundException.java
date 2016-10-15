
package com.github.sahasatvik.math;

public class FunctionNotFoundException extends ExpressionParserException {
	String func;
	public FunctionNotFoundException (String faultyExpression, String func) {
		super(faultyExpression);
		this.func = func;
	}
	public FunctionNotFoundException (String func) {
		this("", func);
	}
	public String getFunc () {
		return func;
	}
} 
