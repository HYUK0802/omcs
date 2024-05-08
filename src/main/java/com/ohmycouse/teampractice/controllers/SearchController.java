package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.models.PagingModel;
import com.ohmycouse.teampractice.services.PlaceService;
import com.ohmycouse.teampractice.services.SearchService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value = "/")
public class SearchController {

    private final SearchService searchService;
    private final PlaceService placeService;

    public SearchController(SearchService searchService, PlaceService placeService) {
        this.searchService = searchService;
        this.placeService = placeService;
    }

    @RequestMapping(value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getSearch(HttpSession session, Model model,
                                  @RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
                                  @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
                                  @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery,
                                  @RequestParam(value = "sort", defaultValue = "latest")String sort) {
        ModelAndView modelAndView = new ModelAndView("search/search");

        PagingModel pagingModel = new PagingModel(
                SearchService.PAGE_COUNT,
                this.searchService.getCount(searchCriterion, searchQuery),
                requestPage
        );
        PlaceEntity[] placeEntities = this.searchService.getByPage(pagingModel, searchCriterion, searchQuery);


        modelAndView.addObject("searchPage",placeEntities);
        modelAndView.addObject("PagingModel", pagingModel);
        modelAndView.addObject("searchCriterion", searchCriterion);
        modelAndView.addObject("searchQuery", searchQuery);
        return modelAndView;
    }

}
