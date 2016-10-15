
package com.github.sahasatvik.math;

public class MathParser {
	protected static boolean isNumber (String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	protected static double factorial (double x) {
		if (x == 0)
			return 1;
		double n = 1;
		while (x > 0)
			n *= x--;
		return n;
	}
	protected static double solveBinaryOperation (double a, String op, double b) {
		double result = 0.0;
		if (op.equals("^")) {
			result = Math.pow(a, b);
		} else if (op.equals("%")) {
			result = a % b;
		} else if (op.equals("/")) {
			result = a / b;
		} else if (op.equals("*")) {
			result = a * b;
		} else if (op.equals("+")) {
			result = a + b;
		} else if (op.equals("-")) {
			result = a - b;
		}
		return result;
	}
	protected static double solveUnaryFunction (String func, double x) throws FunctionNotFoundException {
		double result = 0.0;
		if (func.equals("sin")) {
			result = Math.sin(x);
		} else if (func.equals("cos")) {
			result = Math.cos(x);
		} else if (func.equals("tan")) {
			result = Math.tan(x);
		} else if (func.equals("csc")) {
			result = 1.0/Math.sin(x);
		} else if (func.equals("sec")) {
			result = 1.0/Math.cos(x);
		} else if (func.equals("ctn")) {
			result = 1.0/Math.tan(x);
		} else if (func.equals("rad")) {
			result = Math.toRadians(x);
		} else if (func.equals("deg")) {
			result = Math.toDegrees(x);
		} else if (func.equals("fct")) {
			result = factorial(x);
		} else if (func.equals("abs")) {
			result = Math.abs(x);
		} else if (func.equals("exp")) {
			result = Math.exp(x);
		} else if (func.equals("log")) {
			result = Math.log(x);
		} else {
			throw new FunctionNotFoundException(func + "[]");
		}
		return result;
	}
} 
