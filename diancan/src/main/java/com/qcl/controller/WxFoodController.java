package com.qcl.controller;

import com.qcl.api.FoodRes;
import com.qcl.api.LeimuVO;
import com.qcl.api.ResultVO;
import com.qcl.bean.Leimu;
import com.qcl.bean.Food;
import com.qcl.meiju.FoodStatusEnum;
import com.qcl.repository.FoodRepository;
import com.qcl.repository.LeiMuRepository;
import com.qcl.utils.ApiUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@Slf4j
public class WxFoodController {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private LeiMuRepository leiMuRepository;

    /*
     * 返回菜单和菜品列表
     * */
    /*
    @GetMapping("/buyerfoodList")
    public ResultVO list(@RequestParam("searchKey") String searchKey) {
        log.info("搜索词={}", searchKey);
        List<Food> foodList = new ArrayList<>();
        if (StringUtils.pathEquals("all", searchKey)) {
            //返回所有菜品
            foodList = foodRepository.findByFoodStatus(FoodStatusEnum.UP.getCode());
        } else {
            //查询菜品
            foodList = foodRepository.findByFoodStatusAndFoodNameContaining(FoodStatusEnum.UP.getCode(), searchKey);
            log.info("搜索结果={}", foodList);
        }

        return zuZhuang(foodList);
    }*/

    @GetMapping("/buyerfoodList")
    public ResultVO list(@RequestParam("searchKey") String searchKey, @RequestParam("adminId") Integer adminId) {
        log.info("搜索词={}, adminId={}", searchKey, adminId);
        List<Food> foodList = new ArrayList<>();

        // 根据adminId和搜索条件获取菜品
        if (searchKey != null && !searchKey.trim().isEmpty() && !"all".equals(searchKey)) {
            foodList = foodRepository.findByFoodStatusAndAdminIdAndFoodNameContaining(
                    FoodStatusEnum.UP.getCode(),
                    adminId, // 加入adminId作为查询条件
                    searchKey
            );
        } else {
            // 如果不传searchKey或者searchKey为all，返回该adminId下的所有菜品
            foodList = foodRepository.findByFoodStatusAndAdminId(
                    FoodStatusEnum.UP.getCode(),
                    adminId // 加入adminId作为查询条件
            );
        }

        log.info("搜索结果={}", foodList);
        return zuZhuang(foodList);
    }


    public ResultVO zuZhuang(List<Food> foodList) {
        List<Integer> categoryTypeList = foodList.stream()
                .map(e -> e.getLeimuType())
                .collect(Collectors.toList());
        List<Leimu> leimuList = leiMuRepository.findByLeimuTypeIn(categoryTypeList);

        //3. 数据拼装
        List<LeimuVO> leimuVOList = new ArrayList<>();
        for (Leimu leimu : leimuList) {
            LeimuVO leimuVO = new LeimuVO();
            leimuVO.setLeimuType(leimu.getLeimuType());
            leimuVO.setLeimuName(leimu.getLeimuName());

            List<FoodRes> foodResList = new ArrayList<>();
            for (Food food : foodList) {
                if (food.getLeimuType().equals(leimu.getLeimuType())) {
                    FoodRes foodRes = new FoodRes();
                    BeanUtils.copyProperties(food, foodRes);
                    foodResList.add(foodRes);
                }
            }
            leimuVO.setFoodResList(foodResList);
            leimuVOList.add(leimuVO);
        }

        return ApiUtil.success(leimuVOList);
    }
}
