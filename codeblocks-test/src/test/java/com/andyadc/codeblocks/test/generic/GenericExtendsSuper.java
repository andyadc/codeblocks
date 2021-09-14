package com.andyadc.codeblocks.test.generic;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class GenericExtendsSuper {

	@Test
	public void test01() {
		List<? extends Fruit> fruits = new ArrayList<>();
//		fruits.add(new Food());		// compile error
//		fruits.add(new Fruit());	// compile error
//		fruits.add(new Apple());	// compile error

//		fruits = new ArrayList<Food>();		// compile error
		fruits = new ArrayList<Fruit>();
		fruits = new ArrayList<Apple>();

//		fruits = new ArrayList<? extends Fruit>();		// compile error: 通配符类型无法实例化

		Fruit object = fruits.get(0);    // compile success
	}

	@Test
	public void test02() {
		List<? super Fruit> fruits = new ArrayList<>();
//		fruits.add(new Food());		// compile error
		fruits.add(new Fruit());    // compile success
		fruits.add(new Apple());    // compile success

		fruits = new ArrayList<Food>();
		fruits = new ArrayList<Fruit>();
//		fruits = new ArrayList<Apple>();	// compile error

	}
}

class Food {
}

class Fruit extends Food {
}

class Apple extends Fruit {
}
