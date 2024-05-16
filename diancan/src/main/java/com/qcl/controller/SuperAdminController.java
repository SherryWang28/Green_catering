package com.qcl.controller;

import com.qcl.bean.AdminInfo;
import com.qcl.global.GlobalConst;
import com.qcl.global.GlobalData;
import com.qcl.meiju.AdminStatusEnum;
import com.qcl.repository.AdminRepository;
import com.qcl.request.AdminForm;
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
@RequestMapping("/Superadmin")
@Slf4j
public class SuperAdminController {

    @Autowired
    AdminRepository repository;
/* */

    //管理员页面相关
    @GetMapping("/list")
    public String list(HttpServletRequest request, ModelMap map) {
        List<AdminInfo> adminList = repository.findAll();
        map.put("adminList", adminList);
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConst.COOKIE_TOKEN)) {
                    String cookieValue = cookie.getValue();
                    log.info("获取到存储的cookie={}", cookieValue);
                    if (!StringUtils.isEmpty(cookieValue)) {
                        AdminInfo adminInfo = repository.findByAdminId(Integer.parseInt(cookieValue));
                        // if (adminInfo != null &&  GlobalData.ISSUPERADMIN_ID == 1) {
                            if (adminInfo != null &&  Objects.equals(AdminStatusEnum.SUPER_ADMIN1.getCode(), adminInfo.getAdminType())) {
                            map.put("isAdmin", true);
                        }
                    }
                }
            }
        }
        return "Superadmin/list";
    }




    //管理员详情页
    @GetMapping("/index")
    public String index(@RequestParam(value = "adminId", required = false) Integer adminId,
                        ModelMap map) {
        AdminInfo adminInfo = repository.findByAdminId(adminId);
        map.put("adminInfo", adminInfo);
        map.put("enums", AdminStatusEnum.values());
        log.error("管理员枚举={}", AdminStatusEnum.values());
        return "Superadmin/index";
    }

    //保存/更新
    @PostMapping("/save")
    public String save(@Valid AdminForm form,
                       BindingResult bindingResult,
                       ModelMap map) {
        log.info("SellerForm={}", form);
        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/Superadmin/index");
            return "zujian/error";
        }
        AdminInfo admin = new AdminInfo();
        try {
            if (form.getAdminId() != null) {
                admin = repository.findByAdminId(form.getAdminId());
            }
            BeanUtils.copyProperties(form, admin);
            repository.save(admin);
        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/Superadmin/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/Superadmin/list");
        return "zujian/success";
    }
}