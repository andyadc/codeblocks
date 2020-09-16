package com.andyadc.codeblocks.test.lambda;

import java.util.ArrayList;
import java.util.List;

public class AppleSelect {

	public static void main(String[] args) {
		filterApples(new ArrayList<>(), (apple -> Color.RED.equals(apple.color)));
		filter(new ArrayList<Apple>(), apple -> Color.RED.equals(apple.color));
		filter(new ArrayList<Integer>(), i -> i % 2 == 0);

	}

	public static <T> List<T> filter(List<T> list, Predicate<T> predicate) {
		List<T> result = new ArrayList<>();
		for (T t : list) {
			if (predicate.test(t)) {
				result.add(t);
			}
		}
		return result;
	}

	public static List<Apple> filterApples(List<Apple> apples, ApplePredicate predicate) {
		List<Apple> result = new ArrayList<>();
		for (Apple apple : apples) {
			if (predicate.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}

	enum Color {RED, GREEN}

	@FunctionalInterface
	interface ApplePredicate {
		boolean test(Apple apple);
	}

	@FunctionalInterface
	interface Predicate<T> {
		boolean test(T t);
	}

	class AppleGreenColorPredicate implements ApplePredicate {

		@Override
		public boolean test(Apple apple) {
			return Color.GREEN.equals(apple.color);
		}
	}

	class AppleHeavyWeightPredicate implements ApplePredicate {

		@Override
		public boolean test(Apple apple) {
			return apple.getWeight() >= 100F;
		}
	}

	class Apple {
		private Color color;
		private float weight;

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public float getWeight() {
			return weight;
		}

		public void setWeight(float weight) {
			this.weight = weight;
		}
	}
}
