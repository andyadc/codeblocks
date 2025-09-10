package com.andyadc.codeblocks.kit.validator;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.HibernateValidator;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanValidator {

	private static final ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

	// 自定义ValidatorFactory
	private static final ValidatorFactory customizeFactory = Validation.byProvider(HibernateValidator.class)
		.configure()
		.failFast(true)
		.buildValidatorFactory();

	private BeanValidator() {
	}

	public static Map<String, String> validateObject(Object first, Object... objects) {
		if (objects != null && objects.length > 0) {
			return validate(List.of(first, objects));
		} else {
			return validate(first);
		}
	}

	public static void check(Object param) {
		Map<String, String> map = validateObject(param);
		if (!map.isEmpty()) {
			throw new IllegalArgumentException(map.toString());
		}
	}

	private static <T> Map<String, String> validate(T t, Class<?>... groups) {
		Validator validator = validatorFactory.getValidator();
		Set<ConstraintViolation<T>> violations = validator.validate(t, groups);

		Map<String, String> errors = new LinkedHashMap<>();
		for (ConstraintViolation<T> violation : violations) {
			errors.put(violation.getPropertyPath().toString(), violation.getMessage());
		}
		return errors;
	}

	private static Map<String, String> validate(Collection<?> collection) {
		Iterator<?> iterator = collection.iterator();
		Map<String, String> errors = new LinkedHashMap<>();
		while (iterator.hasNext()) {
			Object object = iterator.next();
			errors.putAll(validate(object));
		}
		return errors;
	}
}
