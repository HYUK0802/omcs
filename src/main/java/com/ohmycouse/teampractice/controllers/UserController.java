package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.*;
import com.ohmycouse.teampractice.enums.*;
import com.ohmycouse.teampractice.entities.RecoverEmailCodeEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.services.UserService;
import org.json.JSONObject;
import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.entities.RecoverContactCodeEntity;
import com.ohmycouse.teampractice.entities.RegisterContactCodeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

import javax.mail.MessagingException;

@Controller
@RequestMapping(value = "/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/login",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getLogin() {
        ModelAndView modelAndView = new ModelAndView("user/login");
        return modelAndView;
    }
    @RequestMapping(value = "/register",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRegister() {
        ModelAndView modelAndView = new ModelAndView("user/register");
        return modelAndView;
    }


    @RequestMapping(value = "/register",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRegister(UserEntity user
            , RegisterContactCodeEntity registerContactCode
    ) throws MessagingException {
        RegisterResult result = this.userService.register(user, registerContactCode);
        JSONObject responseObject = new JSONObject(){{
            put("result", result.name().toLowerCase());
            //.name()은 Java의 enum 타입 객체의 이름을 문자열로 반환하는 메서드입니다.
        }};
        return responseObject.toString();
    }
    @RequestMapping(value = "/contactCode",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getContactCode(RegisterContactCodeEntity registerContactCode) {
        SendRegisterContactCodeResult result = this.userService.sendRegisterContactCode(registerContactCode);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        if (result == SendRegisterContactCodeResult.SUCCESS) {
            responseObject.put("salt", registerContactCode.getSalt());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "/contactCode",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchContactCode(RegisterContactCodeEntity registerContactCode) {
        System.out.println("컨트롤러 : " + registerContactCode.getSalt());
        VerifyRegisterContactCodeResult result = this.userService.verifyRegisterContactCode(registerContactCode);
        System.out.println("컨트롤러 / 서비스 " + result);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }

    @RequestMapping(value = "/emailCode",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getEmailCode(RegisterEmailCodeEntity registerEmailCode) {
        VerifyRegisterEmailCodeResult result = this.userService.verifyRegisterEmailCode(registerEmailCode);
        System.out.println("이메일코드 컨트롤러 : " + result);

        return new ModelAndView() {{
            setViewName("user/emailCode");
            addObject("result", result.name());
        }};
    }

    @RequestMapping(value = "recoverpassword",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPw() {
        ModelAndView modelAndView = new ModelAndView("user/recoverpassword");
        return modelAndView;
    }


    @GetMapping(value = "recoverpwpage",
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverPassword(RecoverEmailCodeEntity recoverEmailCode) {
        VerifyRecoverEmailCodeResult result = this.userService.verifyRecoverEmailCodeResult(recoverEmailCode);
        ModelAndView modelAndView = new ModelAndView("user/recoverpwpage");
        modelAndView.addObject("result", result.name().toLowerCase());
        modelAndView.addObject("recoverEmailCode", recoverEmailCode);
        return modelAndView;
    }//이메일 인증 링크 보내기

    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postRecoverPassword(RecoverEmailCodeEntity recoverEmailCode) throws MessagingException {
        SendRecoverEmailCodeResult result = this.userService.sendRecoverEmailCodeResult(recoverEmailCode);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }//Db에서 이메일값찾기

    @RequestMapping(value = "recoverPassword",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchRecoverPassword(RecoverEmailCodeEntity recoverEmailCode, UserEntity user) {
        RecoverPasswordResult result = this.userService.recoverPassword(recoverEmailCode,user);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    } //비번 변경


    @RequestMapping(value = "login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postLogin(HttpSession session, UserEntity user) {
        LoginResult result = this.userService.login(user);
        if (result == LoginResult.SUCCESS) {
            session.setAttribute("user", user);
        }
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }

    @RequestMapping(value = "/recoveremail",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getRecoverEmail(){
        ModelAndView modelAndView = new ModelAndView("user/recoveremail");
        return modelAndView;
    }

    @RequestMapping(value = "contactCodeRec", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getContactCodeRec(RecoverContactCodeEntity recoverContactCode) {
        SendRecoverContactCodeResult result = this.userService.sendRecoverContactCodeResult(recoverContactCode);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
            put("salt", recoverContactCode.getSalt());
        }};
        return responseObject.toString();
    }
    @RequestMapping(value = "contactCodeRec", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String patchContactCodeRec(RecoverContactCodeEntity recoverContactCode,
                                      String contact) {
        VerifyRecoverContactCodeResult result = this.userService.verifyRecoverContactCodeResult(recoverContactCode);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        if (result == VerifyRecoverContactCodeResult.SUCCESS) {
            UserEntity user = this.userService.getUserByContact(contact);
            responseObject.put("email", user.getEmail());
        }
        return responseObject.toString();
    }

    @RequestMapping(value = "logout",
            method = RequestMethod.GET)
    public ModelAndView getLogout(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("redirect:/");
        session.setAttribute("user", null);
        return modelAndView;
    }
}
