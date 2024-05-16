package com.qcl.controller;

import com.qcl.bean.SUserInfo;
import com.qcl.global.GlobalConst;
import com.qcl.meiju.SUserStatusEnum;
import com.qcl.repository.SUserRepository;
import com.qcl.request.SUserForm;
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
@RequestMapping("/SUserInfo")
@Slf4j
public class SUserInfoController {

    @Autowired
    SUserRepository repository;

    //管理员页面相关
    @GetMapping("/list")
    public String list(HttpServletRequest request, ModelMap map) {


        List<SUserInfo> suserList = repository.findAll();
        map.put("suserList", suserList);

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(GlobalConst.COOKIE_TOKEN)) {
                    String openid = cookie.getValue(); // 直接获取cookie的值作为openid
                    log.info("获取到存储的openid cookie={}", openid);

                    // 调用findByOpenid方法查询用户信息
                    SUserInfo suserInfo = repository.findByOpenid(openid);

                    if (suserInfo != null) {
                        map.put("isSUser", true);
                    }
                }
            }
        }

            return "SUserInfo/list";

    }


    //管理员详情页
    @GetMapping("/index")
        public String index(@RequestParam(value = "openid", required = false) String openid,
                ModelMap map) {
            SUserInfo suserInfo = null;
            if (openid != null && !openid.isEmpty()) {
                // 使用openid查询用户信息
                suserInfo = repository.findByOpenid(openid);
            }

            map.put("suserInfo", suserInfo);
            map.put("enums", SUserStatusEnum.values());

            // 使用log.info而不是log.error来记录用户枚举信息，因为这不是一个错误
            log.info("用户枚举={}", SUserStatusEnum.values());

            // log.error("用户枚举={}", SUserStatusEnum.values());
            return "SUserInfo/index";
        }





        //保存/更新
    @PostMapping("/save")

    public String save(@Valid SUserForm form,
                       BindingResult bindingResult,
                       ModelMap map) {
        log.info("SUserForm={}", form);

        if (bindingResult.hasErrors()) {
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url", "/diancan/SUserInfo/index");
            return "zujian/error";
        }

        SUserInfo suser = new SUserInfo();

        try {
            // 使用openid查询用户信息
            if (form.getOpenid() != null && !form.getOpenid().isEmpty()) {
                suser = repository.findByOpenid(form.getOpenid());
                if (suser == null) {
                    // 如果根据openid找不到用户，则创建一个新用户
                }
            }

            // 如果找到了用户或者我们决定创建一个新用户，则将表单数据复制到suser对象
            BeanUtils.copyProperties(form, suser);

            // 保存或更新用户信息
            repository.save(suser);

        } catch (DianCanException e) {
            map.put("msg", e.getMessage());
            map.put("url", "/diancan/SUserInfo/index");
            return "zujian/error";
        }

        map.put("url", "/diancan/SUserInfo/list");
        return "zujian/success";
    }
}








