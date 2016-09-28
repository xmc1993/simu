package cn.superid.webapp.forms;

import cn.superid.webapp.model.UserEntity;
import org.springframework.beans.BeanUtils;

/**
 * Created by xmc1993 on 16/9/27.
 */
public class UserDto extends UserEntity{
    private String chatToken;

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    public static UserDto UserEntity2UserDto(UserEntity userEntity){
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);
        return userDto;
    }
}
