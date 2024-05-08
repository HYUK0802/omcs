package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.CourseEntity;
import com.ohmycouse.teampractice.entities.DetailPlanEntity;
import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import com.ohmycouse.teampractice.services.CourseService;
import com.ohmycouse.teampractice.services.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;


@Controller
@RequestMapping(value = "/")
public class CourseController {
    private final CourseService courseService;
    private final PlaceService placeService;

    @Autowired
    public CourseController(CourseService courseService, PlaceService placeService) {
        this.courseService = courseService;
        this.placeService = placeService;
    }
    @RequestMapping(value = "/course",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getIndex() {
        ModelAndView modelAndView = new ModelAndView("course/course");
        return modelAndView;
    }
    @RequestMapping(value = "/course/place",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PlaceEntity[] getCoursePlaces(@RequestParam(value = "areaCode") int areaCode) {
        System.out.println("코스 지역번호!! :" + areaCode);
        return this.placeService.getPlaces(areaCode);
    }

    @RequestMapping(value = "/planner",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public PlannerEntity getPlannerIndex(@RequestParam(value = "title") String title){
        System.out.println(" 플래너 타이틀 : " + title);
        return this.courseService.getPlanner(title);
    }


    @RequestMapping(value="/planner",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postPlanner(HttpServletRequest request,
                              HttpSession session,
                              PlannerEntity plannerEntity,
                              @RequestParam("start_Date") String startDate,
                              @RequestParam("end_Date") String endDate,
                              @RequestParam("areaCode")int areaCode) throws ParseException {
        System.out.println("코스만들기 컨트롤러 HttpServletRequest : " + request.getParameter("title"));
        System.out.println("코스만들기 컨트롤러 startDate : " + startDate);
//        System.out.println("컨트롤러 areaCode:" + areaCode);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sday = sdf.parse(startDate);
        Date eday = sdf.parse(endDate);
        boolean result = this.courseService.savePlanner(
                request, session, plannerEntity, sday, eday, areaCode);
        return String.valueOf(result);
    }

    @RequestMapping(value = "/detailPlan",
    method = RequestMethod.POST,
    produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postDetailPlan(HttpServletRequest request,
                                 HttpSession session,
                                 DetailPlanEntity detailPlanEntity,
                                 @RequestParam("plannerIndex") int plannerIndex,
                                 @RequestParam("thisDate") String date,
                                 @RequestParam("title")String title,
                                 @RequestParam("day")int day
//                                 @RequestParam("contentId") int contentId,
//                                 @RequestParam("place-img") String placeImg,
//                                 @RequestParam("mapX")double mapX,
//                                 @RequestParam("mapY")double mapY
    ) throws ParseException {
        System.out.println("디테일저장 날짜 : " +date);
        System.out.println("컨트롤러 디테일저장 타이틀 : " + title);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date detailDate = sdf.parse(date);

        System.out.println("Date 형식으로 전환되나? : " + detailDate );

        boolean result = this.courseService.saveDetailPlan(
                request, session, detailPlanEntity,plannerIndex, detailDate, title, day
        );
        return String.valueOf(result);
    }


    @RequestMapping(value = "/",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String calcApi(@RequestParam(value = "x") double x,
                               @RequestParam(value = "y") double y) {
//        System.out.println(calcApi(x,y));
        System.out.println(x);
        System.out.println(y);
        CourseEntity apiDb = this.courseService.getApiDB(x, y);
        System.out.println(apiDb.getMapX());
        System.out.println(apiDb.getMapY());
        return "true";
    }

    
}

