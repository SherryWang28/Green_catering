package com.qcl.repository;

import com.qcl.bean.SUserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SUserRepository extends JpaRepository<SUserInfo, Integer> {
    //    List<AdminInfo> findByPhoneOrUsername(String phone);
//    void findByUsername();
   // SUserInfo findByPhoneOrUsername(String phone, String susername);

    //SUserInfo findBysuserId(Integer suserId);
    SUserInfo findByOpenid(String openid);

}
