package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.CourseEntity;
import com.ohmycouse.teampractice.entities.DetailPlanEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseMapper {

    CourseEntity[] selectApiDB(@Param(value = "clientX") double clientX,
                               @Param(value = "clientY") double clientY);
//    인터페이스에서 @Param값은 xml의 where #{} 값

    PlannerEntity selectPlannerByTitle(@Param(value="plannerTitle") String plannerTitle);


    int insertPlanner(PlannerEntity plannerEntity);
    int insertDetailPlan(DetailPlanEntity detailPlanEntity);
}
