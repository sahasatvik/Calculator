
package com.github.sahasatvik.math;

public class VariableNotFoundException extends ExpressionParserException {
	String var;
	public VariableNotFoundException (String faultyExpression, String var) {
		super(faultyExpression);
		this.var = var;
	}
	public VariableNotFoundException (String var) {
		this("", var);
	}
	public String getVar () {
		return var;
	}
} 
