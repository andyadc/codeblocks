package com.andyadc.codeblocks.test.enumtest;

/**
 * andy.an
 * 2020/3/24
 */
public enum Calculator {

	ADDITION {
		public Double execute(Double x, Double y) {
			return x + y;
		}
	},

	SUBTRACTION {
		public Double execute(Double x, Double y) {
			return x - y;
		}
	},

	MULTIPLICATION {
		public Double execute(Double x, Double y) {
			return x * y;
		}
	},


	DIVISION {
		public Double execute(Double x, Double y) {
			return x / y;
		}
	},
	;

	public abstract Double execute(Double x, Double y);
}
