package com.qcl.bean;

import com.qcl.meiju.AdminStatusEnum;
import com.qcl.meiju.OrderStatusEnum;
import com.qcl.utils.EnumUtil;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Entity
@Data
@EntityListeners(AuditingEntityListener.class)
public class AdminInfo {
    @Id
    @GeneratedValue
    private Integer adminId;
    private String username;
    private String password;
    private String phone;
    private Integer adminType;//1员工，2管理员，3用户
    @CreatedDate//自动添加创建时间的注解
    private Date createTime;
    @LastModifiedDate//自动添加更新时间的注解
    private Date updateTime;

    //    @Transient//忽略映射
    public AdminStatusEnum getAdminStatusEnum() {
        return EnumUtil.getByCode(this.adminType, AdminStatusEnum.class);
    }


}
