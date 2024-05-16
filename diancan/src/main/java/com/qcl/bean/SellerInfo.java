package com.qcl.bean;


import com.qcl.meiju.SellerStatusEnum;
import com.qcl.utils.EnumUtil;
import lombok.Data;
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
public class SellerInfo {
    @Id
    @GeneratedValue
    private Integer sellerId;
    private String phone;
    private String sellername;
    private String password;
    private Integer sellerType;//1员工，2管理员，3用户
    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;
    private Integer quali;//1合格，0不合格

    //    @Transient//忽略映射
    public SellerStatusEnum getSellerStatusEnum() {
        return EnumUtil.getByCode(this.sellerType,  SellerStatusEnum.class);
    }
}
