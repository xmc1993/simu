package cn.superid.utils.id_generator.exceptions;

/**
 * 服务器组没有机器异常
 * Created by zoowii on 2014/8/30.
 */
public class ServerGroupEmptyException extends IdGeneratorException {
    public ServerGroupEmptyException(String message) {
        super(message);
    }
}
