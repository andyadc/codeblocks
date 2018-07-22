package com.andyadc.permission.util;

import com.andyadc.permission.dto.Params;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @author andaicheng
 * @since 2018/7/22
 */
public class BeanValidator {

    private static ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();

    public static <T> Map<String, String> validate(T t, Class... groups) {
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<T>> violations = validator.validate(t, groups);
        if (violations.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, String> errors = Maps.newLinkedHashMap();
        for (ConstraintViolation<T> violation : violations) {
            errors.put(violation.getPropertyPath().toString(), violation.getMessage());
        }
        return errors;
    }

    public static Map<String, String> validate(Collection<?> collection) {
        Preconditions.checkNotNull(collection);
        Iterator iterator = collection.iterator();
        Map errors;

        do {
            if (!iterator.hasNext()) {
                return Collections.emptyMap();
            }
            Object object = iterator.next();
            errors = validate(object);
        } while (errors.isEmpty());

        return errors;
    }

    public static Map<String, String> validateObject(Object first, Object... objects) {
        if (objects != null && objects.length > 0) {
            return validate(Lists.asList(first, objects));
        } else {
            return validate(first);
        }
    }

    public static void main(String[] args) {
        Params params = new Params();
        Map<String, String> map = BeanValidator.validate(params);
        System.out.println(map);
    }

//    public static void check(Object param) throws ParamException {
//        Map<String, String> map = BeanValidator.validateObject(param);
//        if (map != null && map.size() > 0) {
//            throw new ParamException(map.toString());
//        }
//    }
}
