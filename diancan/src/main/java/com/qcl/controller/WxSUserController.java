package com.qcl.controller;

import com.qcl.api.ResultVO;
import com.qcl.bean.UserInfo;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.UserRepository;
import com.qcl.request.UserForm;
import com.qcl.utils.ApiUtil;
import com.qcl.yichang.DianCanException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;


import com.qcl.api.ResultVO;
import com.qcl.bean.SUserInfo;
import com.qcl.meiju.ResultEnum;
import com.qcl.repository.SUserRepository;
import com.qcl.request.SUserForm;

import javax.validation.Valid;

/**
 * 用户相关
 */
@RestController
@RequestMapping("/SUser")
@Slf4j
public class WxSUserController {

    @Autowired
    SUserRepository repository;

    //创建订单
    @PostMapping("/save")
    public ResultVO create(@Valid SUserForm suserForm,
                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error("参数不正确, SuserForm={}", suserForm);
            throw new DianCanException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        SUserInfo suser = new SUserInfo();
        suser.setOpenid(suserForm.getOpenid());
        suser.setSusername(suserForm.getSusername());
        suser.setPhone(suserForm.getPhone());
       // suser.setSuserType(3);


  return ApiUtil.success(repository.save(suser));
    }



    @GetMapping("/getSUserInfo")
    public ResultVO getSUserInfo(@RequestParam("openid") String openid) {
        SUserInfo suser = repository.findByOpenid(openid);
        return ApiUtil.success(suser);
    }
}
