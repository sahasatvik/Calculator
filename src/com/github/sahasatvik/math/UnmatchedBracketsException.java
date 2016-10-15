
package com.github.sahasatvik.math;

public class UnmatchedBracketsException	extends ExpressionParserException {
	int pos;
	public UnmatchedBracketsException (String faultyExpression, int pos) {
		super(faultyExpression);
		this.pos = pos;
	}
	public int getPositionOfBracket () {
		return pos;
	}
} 
