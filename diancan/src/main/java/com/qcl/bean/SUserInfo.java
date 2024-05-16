package com.qcl.bean;


import com.qcl.meiju.SUserStatusEnum;
import com.qcl.utils.EnumUtil;
import lombok.Data;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class SUserInfo {
    @Id
    @GeneratedValue
    private String openid;

   // private Integer suserId;
    private String susername;
    private String phone;
   // private Integer suserType;//1员工，2管理员，3用户
    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;



    //    @Transient//忽略映射
   /* public SUserStatusEnum getSUserStatusEnum() {
      return EnumUtil.getByCode(this.suserType,  SUserStatusEnum.class);
     }*/
}
