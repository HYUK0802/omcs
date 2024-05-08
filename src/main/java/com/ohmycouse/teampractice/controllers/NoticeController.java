package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.NoticeEntity;
import com.ohmycouse.teampractice.models.PagingModel;
import com.ohmycouse.teampractice.services.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = "/")
public class NoticeController {

    private final NoticeService noticeService;

    @Autowired
    public NoticeController(NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @RequestMapping(value = "/notice",
            method = RequestMethod.GET)
    public ModelAndView getNotice(
            @RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
            @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
            @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery) {
        ModelAndView modelAndView = new ModelAndView("notice/notice");

        PagingModel pagingModel = new PagingModel(
                NoticeService.PAGE_COUNT,
                this.noticeService.getCount(searchCriterion, searchQuery), requestPage);
//        NoticeEntity[] noticeEntity = this.noticeService.getAll();
        NoticeEntity[] noticeEntities = this.noticeService.getByPage(pagingModel, searchCriterion, searchQuery);
        modelAndView.addObject("notice", noticeEntities);
        modelAndView.addObject("pagingModel", pagingModel);
        modelAndView.addObject("searchCriterion", searchCriterion);
        modelAndView.addObject("searchQuery", searchQuery);

        System.out.println("컨트롤러 requestPage : " + requestPage);
        System.out.println("컨트롤러 searchCriterion : " + searchCriterion);
        System.out.println("컨트롤러 searchQuery : " + searchQuery);
        return modelAndView;
    }

    @RequestMapping(value = "/noticeWrite",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postNotice(NoticeEntity noticeEntity,
                             @RequestParam("title") String title,
                             @RequestParam("content") String text) {
        System.out.println("공지사항 타이틀 : " + title);
        System.out.println("공지사항 내용 : " + text);

        boolean result = this.noticeService.saveNotice(
                noticeEntity, title, text
        );
        return String.valueOf(result);
    }

    @RequestMapping(value = "/deleteNotice/",
            method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteNotice(
            @RequestParam(value = "index") int index) {
        boolean result = this.noticeService.deleteNotice(index);
        if(result){
            return "true";
        }else{
            return "false";
        }
    }

    @RequestMapping(value = "/notice/patch",
    method = RequestMethod.PATCH)
    @ResponseBody
    public String promptText(@RequestParam(value = "index") int index,
                             @RequestParam(value = "text") String text){
        boolean result = this.noticeService.updateByNotice(index, text);

        if(result){
            return "true";
        }else{
            return "flase";
        }
    }

}
