package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.enums.ModifyNicknameResult;
import com.ohmycouse.teampractice.enums.ModifyPasswordResult;
import com.ohmycouse.teampractice.entities.WithDrawContactCodeEntity;
import com.ohmycouse.teampractice.enums.*;
import com.ohmycouse.teampractice.mappers.MyPageMapper;
import com.ohmycouse.teampractice.services.MyPageService;
import com.ohmycouse.teampractice.vos.DetailPlanVo;
import com.ohmycouse.teampractice.vos.PlannerVo;
import org.apache.catalina.User;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/")
public class MyPageController {
    private final MyPageMapper myPageMapper;
    private final MyPageService myPageService;



    public MyPageController( MyPageMapper myPageMapper, MyPageService myPageService) {
        this.myPageMapper = myPageMapper;
        this.myPageService = myPageService;
    }

    @RequestMapping(value = "/mypage",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyPage(HttpSession session, Model model){
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        if (loggedInUser == null) {
            return new ModelAndView("redirect:/user/login"); // 로그인 페이지로 리다이렉트
        }
        List<MyPickEntity> myPickEntities = myPageService.selectMyPick(loggedInUser);
        List<PlannerVo> plannerVo = myPageService.readAreaCode(loggedInUser);
        List<PlannerEntity> plannersToShow = new ArrayList<>();
        int count = 0;
//        List<PlannerEntity> plannerEntities = myPageService.selectPlanner(loggedInUser);
        for (PlannerVo planner : plannerVo) {
            if (planner.getEmail().equals(loggedInUser.getEmail())) {
                plannersToShow.add(planner);
                count++;
            }

            if (count >= 2) {
                break;
            }
        }

        System.out.println(plannerVo);
        model.addAttribute("plannersToShow", plannersToShow);
        model.addAttribute("plannerVo", plannerVo);
        model.addAttribute("loggedInUser", loggedInUser);
        model.addAttribute("myPickEntities", myPickEntities);
        return new ModelAndView("mypage/mypage");
    }
    @RequestMapping(value = "/mypage",
    method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteIndex(@RequestParam(value = "index")int index) {
        boolean result = this.myPageService.deleteIndex(index);
        return String.valueOf(result);
    }
    @RequestMapping(value = "/deleteMyPick",
    method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMyPick(@RequestParam(value = "index")int index) {
        boolean result = this.myPageService.deleteMyPick(index);
        return String.valueOf(result);
    }


    @GetMapping("/myPage/editPersonal")
    public String editPersonal(HttpSession session, Model model) {
        // HttpSession 객체를 통해 로그인된 사용자의 정보를 가져옵니다.
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        if (loggedInUser != null) {
            // 사용자 정보 활용하여 필요한 동작 수행
            UserEntity user = myPageMapper.selectUserByEmail(loggedInUser.getEmail());
            model.addAttribute("user", user);
            return "mypage/editpersonal";
        } else {
            // 로그인되지 않은 경우 처리
            return "redirect:/login";
        }
    }
    @GetMapping("/myPage/editPersonal/modifyNickname")
    public String modifyNickname(HttpSession session, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        // 로그인 됬을때
        if (loggedInUser != null) {
            UserEntity user = myPageMapper.selectUserByEmail(loggedInUser.getEmail());
            model.addAttribute("user", user);
            return "mypage/modifynickname";
        }else {
            return "redirect:/login";
        }
        // 로그인 안됬을때
    }
    @RequestMapping(value = "/myPage/editPersonal/modifyNickname",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String modifyNickname(HttpSession session, @RequestParam("nickname") String nickname) {
            UserEntity user = (UserEntity) session.getAttribute("user");
            user.setNickname(nickname);
            ModifyNicknameResult result = this.myPageService.modifyNickname(user);
            JSONObject responseObject = new JSONObject() {{
                put("result", result.name().toLowerCase());
            }};
            return responseObject.toString();
        }


//---------------------------------------------------------------------------------

    @GetMapping("/myplan")
    public ModelAndView myPlan(
            @RequestParam(value = "index", defaultValue = "1", required = false) int index,
            HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("mypage/myPlan");

        UserEntity user = (UserEntity) session.getAttribute("user");
        PlannerVo planner = this.myPageService.readPlan(index);
        modelAndView.addObject("planner", planner);
        modelAndView.addObject("sessionUser", user);

        List<DetailPlanVo> detailPlans = this.myPageService.getDetailPlansByTourIndex(index);
        modelAndView.addObject("detailPlans", detailPlans);

        Map<Integer, List<DetailPlanVo>> groupedDetailPlans = this.myPageService.groupDetailPlansByDay(detailPlans);
        System.out.println("groupedDetailPlans: " + groupedDetailPlans);

        modelAndView.addObject("groupedDetailPlans", groupedDetailPlans); // 변경된 부분

        return modelAndView;
    }



    @PostMapping("/process-plan-title")
    public ResponseEntity<String> processPlanTitle(@RequestBody String planTitle) {
        // planTitle 값을 컨트롤러에서 사용하는 로직을 작성합니다.
        System.out.println("Received plan title: " + planTitle);
        // 처리가 완료되면 적절한 ResponseEntity를 반환합니다.
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    @RequestMapping(value = "/memo",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateMemo(@RequestParam("index") int index,
                             @RequestParam("patchMemo") String memo){

        System.out.println("디테일 인덱스 : " + index);
        System.out.println("수정할 메모 내용 :" + memo);

        boolean result = this.myPageService.updateMemoService(index, memo);
        return String.valueOf(result);
    }
    //    -------------------------------------------------------------------------------------------
    @RequestMapping(value = "/myPage/editPersonal/modifyPassword",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getModifyPassword(){
        return new ModelAndView("mypage/modifypassword");
    }

    @RequestMapping(value = "/myPage/editPersonal/modifyPassword",
            method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String modifyPassword(HttpSession session, @RequestParam("password") String password) {
        UserEntity user = (UserEntity) session.getAttribute("user");
        ModifyPasswordResult result = this.myPageService.modifyPassword(password, user, session);

        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }

    @RequestMapping(value = "/mypage/editPersonal/withDraw",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWithDraw(){
        return new ModelAndView("mypage/withDraw");
    }

//   -------------------------------------------------------------------------------------------------
    @RequestMapping(value = "/mypage/editPersonal/withDraw",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getContactCodeRec(WithDrawContactCodeEntity withDrawContactCode,HttpSession session) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        session.setAttribute("user", loggedInUser);


        SendDeleteContactCodeResult result = this.myPageService.deleteContactCodeResult(withDrawContactCode,
                session);

        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};

        if (result == SendDeleteContactCodeResult.SUCCESS) {
            responseObject.put("salt", withDrawContactCode.getSalt());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/mypage/editPersonal/withDraw",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchContactCodeRec(WithDrawContactCodeEntity withDrawContactCode) {

        VerifyDeleteContactCodeResult result = this.myPageService.verifyDeleteContactCodeResult(withDrawContactCode);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};

        return responseObject.toString();
    }

    @RequestMapping(value = "/mypage/editPersonal/withDraw",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String deleteUser(WithDrawContactCodeEntity withDrawContactCode,HttpSession session) {

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        session.setAttribute("user", loggedInUser);

        DeleteUserResult result = this.myPageService.deleteUserResult(withDrawContactCode,session);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        if (result == DeleteUserResult.SUCCESS) {
            session.invalidate();
        }


        return responseObject.toString();
    }
//   -------------------------------------------------------------------------------------------------
}
