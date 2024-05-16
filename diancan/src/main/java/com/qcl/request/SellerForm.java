package com.qcl.request;

import lombok.Data;

@Data
public class SellerForm {

    private String username;
    private String password;
    private String phone;
    private Integer sellerId;
    private Integer sellerType;
    private Integer quali;
}
