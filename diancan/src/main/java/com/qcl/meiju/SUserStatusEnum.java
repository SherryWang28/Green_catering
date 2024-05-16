package com.qcl.meiju;

import lombok.Getter;

@Getter
public enum SUserStatusEnum implements CodeNumEnum {
    //2：管理员可以管理所有，1：商家只可以管理菜品和订单//3.用户
    USER(3, "顾客"),
    SUPER_ADMIN(2, "管理员"),
    YUANGONG(1, "商家");


    private Integer code;
    private String message;

    SUserStatusEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
