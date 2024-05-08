package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping(value = "/")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @RequestMapping(value = "/admin",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getAdmin(HttpSession session) {
        ModelAndView modelAndView = new ModelAndView("admin/admin");

        List<UserEntity> userList = this.adminService.readUser();
        modelAndView.addObject("userList", userList);

        UserEntity user = (UserEntity) session.getAttribute("user");
        modelAndView.addObject("user", user);

        return modelAndView;
    }

    @RequestMapping(value = "/admin/delete/",
    method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteUserAdmin(@RequestParam(value = "email") String email){
        boolean result = this.adminService.deleteUserAdmin(email);
        if(result){
            return "true";
        }else{
            return "false";
        }
    }

    @RequestMapping(value = "/admin/patch",
    method = RequestMethod.PATCH)
    @ResponseBody
    public String patchUserAdmin(@RequestParam(value = "contact") String contact,
                                 @RequestParam(value = "changeContact")String changeContact){
        boolean result = this.adminService.updateByUserAdmin(contact, changeContact);

        if(result){
            return "true";
        }else{
            return "false";
        }
    }
}
