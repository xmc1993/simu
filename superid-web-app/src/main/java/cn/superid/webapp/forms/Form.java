package cn.superid.webapp.forms;

/**
 * Created by jessiechen on 16/01/17.
 */
public abstract class Form {

    protected static void inRange(String property, Integer value, int lbound, int ubound) throws IllegalArgumentException {
        notNull(property, value);
        if (value < lbound || value > ubound) {
            String errorMsg = String.format("%s must be in range [%d, %d]", property, lbound, ubound);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    protected static void inLength(String property, String value, int lbound, int ubound) throws IllegalArgumentException {
        notNull(property, value);
        int length = value.length();
        if (length < lbound || length > ubound) {
            String errorMsg = String.format("%s must be in length of [%d, %d]", property, lbound, ubound);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    protected static void notEmpty(String property, String value) throws IllegalArgumentException {
        if (value == null || value.trim().isEmpty()) {
            String errorMsg = String.format("%s must not be empty", property);
            throw new IllegalArgumentException(errorMsg);
        }
    }

    protected static void notNull(String property, Object value) throws IllegalArgumentException {
        String errorMsg = String.format("%s must not be null", property);
        if (value == null)
            throw new IllegalArgumentException(errorMsg);
    }

    protected static boolean notAllNull(Object... objects) {
        for (Object object : objects) {
            if (object != null)
                return true;
        }
        return false;
    }

    public abstract void validate() throws IllegalArgumentException;

}
