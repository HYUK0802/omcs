package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.enums.MyPickResult;
import com.ohmycouse.teampractice.models.PagingModel;
import com.ohmycouse.teampractice.entities.ReviewEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.services.PlaceService;
import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;
import com.ohmycouse.teampractice.services.ReviewService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class HomeController {
    private final PlaceService placeService;
    public final ReviewService reviewService;

    @Autowired
    public HomeController(PlaceService placeService, ReviewService reviewService) {
        this.placeService = placeService;
        this.reviewService = reviewService;
    }

    @RequestMapping(value = "/",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getHome(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("home");

        UserEntity user = (UserEntity) session.getAttribute("user");
        modelAndView.addObject("user", user);
        return modelAndView;
    }


    @RequestMapping(value = "/place",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getAreaPlaces(@RequestParam(value = "areaCode", required = false, defaultValue = "1") int areaCode,
                                @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        Pair<PlaceEntity[], PagingModel> pair = this.placeService.getPlacesPaged(areaCode, page);
        PlaceEntity[] places = pair.getLeft();
        PagingModel pagingModel = pair.getRight();
        JSONObject responseObject = new JSONObject();
        JSONObject pagingObject = new JSONObject();
        pagingObject.put("minPage", pagingModel.minPage);
        pagingObject.put("maxPage", pagingModel.maxPage);
        pagingObject.put("displayStartPage", pagingModel.displayStartPage);
        pagingObject.put("displayEndPage", pagingModel.displayEndPage);
        responseObject.put("places", places);
        responseObject.put("paging", pagingObject);
        return responseObject.toString();
    }


    @GetMapping(value = "detail",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getDetail(@RequestParam(value = "contentId") int contentId) {
        ModelAndView modelAndView = new ModelAndView("detail");
        PlaceEntity place = placeService.getPlace(contentId);
        ReviewEntity[] reviews = reviewService.selectReviewsByContentId(contentId);
        Double reviewAverageScore = reviewService.selectReviewByScoreAverage(contentId);

        modelAndView.addObject("place", place);
        modelAndView.addObject("reviews", reviews);
        modelAndView.addObject("reviewAverageScore", reviewAverageScore);

        return modelAndView;
    }

    @RequestMapping(value = "review",
            method = RequestMethod.POST)
    @ResponseBody
    public ModelAndView postReview(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            HttpServletRequest request,
            ReviewEntity review) {
        ModelAndView modelAndView;
        if (user == null) {
            modelAndView = new ModelAndView("redirect:/user/login");
        } else {
            boolean result = this.reviewService.putReview(request, review, user);
            modelAndView = new ModelAndView("redirect:/detail?contentId=" + review.getContentId());
            modelAndView.addObject("review", review);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteByReview(
            @SessionAttribute(value = "user", required = false) UserEntity user,
            @RequestParam(value = "index") int index) {


        if (user == null) {
            // 로그인하지 않은 경우
            return "false";
        }

        boolean result = this.reviewService.deleteByReview(index, user.getNickname());
        if (result) {
            // 리뷰 삭제 성공
            return "true";
        } else {
            // 리뷰 삭제 실패
            return "false";
        }
    }

    @RequestMapping(value = "/", method = RequestMethod.PATCH)
    @ResponseBody //결과를 html에 전달
    public String alterText(@RequestParam(value = "index") int index,
                            @RequestParam(value = "content") String content) {
        boolean result = this.reviewService.updateByReview(index, content);
        return String.valueOf(result); //참이면 결과 반환
    }

    @PostMapping(value = "myPick",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postMyPick(HttpServletRequest request,
            MyPickEntity myPick,
                             HttpSession session,
                             PlaceEntity place){
        MyPickResult result = this.reviewService.putMyPick(request, myPick, session, place);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }
}
