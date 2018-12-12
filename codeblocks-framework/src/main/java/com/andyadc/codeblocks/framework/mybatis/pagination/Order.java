package com.andyadc.codeblocks.framework.mybatis.pagination;

import com.andyadc.codeblocks.kit.text.StringUtil;

import java.io.Serializable;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * @author andy.an
 * @since 2018/4/18
 */
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final String INJECTION_REGEX = "[A-Za-z0-9\\_\\-\\+\\.]+";
    private Direction direction;
    private String property;

    public Order(Direction direction, String property) {
        this.direction = direction;
        this.property = property;
    }

    public static boolean isSQLInjection(String str) {
        return !Pattern.matches(INJECTION_REGEX, str);
    }

    public static Order create(String property, String direction) {
        return new Order(Direction.fromString(direction), property);
    }

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    @Override
    public String toString() {
        if (StringUtil.isBlank(property)) {
            throw new IllegalArgumentException("property is null");
        }
        if (isSQLInjection(property)) {
            throw new IllegalArgumentException("SQLInjection property: " + property);
        }
        return property + (direction == null ? "" : " " + direction.name());
    }

    public enum Direction {
        ASC, DESC;

        public static Direction fromString(String value) {
            try {
                return Direction.valueOf(value.toUpperCase(Locale.US));
            } catch (Exception e) {
                return ASC;
            }
        }
    }
}
