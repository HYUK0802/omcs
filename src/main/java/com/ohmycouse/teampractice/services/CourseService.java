package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.CourseEntity;
import com.ohmycouse.teampractice.entities.DetailPlanEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.mappers.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

@Service
public class CourseService {

    private final CourseMapper courseMapper;

    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public boolean savePlanner(HttpServletRequest request,
                               HttpSession session,
                               PlannerEntity plannerEntity,
                               Date startDate,
                               Date endDate,
                               int areaCode) {
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        plannerEntity.setEmail(loginUser.getEmail())
                .setCreateAt(new Date())
                .setPlannerTitle(request.getParameter("title"))
                .setStartDate(startDate)
                .setEndDate(endDate)
                .setAreaCode(areaCode);
        System.out.println("여긴가? :" + request.getParameter("title"));
        System.out.println(request.getParameter("title"));
        return this.courseMapper.insertPlanner(plannerEntity) > 0;
    }

    public boolean saveDetailPlan(HttpServletRequest request,
                                  HttpSession session,
                                  DetailPlanEntity detailPlanEntity,
                                  int plannerIndex,
                                  Date date,
                                  String title,
                                  int day
//                                  int contentIdValue,
//                                  String placeImg,
//                                  double mapX,
//                                  double mapY
    ) {

        System.out.println("서비스 디테일 타이틀 : " + title);
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        detailPlanEntity
                .setTourIndex(plannerIndex)
                .setEmail(loginUser.getEmail())
                .setDate(date)
                .setTitle(title)
//                .setContentId(contentIdValue)
//                .setPlaceImg(placeImg)
//                .setMapX(mapX)
//                .setMapY(mapY)
                .setDay(day)
        ;


        return this.courseMapper.insertDetailPlan(detailPlanEntity) > 0;
    }

//    public PlannerEntity getPlnner(int index){
//        return this.courseMapper.selectPlannerByIndex(index);
//    }

    public PlannerEntity getPlanner(String title) {
        System.out.println("서비스 플래너타이틀 : " + title);
        return this.courseMapper.selectPlannerByTitle(title);

    }


    //    public boolean putComment(HttpServletRequest request ,
//                              HttpSession session ,
//                              CommentEntity comment) {
//        UserEntity loginUser = (UserEntity) session.getAttribute("user");
//        comment.setDeleted(false)
//                .setCreatedAt(new Date())
//                .setClientIp(request.getRemoteAddr())
//                .setClientUa(request.getHeader("User-Agent"))
//                .setNickname(loginUser.getNickname());
//        return this.communityMapper.insertComment(comment) > 0;
//    }
//
    public CourseEntity getApiDB(double x, double y) {
        //사용자가 선택한 위도와 경도 받아옴
        System.out.println("=====컨트롤러에서 받아온 사용자 장소======");
        System.out.println(x);
        System.out.println(y);
        CourseEntity[] courseEntities = this.courseMapper.selectApiDB(x, y);
        HashMap<Double, Integer> map = new HashMap<Double, Integer>();
        int index = 0;
        for (CourseEntity apiDb : courseEntities) {
            Double d = calcApi(y, x, apiDb.getMapX(), apiDb.getMapY());
            map.put(d, index++);
        }
        Double[] d = map.keySet().toArray(new Double[0]);
        Arrays.sort(d);
        System.out.println("=====DB 장소와 거리계산후 가까운순 정렬===");
        for (Double aDouble : d) {
            System.out.println(aDouble.toString());
        }
        System.out.println("====첫번째로 가까운 여행지=======");
        System.out.println(map.get(d[0]));
        System.out.println("===========================");
        return courseEntities[map.get(d[0])];
    }

    public double calcApi(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy); // 피타고라스 좌표 거리 값 구하기
    }

}


