package com.qcl.meiju;

import lombok.Getter;

@Getter
public enum SellerQualiEnum implements CodeNumEnum {
    //0不合格，1合格
    BUHEGE(1, "合格"),
    HEGE(0, "不合格");


    private Integer code;
    private String message;

    SellerQualiEnum(Integer code, String message) {
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
