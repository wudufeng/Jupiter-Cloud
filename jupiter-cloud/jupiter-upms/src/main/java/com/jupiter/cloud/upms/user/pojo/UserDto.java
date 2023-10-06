package com.jupiter.cloud.upms.user.pojo;

import com.jupiter.cloud.upms.user.entity.UserInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * @author jupiter
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserDto extends UserInfo {
    private static final long serialVersionUID = 6449474008748742204L;

    private String loginName;
    private String password;
}
