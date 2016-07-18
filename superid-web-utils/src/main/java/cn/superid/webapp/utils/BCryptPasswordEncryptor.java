package cn.superid.webapp.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Created by zoowii on 14/10/13.
 */
public class BCryptPasswordEncryptor implements IPasswordEncryptor {
    @Override
    public String encode(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean matches(String rawPassword, String encodePassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(rawPassword, encodePassword);
    }
}
