package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.UserInfo;
import com.qcl.meiju.ResultEnum;
import com.qcl.yichang.DianCanException;
import com.qcl.request.UserForm;
import com.qcl.repository.UserRepository;
import com.qcl.utils.ApiUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户相关
 */
@RestController
@RequestMapping("/user")
@Slf4j
public class WxUserController {

    @Autowired
    UserRepository repository;

    //创建订单
    @PostMapping("/save")
    public ResultVO create(@Valid UserForm userForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确, userForm={}", userForm);
            throw new DianCanException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        UserInfo user = new UserInfo();
        user.setUsername(userForm.getUsername());
        user.setOpenid(userForm.getOpenid());
        user.setPhone(userForm.getPhone());
        user.setZhuohao(userForm.getZhuohao());
        user.setRenshu(userForm.getRenshu());

        return ApiUtil.success(repository.save(user));
    }

    @GetMapping("/getUserInfo")
    public ResultVO getUserInfo(@RequestParam("openid") String openid) {
        UserInfo user = repository.findByOpenid(openid);
        return ApiUtil.success(user);
    }

}
