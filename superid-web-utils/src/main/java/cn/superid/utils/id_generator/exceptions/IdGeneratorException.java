package cn.superid.utils.id_generator.exceptions;

/**
 * Created by zoowii on 2014/8/30.
 */
public class IdGeneratorException extends Exception {
    private String message = null;

    public IdGeneratorException(String message) {
        this.message = message;
    }
}
