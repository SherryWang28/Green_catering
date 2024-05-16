package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.Food;
import com.qcl.bean.SellerInfo;
import com.qcl.bean.TotalMoney;
import com.qcl.bean.WxOrderRoot;
import com.qcl.global.GlobalData;
import com.qcl.meiju.OrderStatusEnum;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.OrderRootRepository;
import com.qcl.repository.SellerRepository;
import com.qcl.utils.ApiUtil;
import com.qcl.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Optional;
import org.springframework.ui.ModelMap;


@Controller
@RequestMapping("/home")
@Slf4j
public class AdminHomeController {

    @Autowired
    OrderRootRepository orderRootRepository;
    @Autowired
    FoodRepository foodRepository;


    @Autowired
    private SellerRepository sellerRepository;







    /*
     * 页面相关
     * 1,查询当月收入
     * 2，库存预警
     * 3，账单查询
     * */
    @GetMapping("/homeList")
    public String homeList(ModelMap map) {
        int year = TimeUtils.getCurrentYear();
        int month = TimeUtils.getCurrentMonth();
        List<TotalMoney> totalMoneyList = new ArrayList<>();
        for (int i = 1; i <= month; i++) {
            TotalMoney totalMoney = new TotalMoney();
            totalMoney.setTime(year + "年" + i + "月");
            totalMoney.setTotalMoney(getMonthMoney(year, i));
            totalMoneyList.add(totalMoney);
        }
        map.put("totalMoneyList", totalMoneyList);
        map.put("yearMoney", getYearMoney(year));
        map.put("foodList", getFoodKuCunList());


        // 假设你知道要查询的sellerId，这里用一个示例值代替
        int targetSellerId = GlobalData.ADMIN_ID; // 你可以从请求参数、会话或其他地方获取这个值

        // 使用SellerRepository查询SellerInfo实体
        Optional<SellerInfo> sellerInfoOptional = sellerRepository.findById(targetSellerId);

        if (sellerInfoOptional.isPresent()) {
            SellerInfo sellerInfo = sellerInfoOptional.get();
            int sellerId = sellerInfo.getSellerId();

            // 构建欢迎消息字符串
            String welcomeMessage = "商家ID: "+ sellerId +"，欢迎登陆！";

            // 将欢迎消息和其他数据传递给模板
            map.put("welcomeMessage", welcomeMessage);
            // ... 添加其他需要传递给模板的数据，如totalMoneyList等
        } else {
            // 处理未找到SellerInfo的情况，例如显示错误消息或重定向到登录页面
        }

        return "home/list"; // 返回模板名称





    }











    //获取今年年收入
    private BigDecimal getYearMoney(int year) {
        Specification<WxOrderRoot> spec1 = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            try {
                Integer[] statusArr = {
                        OrderStatusEnum.NEW_PAYED.getCode(),
                        OrderStatusEnum.FINISHED.getCode(),
                        OrderStatusEnum.COMMENT.getCode()
                };
                list.add(root.get("orderStatus").in(statusArr));

                // 增加新的条件：只计算adminID=3的订单
                int i= GlobalData.ADMIN_ID;
                list.add(cb.equal(root.get("adminId"), i));



                //大于或等于传入时间
                list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getFisrtDayOfMonth(year, 1)));
                //小于或等于传入时间
                list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getLastDayOfMonth(year, 12)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        List<WxOrderRoot> orderList = orderRootRepository.findAll(spec1);
        BigDecimal totalMoney = new BigDecimal(0);
        for (WxOrderRoot root : orderList) {
            totalMoney = totalMoney.add(root.getOrderAmount());
        }
        return totalMoney;
    }

    //获取每个月份的收入
    private BigDecimal getMonthMoney(int year, int month) {
        //查询当月订单
        Specification<WxOrderRoot> spec1 = (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            try {
                Integer[] statusArr = {
                        OrderStatusEnum.NEW_PAYED.getCode(),
                        OrderStatusEnum.FINISHED.getCode(),
                        OrderStatusEnum.COMMENT.getCode()
                };
                list.add(root.get("orderStatus").in(statusArr));

                //!!!!!!!!!
                int i= GlobalData.ADMIN_ID;
                list.add(cb.equal(root.get("adminId"), i));


                //大于或等于传入时间
                list.add(cb.greaterThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getFisrtDayOfMonth(year, month)));
                //小于或等于传入时间
                list.add(cb.lessThanOrEqualTo(root.get("updateTime").as(Date.class), TimeUtils.getLastDayOfMonth(year, month)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Predicate[] p = new Predicate[list.size()];
            return cb.and(list.toArray(p));
        };
        List<WxOrderRoot> orderList = orderRootRepository.findAll(spec1);
        BigDecimal totalMoney = new BigDecimal(0);
        for (WxOrderRoot root : orderList) {
            totalMoney = totalMoney.add(root.getOrderAmount());
        }
//        log.error("查询到指定月份的订单列表={}", orderList);
        return totalMoney;
    }

    //查询库存少于5的菜品，提醒管理员及时补充库存
    private List<Food> getFoodKuCunList() {


       // return foodRepository.findByFoodStockLessThan(5);

        int num = 10; // 库存少于这个数的菜品
        int i = GlobalData.ADMIN_ID; // 只显示这个adminId的菜品
        return foodRepository.findByFoodStockLessThanAndAdminId(num, i);
    }
}