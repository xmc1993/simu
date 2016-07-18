package cn.superid.webapp.utils;

/**
 * Created by zoowii on 14/10/16.
 */
public interface IPasswordEncryptor {
    public String encode(String password);

    public boolean matches(String rawPassword, String encodePassword);
}
