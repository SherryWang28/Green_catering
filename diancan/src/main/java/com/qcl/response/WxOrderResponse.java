package com.qcl.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.qcl.bean.WxOrderDetail;
import com.qcl.meiju.OrderStatusEnum;
import com.qcl.utils.EnumUtil;
import com.qcl.utils.serializer.Date2StringSerializer;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
//@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxOrderResponse {
    private Integer orderId;//订单id
    private String buyerName;//买家名字
    private String buyerPhone;//买家手机号
    private String buyerAddress;//买家桌号

    private String buyerGreen;//买家是否预定
    private String buyerTaste;//买家口味
    private String buyerDateTime;//买家预定时间
    private String buyerRemarks;//买家备注时间
    private  Integer adminId;//买家备注时间

    private String buyerOpenid;//买家微信Openid
    private BigDecimal orderAmount;//订单总金额
    private Integer cuidan = 0;//被催单次数
    /**
     * 订单状态, 默认为0新下单.
     * NEW(0, "新订单，未支付"),
     * NEW_PAYED(0, "新订单，已支付"),
     * FINISHED(1, "完结"),
     * CANCEL(2, "已取消"),
     */
    private Integer orderStatus;//0"未支付",1, "已支付",2, "已取消"),3, "已完结",4, "已评价"

    private String orderStatusStr;

    @JsonIgnore
    public String getOrderStatusStr(Integer orderStatus2) {
        return EnumUtil.getByCode(orderStatus2, OrderStatusEnum.class).getMessage();
    }

    //创建时间.
    //    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date createTime;

    //更新时间.
    //    @JsonSerialize(using = Date2LongSerializer.class)
    @JsonSerialize(using = Date2StringSerializer.class)//用于把date类型转换为string类型
    private Date updateTime;

    List<WxOrderDetail> orderDetailList;

    @JsonIgnore
    public OrderStatusEnum getOrderStatusEnum() {
        return EnumUtil.getByCode(orderStatus, OrderStatusEnum.class);
    }
}
