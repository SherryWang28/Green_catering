package com.qcl.controller;

import com.qcl.bean.SellerInfo;
import com.qcl.global.GlobalConst;
import com.qcl.global.GlobalData;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.SellerRepository;
import com.qcl.utils.CookieUtil;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class LoginSellerController {

    @Autowired
    SellerRepository repository;

    @GetMapping("/loginSeller")
    @ResponseBody
    public String loginSeller(@RequestParam("phoneOrname") String phoneOrname,
                             @RequestParam("password") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        //这里得phoneOrname代表 手机号或者用户名
        System.out.println("执行了登陆查询");
        SellerInfo seller = repository.findByPhoneOrSellername(phoneOrname, phoneOrname);
        log.info("查询到得Sellerinfo={}", seller);
        if (seller != null && seller.getPassword().equals(password)) {
            log.info("登录成功的token={}", seller.getSellerId());//用sellerid做cookie
            GlobalData.ADMIN_ID = seller.getSellerId();
            //有效期2小时
           // CookieUtil.set(response, GlobalConst.COOKIE_TOKEN, "" + seller.getSellerId(), 7200);


            HttpSession session =request.getSession();
            session.setMaxInactiveInterval(24*60*60);
            session.setAttribute("User",seller);

            return "登录成功";
        } else {
            throw new DianCanException(ResultEnum.LOGIN_FAIL);
        }
    }

    @GetMapping("/logoutSeller")
    public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         ModelMap map) {

        // 从session中删除用户信息
        HttpSession session = request.getSession(false); // 传入false表示如果session不存在，则不创建新的session
        if (session != null) {
            session.removeAttribute("User"); // 删除键为"User"的属性
            session.invalidate(); // 可选：使session失效，如果后续没有其他操作需要这个session的话
        }

        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, GlobalConst.COOKIE_TOKEN);
        if (cookie != null) {
            //2. 清除cookie
            CookieUtil.set(response, GlobalConst.COOKIE_TOKEN, null, 0);
        }
        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/diancan/Seller/list");
        return "zujian/success";
    }
}