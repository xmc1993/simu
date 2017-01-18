package cn.superid.webapp.notice.chat.exception;

/**
 * Created by jessiechen on 11/01/17.
 */
public class NotLoginException extends RuntimeException {
    public NotLoginException() {
        super("login first");
    }
}
