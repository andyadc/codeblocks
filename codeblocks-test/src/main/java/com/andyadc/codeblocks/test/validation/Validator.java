package com.andyadc.codeblocks.test.validation;

@FunctionalInterface
public interface Validator<T> {

	boolean validate(T value);
}
