package com.qcl.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import com.qcl.meiju.FoodStatusEnum;
import lombok.Data;

@Data
public class FoodRes {
    @JsonProperty("id")
    private Integer foodId;

    @JsonProperty("name")
    private String foodName;

    @JsonProperty("price")
    private BigDecimal foodPrice;
    @JsonProperty("stock")
    private Integer foodStock;//库存

    @JsonProperty("desc")
    private String foodDesc;

    @JsonProperty("icon")
    private String foodIcon;
}
