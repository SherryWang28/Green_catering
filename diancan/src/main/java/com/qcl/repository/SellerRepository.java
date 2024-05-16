package com.qcl.repository;


import com.qcl.bean.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<SellerInfo, Integer> {
    //    List<AdminInfo> findByPhoneOrUsername(String phone);
//    void findByUsername();
  SellerInfo findByPhoneOrSellername(String phone,  String sellername);

    SellerInfo findBySellerId(Integer sellerId);
}
