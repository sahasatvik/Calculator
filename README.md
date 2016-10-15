#Calculator
This is a simple java application written to evaluate mathematical expressions.
			
'Calculator' can evaluate simple arithmetic expressions, using the operators (+, -, \*, /, ^(power)), as well as 
parenthesis ('(', ')').	'Calculator' follows the BODMAS rule.

Following are some valid expressions : 
	1 + 1			=>		 2.0
	1 * (2 + 3)		=>		 5.0
	10 * (64 ^ -0.5)	=>		1.25

##Sample Runtime
```
	?> 1 + 1
	=> 2.0
	?> 2 ^ (4 - 1)
	=> 8.0
	?> 2e3
	=> 2000.0
	?> x = 3!
	=> 6.0
	?> <x>^2 - <x> - 1
	=> 29.0
	?> y = 30
	=> 30.0
	?> sin[<y>]^2 + cos[<y>]^2
	=> 1.0
	?> exit
```

##Variables
'Calculator' can also store user-defined variables. The syntax for assigning and using variables is as follows : 
	var = value		>	assign 'value' to 'var'
	<var>			>	<var> will be replaced
					its value.
			
Following are some valid uses of variables : 
	x = 3			=>		 3.0
	y = <x> + 1		=>		 4.0
	(<x>^2 + <y>^2)^0.5	=>		 5.0 

Nesting of assignments is also supported, as follows : 
	x = 1 + (y = 1)		=>		 2.0
	<x>			=>		 2.0
	<y>			=>		 1.0

A special variable <ans> stores the previous expression. Thus, the following is valid : 
	1 * 2 * 3 * 4		=>		24.0
	<ans> * 5		=>	       120.0
			
##Functions
'Calculator' supports the use of some basic functions. They can be used with the following syntax : 
	fnc[ value ]		>	evaluate 'fnc' of 'value'

Following are some valid uses of functions : 
	sin[<pi> / 2]		=>		 1.0
	1 + abs[2 - 3]		=>		 2.0
	log[<e> ^ 3]		=>		 3.0

A complete list of functions :
Function	|	Value returned
----------------|-----------------------------------
	abs[ x ]	|	      absolute value of \<x\>
	exp[ x ]	|	      exponent of \<x\> (\<e\> ^ \<x\>)
	log[ x ]	|	      logarithm of \<x\> (base \<e\>)
	fct[ x ] or x!	|	      factorial of \<x\>
	deg[ x ]	|	      convert \<x\> to degrees from radians
	rad[ x ]	|	      convert \<x\> to radians from degrees
	sin[ x ], cos[ x ], tan[ x ], csc[ x ], sec[ x ], ctn[ x ]	|	trigonometric functions  ( \<x\> in radians )
		             

##Commands
'Calculator' interprets expressions starting with '/' as 'commands'. These are special expressions which are not parsed 
as mathematical expressions, but as instructions to the 'Calculator'.

A complete list of commands :
Command		|	Purpose
----------------|---------------------------
	/help				|	general help
	/help vars			|	help on Variables
	/help funcs			|	help on Functions
	/help cmds			|	help on Commands
	/list vars			|	list variables
	/list funcs			|	list functions
	/list cmds  or  /list		|	list commands

