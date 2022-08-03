package com.createryan.usercenter.model.dto;

import lombok.Data;

import java.util.Date;

/**
 * 用户信息数据传输对象
 *
 * @author: createryan
 * @date 2022/8/4 1:31
 */
@Data
public class UserDTO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String username;

    /**
     * 用户账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String avatarUrl;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 用户状态.0 - 正常
     */
    private Integer userStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户角色.0-普通用户，1-管理员用户
     */
    private Integer userRole;
}
