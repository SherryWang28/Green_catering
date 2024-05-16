package com.qcl.request;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.joda.time.DateTime;

import java.sql.Date;

@Data
public class SUserForm {

    private String openid;
   // private Integer suserId;
    private String susername;

    private String phone;
  //  private Integer suserType = 3;
    private Date createtime;
    private Date updatetime;

    /* */
    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
