package com.qcl.repository;

import com.qcl.bean.Food;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Integer> {

    List<Food> findByFoodStockLessThan(int num); // 查询库存少于num的菜品

    //!!!!!!!
    List<Food> findByFoodStockLessThanAndAdminId(int num, int adminId);

    List<Food> findByFoodStatusAndFoodNameContaining(Integer foodStatus, String name);

    List<Food> findByFoodStatus(Integer foodStatus);

    // 添加分页功能的findByAdminId方法  
    Page<Food> findByAdminId(Integer adminId, Pageable pageable);

    // 如果你还需要不带分页的findByAdminId方法，可以保留它  
    List<Food> findByAdminId(Integer adminId);

    List<Food> findByFoodStatusAndAdminIdAndFoodNameContaining(Integer code, Integer adminId, String searchKey);

    List<Food> findByFoodStatusAndAdminId(Integer code, Integer adminId);

    // ... 其他的方法定义  
}