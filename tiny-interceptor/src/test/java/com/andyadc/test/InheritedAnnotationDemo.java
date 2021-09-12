package com.andyadc.test;

/**
 *
 */
public class InheritedAnnotationDemo {

	public static void main(String[] args) {
		Logging logging = A.class.getAnnotation(Logging.class);
		System.out.println(logging);

		logging = B.class.getAnnotation(Logging.class);
		System.out.println(logging);
	}
}

@Logging(name = "A")
class A { // Super Class

}

class B extends A { // B is A

}
