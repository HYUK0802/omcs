package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.DetailPlanEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.entities.WithDrawContactCodeEntity;
import com.ohmycouse.teampractice.vos.PlannerVo;
import com.ohmycouse.teampractice.vos.DetailPlanVo;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface MyPageMapper {


    UserEntity selectUserByEmail(@Param(value = "email") String email);

    List<PlannerEntity> selectIndexByPlanner(@Param(value = "email") String email);
    List<MyPickEntity> selectIndexByMyPick(@Param(value = "email") String email);

    int updateUser(UserEntity user);

    UserEntity selectUserByContact(@Param(value = "contact") String contact);

    int insertWithDrawContactCodes(WithDrawContactCodeEntity withDrawContactCode);

    WithDrawContactCodeEntity selectRecoverContactCodeByContactCodeSalt(WithDrawContactCodeEntity withDrawContactCode);

    int updateWithDrawContactCode(WithDrawContactCodeEntity withDrawContactCode);


    int deleteWithDrawUser(UserEntity user);
    int deleteByIndex(@Param(value = "index")int index);
    int deleteByMyPick(@Param(value = "index")int index);


    int updatePassword(UserEntity user);

    //    int selectIndexPlan(PlannerEntity plannerEntity);
//    Select("SELECT * FROM planner_table WHERE index = #{index}")
    PlannerVo selectIndexPlan(int index);

    // tourIndex를 기준으로 세부 일정 정보를 조회하는 메서드
    List<DetailPlanVo> selectDetailPlansByTourIndex(int tourIndex);

    List<PlannerVo> selectImageByAreaCode(@Param(value = "email")String email);
    Map<Integer, List<DetailPlanEntity>> groupDetailPlansByDay(List<DetailPlanEntity> detailPlans);

    int updateByMemo(@Param(value = "index") int index,
                     @Param(value = "memo") String memo);
}
