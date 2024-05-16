package com.qcl.controller;

import com.qcl.bean.AdminInfo;
import com.qcl.global.GlobalConst;
import com.qcl.global.GlobalData;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.utils.CookieUtil;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.UUID;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
public class LoginAdminController {

    @Autowired
    AdminRepository repository;

    @GetMapping("/login")
    public String login(HttpServletRequest request,
                        HttpServletResponse response)
        throws IOException{
        HttpSession session =request.getSession();
        log.info("sessionId:{}",session.getId());
        Object user = session.getAttribute("User");
        if(user!=null)
        {
            // 如果已经登录，跳转到后台
            response.sendRedirect("Seller/list");
            return null;

        }
    return "zujian/loginView";
    }

    @GetMapping("/loginAdmin")
    @ResponseBody
    public String loginAdmin(@RequestParam("phoneOrname") String phoneOrname,
                             @RequestParam("password") String password,
                             HttpServletRequest request,
                             HttpServletResponse response) {
        //这里得phoneOrname代表 手机号或者用户名
        System.out.println("执行了登陆查询");
        AdminInfo admin = repository.findByPhoneOrUsername(phoneOrname, phoneOrname);
        log.info("查询到得admininfo={}", admin);
        if (admin != null && admin.getPassword().equals(password)) {
            log.info("登录成功的token={}", admin.getAdminId());//用adminid做cookie
           // GlobalData.ISSUPERADMIN_ID = admin.getAdminId();
            //有效期2小时
            /*
            * 将 adminId 存在cookie中是不安全的，这个值可以在浏览器中更改，如果是int类型，可以随意设詈登录用户Id
            //因此用户数据应该存在Session中，当然还可以使用Spring security之类的框架
            //Session会在浏览器中存一个cookie,JSESSIONID=41CDFBQA154319CD27106502A4AD456E
            // 服务端有全局的Session列表，自动通过JSESSIONID值获取当前请求的Session
            *
            * */
          //  CookieUtil.set(response, GlobalConst.COOKIE_TOKEN, "" + admin.getAdminId(), 7200);

            HttpSession session =request.getSession();
            session.setMaxInactiveInterval(24*60*60);
            session.setAttribute("User",admin);
            return "登录成功";
        } else {
            throw new DianCanException(ResultEnum.LOGIN_FAIL);
        }
    }

  @GetMapping("/logoutAdmin")  //删除session中的数据
  public String logout(HttpServletRequest request,
                       HttpServletResponse response,
                       ModelMap map) {
      // 从session中删除用户信息
      HttpSession session = request.getSession(false); // 传入false表示如果session不存在，则不创建新的session
      if (session != null) {
          session.removeAttribute("User"); // 删除键为"User"的属性
          session.invalidate(); // 可选：使session失效，如果后续没有其他操作需要这个session的话
      }

      // 从cookie里查询并清除
      Cookie cookie = CookieUtil.get(request, GlobalConst.COOKIE_TOKEN);
      if (cookie != null) {
          // 清除cookie
          CookieUtil.set(response, GlobalConst.COOKIE_TOKEN, null, 0);
      }

      map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
      map.put("url", "/diancan/adimOrder/list");
      return "zujian/success";
  }

   /*  public String logout(HttpServletRequest request,
                         HttpServletResponse response,
                         ModelMap map) {
        //1. 从cookie里查询
        Cookie cookie = CookieUtil.get(request, GlobalConst.COOKIE_TOKEN);
        if (cookie != null) {
            //2. 清除cookie
            CookieUtil.set(response, GlobalConst.COOKIE_TOKEN, null, 0);
        }
        map.put("msg", ResultEnum.LOGOUT_SUCCESS.getMessage());
        map.put("url", "/diancan/adimOrder/list");
        return "zujian/success";
    }
     */
}