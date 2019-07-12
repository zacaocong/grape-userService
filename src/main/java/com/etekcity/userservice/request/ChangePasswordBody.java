package com.etekcity.userservice.request;

import javax.validation.constraints.NotEmpty;

/**
 * 修改密码的请求消息体
 *
 * @author grape
 */
public class ChangePasswordBody {
    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
