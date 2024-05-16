package com.qcl.controller;

import com.qcl.bean.Food;
import com.qcl.bean.WxOrderDetail;
import com.qcl.bean.WxOrderRoot;
import com.qcl.global.GlobalData;
import com.qcl.meiju.OrderStatusEnum;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.OrderDetailRepository;
import com.qcl.repository.OrderRootRepository;
import com.qcl.response.WxCardResponse;
import com.qcl.response.WxOrderResponse;
import com.qcl.utils.EnumUtil;
import com.qcl.utils.ExcelExportUtils;
import com.qcl.websocket.WebSocketServer;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
public class WxOrderUtils {
    @Autowired
    private FoodRepository foodRepository;
    @Autowired
    private WebSocketServer webSocket;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRootRepository orderRootRepository;


    //创建订单的方法
    public WxOrderResponse createOrder(WxOrderResponse orderBean) {
        log.error("小程序提交上来的订单={}", orderBean);
        BigDecimal orderAmount = new BigDecimal(BigInteger.ZERO);//订单总价格

        //计算订单总价
        for (WxOrderDetail orderDetail : orderBean.getOrderDetailList()) {
            Food foodInfo = foodRepository.findById(orderDetail.getFoodId()).orElse(null);
            if (foodInfo == null) {
                throw new DianCanException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            orderAmount = foodInfo.getFoodPrice()
                    .multiply(new BigDecimal(orderDetail.getFoodQuantity()))
                    .add(orderAmount);//计算订单总价
        }
        //写入总订单数据库
        WxOrderRoot orderMaster = new WxOrderRoot();
        BeanUtils.copyProperties(orderBean, orderMaster);
        orderMaster.setOrderAmount(orderAmount);
        orderMaster.setOrderStatus(OrderStatusEnum.NEW_PAYED.getCode());
        WxOrderRoot orderRoot = orderRootRepository.save(orderMaster);

        for (WxOrderDetail orderDetail : orderBean.getOrderDetailList()) {
            Food foodInfo = foodRepository.findById(orderDetail.getFoodId()).orElse(null);
            //订单详情入库
            orderDetail.setOrderId(orderRoot.getOrderId());
            BeanUtils.copyProperties(foodInfo, orderDetail);
            orderDetailRepository.save(orderDetail);
        }

        //扣库存
        List<WxCardResponse> cartDTOList = orderBean.getOrderDetailList().stream().map(e ->
                new WxCardResponse(e.getFoodId(), e.getFoodQuantity())
        ).collect(Collectors.toList());

        for (WxCardResponse cartDTO : cartDTOList) {
            Food food = foodRepository.findById(cartDTO.getProductId()).orElse(null);
            if (food == null) {
                throw new DianCanException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = food.getFoodStock() - cartDTO.getProductQuantity();
            if (result < 0) {
                throw new DianCanException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            food.setFoodStock(result);
            foodRepository.save(food);
        }
        try {
            log.error("下单,,,,orderDTO={}", orderBean);
            webSocket.sendMessage("0", "0");//发送websocket消息
        } catch (IOException e) {
            e.printStackTrace();
        }
        return orderBean;
    }

    //用户催单
    public void cuiDan(String zhuoHao, Integer orderId) {
        try {
            WxOrderRoot orderRoot = orderRootRepository.findById(orderId).orElse(null);
            if (orderRoot != null) {
                int cuidan = orderRoot.getCuidan();
                orderRoot.setCuidan(cuidan + 1);//催单次数加一
                orderRootRepository.save(orderRoot);
            }
           // webSocket.sendMessage("桌号为" + zhuoHao + ";  订单号为" + orderId, "0");//发送websocket消息
            webSocket.sendMessage( "  订单号为" + orderId, "0");//发送websocket消息
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //查询单个订单
    public WxOrderResponse findOne(Integer orderId) {
        WxOrderRoot orderMaster = orderRootRepository.findById(orderId).orElse(null);
        if (orderMaster == null) {
            throw new DianCanException(ResultEnum.ORDER_NOT_EXIST);
        }

        List<WxOrderDetail> orderDetailList = orderDetailRepository.findByOrderId(orderId);
        if (CollectionUtils.isEmpty(orderDetailList)) {
            throw new DianCanException(ResultEnum.ORDERDETAIL_NOT_EXIST);
        }

        WxOrderResponse orderDTO = new WxOrderResponse();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        orderDTO.setOrderDetailList(orderDetailList);
        orderDTO.setOrderStatusStr(orderDTO.getOrderStatusStr(orderDTO.getOrderStatus()));


        return orderDTO;
    }

//    public Page<WxOrderResponse> findList(String buyerOpenid, Pageable pageable) {
//        Page<OrderMaster> orderMasterPage = orderMasterRepository.findByBuyerOpenid(buyerOpenid, pageable);
//
//        List<WxOrderResponse> orderDTOList = OrderMaster2OrderDTOConverter.convert(orderMasterPage.getContent());
//
//        return new PageImpl<WxOrderResponse>(orderDTOList, pageable, orderMasterPage.getTotalElements());
//    }

    //查询不同订单状态列表
    public List<WxOrderResponse> findListStats(String buyerOpenid, Integer orderStatus) {

        List<WxOrderRoot> orderMasters = orderRootRepository.findByBuyerOpenidAndOrderStatus(buyerOpenid, orderStatus,
                Sort.by(Sort.Direction.DESC, "updateTime"));
        return orderResponse(orderMasters);
    }

    //删除订单
    @Transactional
    public boolean remove(Integer orderId) {
        if (orderId == null) {
            throw new DianCanException(ResultEnum.ORDER_NOT_EXIST);
        }
        orderRootRepository.deleteById(orderId);
        List<WxOrderDetail> orderDetails = orderDetailRepository.findByOrderId(orderId);
        for (WxOrderDetail detail : orderDetails) {
            orderDetailRepository.deleteById(detail.getDetailId());
        }
        return true;
    }

    //取消订单
    @Transactional
    public WxOrderResponse cancel(WxOrderResponse orderDTO) {
        WxOrderRoot orderMaster = new WxOrderRoot();
        //判断订单状态
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW_PAYED.getCode())) {
            log.error("【取消订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new DianCanException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.CANCEL.getCode());
        BeanUtils.copyProperties(orderDTO, orderMaster);
        WxOrderRoot updateResult = orderRootRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【取消订单】更新失败, orderMaster={}", orderMaster);
            throw new DianCanException(ResultEnum.ORDER_UPDATE_FAIL);
        }

        //返回库存
        if (CollectionUtils.isEmpty(orderDTO.getOrderDetailList())) {
            log.error("【取消订单】订单中无商品详情, orderDTO={}", orderDTO);
            throw new DianCanException(ResultEnum.ORDER_DETAIL_EMPTY);
        }
        List<WxCardResponse> cartDTOList = orderDTO.getOrderDetailList().stream()
                .map(e -> new WxCardResponse(e.getFoodId(), e.getFoodQuantity()))
                .collect(Collectors.toList());
        for (WxCardResponse cartDTO : cartDTOList) {
            Food food = foodRepository.findById(cartDTO.getProductId()).orElse(null);
            log.error("订单里的菜品id={},查询的菜品={}", cartDTO.getProductId(), food);
            if (food == null) {
                throw new DianCanException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            Integer result = food.getFoodStock() + cartDTO.getProductQuantity();
            food.setFoodStock(result);
            foodRepository.save(food);
        }

        return orderDTO;
    }

    //完结订单
    @Transactional
    public WxOrderResponse finish(WxOrderResponse orderDTO) {
        //提示用户支付
        if (orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new DianCanException(ResultEnum.ORDER_NO_PAY);
        }
        //只有已支付订单才可以完结订单
        if (!orderDTO.getOrderStatus().equals(OrderStatusEnum.NEW_PAYED.getCode())) {
            log.error("【完结订单】订单状态不正确, orderId={}, orderStatus={}", orderDTO.getOrderId(), orderDTO.getOrderStatus());
            throw new DianCanException(ResultEnum.ORDER_STATUS_ERROR);
        }

        //修改订单状态
        orderDTO.setOrderStatus(OrderStatusEnum.FINISHED.getCode());
        WxOrderRoot orderMaster = new WxOrderRoot();
        BeanUtils.copyProperties(orderDTO, orderMaster);
        WxOrderRoot updateResult = orderRootRepository.save(orderMaster);
        if (updateResult == null) {
            log.error("【完结订单】更新失败, orderMaster={}", orderMaster);
            throw new DianCanException(ResultEnum.ORDER_UPDATE_FAIL);
        }
        return orderDTO;
    }

    public Page<WxOrderResponse> findList(Pageable pageable) {
/*

        Page<WxOrderRoot> orderMasterPage = orderRootRepository.findAll(pageable);

        List<WxOrderResponse> orderDTOList = orderResponse(orderMasterPage.getContent());

        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
*/

        int i= GlobalData.ADMIN_ID;
        Page<WxOrderRoot> orderMasterPage = orderRootRepository.findAllByAdminId(i, pageable);

        // 将查询结果转换为 WxOrderResponse 列表
        List<WxOrderResponse> orderDTOList = orderResponse(orderMasterPage.getContent());

        // 返回新的 Page 对象，包含转换后的订单 DTO 和原始分页信息
        return new PageImpl<>(orderDTOList, pageable, orderMasterPage.getTotalElements());
    }




    /*
     * 类型转换的工具类
     * */
    private WxOrderResponse convert1(WxOrderRoot orderMaster) {
        WxOrderResponse orderDTO = new WxOrderResponse();
        BeanUtils.copyProperties(orderMaster, orderDTO);
        return orderDTO;
    }

    private List<WxOrderResponse> orderResponse(List<WxOrderRoot> orderMasterList) {
        return orderMasterList.stream().map(e ->
                convert1(e)
        ).collect(Collectors.toList());
    }

    //导出订单数据到excel
    public void exportOrderToExcel(HttpServletResponse response) throws IOException {
        String fileName = "订单导出";
        String[] titles = {"订单id", "用户姓名", "用户手机号", "口味", "预定出餐时间","备注","订单金额", "订单状态", "下单时间"};
        List<WxOrderRoot> rootList = orderRootRepository.findAll();
        int size = rootList.size();
        String[][] dataList = new String[size][titles.length];
        for (int i = 0; i < size; i++) {
            WxOrderRoot orderRoot = rootList.get(i);
            dataList[i][0] = "" + orderRoot.getOrderId();
            dataList[i][1] = orderRoot.getBuyerName();
            dataList[i][2] = orderRoot.getBuyerPhone();
           // dataList[i][3] = orderRoot.getBuyerAddress();

            dataList[i][3] = orderRoot.getBuyerTaste();
            dataList[i][4] = orderRoot.getBuyerDateTime();
            dataList[i][5] = orderRoot.getBuyerRemarks();

            dataList[i][6] = "" + orderRoot.getOrderAmount();
            dataList[i][7] = EnumUtil.getByCode(orderRoot.getOrderStatus(), OrderStatusEnum.class).getMessage();
            dataList[i][8] = "" + orderRoot.getCreateTime();




        }
        ExcelExportUtils.createWorkbook(fileName, titles, dataList, response);

    }
}
