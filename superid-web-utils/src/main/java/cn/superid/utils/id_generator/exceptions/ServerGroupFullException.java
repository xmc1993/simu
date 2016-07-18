package cn.superid.utils.id_generator.exceptions;

/**
 * 服务器组满载异常
 * Created by zoowii on 2014/8/30.
 */
public class ServerGroupFullException extends IdGeneratorException {
    public ServerGroupFullException(String message) {
        super(message);
    }
}
