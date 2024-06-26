package com.qcl.bean;

import lombok.Data;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.math.BigDecimal;

@Entity
@Data
public class WxOrderDetail {
    @Id
    @GeneratedValue
    private Integer detailId;
    private Integer orderId;//订单id.
    private int foodId;//菜品id
    private String foodName;

   // private String foodTaste;
   // private String foodDateTime;
   // private String foodRemarks;


    private BigDecimal foodPrice;
    private Integer foodQuantity;//下单食物数量
    private String foodIcon;//商品图
}
