package com.andyadc.codeblocks.kit;

import javax.xml.bind.ValidationException;

/**
 * Enum of Validator implementations.
 *
 * @author andy.an
 * @since 2017/11/2
 */
public enum Validator {

    /**
     * This validator checks that the string represents an integer.
     */
    INTEGER_NUMBER {
        @Override
        void validate(String str) throws ValidationException {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                throw new ValidationException("Error while validating "
                        + str);
            }
        }
    },

    /**
     * This validator checks that the string represents an long.
     */
    LONG_NUMBER {
        @Override
        void validate(String str) throws ValidationException {
            try {
                Long.parseLong(str);
            } catch (NumberFormatException e) {
                throw new ValidationException("Error while validating "
                        + str);
            }
        }
    },

    /**
     * This validator checks that the string represents a positive number.
     */
    POSITIVE_NUMBER {
        @Override
        void validate(String str) throws ValidationException {
            try {
                if (Double.compare(0.0, Double.parseDouble(str)) > 0) {
                    throw new Exception();
                }
            } catch (Exception e) {
                throw new ValidationException("Error while validating "
                        + str);
            }
        }
    };

    /**
     * Checks that the supplier is valid.
     *
     * @param string - a string supplier
     * @throws ValidationException if validation check fails
     */
    abstract void validate(String string) throws ValidationException;
}
