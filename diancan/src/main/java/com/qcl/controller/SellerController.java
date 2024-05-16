package com.qcl.controller;

import com.qcl.bean.SellerInfo;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.SellerQualiEnum;
import com.qcl.meiju.SellerStatusEnum;
import com.qcl.repository.SellerRepository;
import com.qcl.request.SellerForm;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/Seller")
@Slf4j
public class SellerController {

    @Autowired
    SellerRepository repository;

    //管理员商家页面相关
    @GetMapping("/list")
    public String list(HttpServletRequest request, ModelMap map) {
        List<SellerInfo> sellerList = repository.findAll();
        map.put("sellerList", sellerList);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConst.COOKIE_TOKEN)) {
                    String cookieValue = cookie.getValue();
                    log.info("获取到存储的cookie={}", cookieValue);
                    if (!StringUtils.isEmpty(cookieValue)) {
                        SellerInfo sellerInfo = repository.findBySellerId(Integer.parseInt(cookieValue));
                        if (sellerInfo != null && Objects.equals(SellerStatusEnum.YUANGONG.getCode(), sellerInfo.getSellerType())) {
                            map.put("isSeller", true);
                        }
                    }
                }
            }
        }
        return "Seller/list";
    }

    //管理员详情页
    @GetMapping("/index")
    public String index(@RequestParam(value = "sellerId", required = false) Integer sellerId,
                        ModelMap map) {
        SellerInfo sellerInfo = repository.findBySellerId(sellerId);
        map.put("sellerInfo", sellerInfo);
        map.put("enums", SellerStatusEnum.values());
        log.error("商家枚举={}", SellerStatusEnum.values());
        map.put("enums1",  SellerQualiEnum.values());
        log.error("商家枚举={}", SellerQualiEnum.values());
        return "Seller/index";
    }

    //保存/更新
    @PostMapping("/save")
    public String save(@Valid SellerForm form,
                       BindingResult bindingResult,
                       ModelMap map) {
        log.info("SellerForm={}", form);
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/Seller/index");
            return "zujian/error";
        }
        SellerInfo seller = new SellerInfo();
        try {
            if (form.getSellerId() != null) {
                seller = repository.findBySellerId(form.getSellerId());
            }
            BeanUtils.copyProperties(form, seller);
            repository.save(seller);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/Seller/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/Seller/list");
        return "zujian/success";
    }
}