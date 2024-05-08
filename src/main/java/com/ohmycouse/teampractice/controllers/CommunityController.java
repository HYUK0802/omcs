package com.ohmycouse.teampractice.controllers;

import com.ohmycouse.teampractice.entities.ArticlesEntity;
import com.ohmycouse.teampractice.entities.ImageEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.enums.EditWriteResult;
import com.ohmycouse.teampractice.enums.PostLikeResult;
import com.ohmycouse.teampractice.models.PagingModel;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;
import com.ohmycouse.teampractice.entities.*;
import com.ohmycouse.teampractice.mappers.CommunityMapper;
import com.ohmycouse.teampractice.services.CommunityService;
import com.ohmycouse.teampractice.vos.ArticleVo;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Controller
@RequestMapping(value = "/")
public class CommunityController {

    private final CommunityService communityService;

    public CommunityController(CommunityService communityService) {
        this.communityService = communityService;
    }


    @RequestMapping(value = "/community/write",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getWrite(HttpSession session, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        if (loggedInUser == null) {
            // 로그인 후 이용해야 함을 알리는 메시지를 모델에 추가
            model.addAttribute("message", "로그인 후 이용해주세요.");
            return new ModelAndView("redirect:/user/login"); // 로그인 페이지로 리다이렉트
        }
        return new ModelAndView("community/write");
    }

    @RequestMapping(value = "/community/write",
            method = RequestMethod.POST,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView postWrite(HttpServletRequest request,
                                  @SessionAttribute(value = "user") UserEntity user,
                                  ArticlesEntity article,
                                  @RequestParam(value = "files", required = false) MultipartFile[] files) throws IOException {
        if (files == null) {
            files = new MultipartFile[0];
        }
        boolean result = this.communityService.putArticle(user, request, article, files);
        ModelAndView modelAndView = new ModelAndView();
        if (result) {
            modelAndView.setViewName("redirect:/community/");
        } else {
            modelAndView.setViewName("community/write");
            modelAndView.addObject("result", result);
        }
        return modelAndView;
    }

    @RequestMapping(value = "/community/uploadImage",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)

    @ResponseBody
    public String postUploadImage(HttpServletRequest request,
                                  @SessionAttribute(value = "user") UserEntity user,
                                  @RequestParam(value = "upload") MultipartFile file
    ) throws IOException {
        ImageEntity image = this.communityService.putImage(user, file, request);
        JSONObject responseObject = new JSONObject() {{
            put("url", "/community/downloadImage?index=" + image.getIndex());
        }};

        return responseObject.toString();
    }

    @RequestMapping(value = "/community/downloadImage", method = RequestMethod.GET)
    public ResponseEntity<byte[]> getDownloadImage(@RequestParam(value = "index") int index) {
        ImageEntity file = this.communityService.getImage(index);
        ResponseEntity<byte[]> response;
        if (file == null) {
            response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentLength(file.getSize());
            headers.setContentType(MediaType.parseMediaType(file.getContentType()));
            response = new ResponseEntity<>(file.getData(), headers, HttpStatus.OK);
        }
        return response;
    }

    @RequestMapping(value = "/community/editWrite",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getEditWrite(Model model, @RequestParam(value = "index", defaultValue = "1", required = false) int index, ArticlesEntity article) {
        article = communityService.selectArticleByIndex(index);
        model.addAttribute("article", article);
        return new ModelAndView("community/editWrite");
    }

    @RequestMapping(value = "/community/editWrite",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String editWrite(ArticlesEntity article,
                            @RequestParam(value = "index", defaultValue = "1", required = false) int index,
                            @RequestParam("title") String title,
                            @RequestParam("content") String content) {
//        article.setIndex(index);

        article.setTitle(title);
        article.setContent(content);
        EditWriteResult result = this.communityService.editWriteResult(article);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }

    private List<ArticleVo> convertArticlesToArticleVos(List<ArticlesEntity> articles) {
        List<ArticleVo> articleVos = new ArrayList<>();
        for (ArticlesEntity article : articles) {
            ArticleVo articleVo = new ArticleVo();
            articleVo.setIndex(article.getIndex());
            articleVo.setEmail(article.getEmail());
            articleVo.setNickname(article.getNickname());
            articleVo.setTitle(article.getTitle());
            articleVo.setContent(article.getContent());
            articleVo.setView(article.getView());
            articleVo.setCreatedAt(article.getCreatedAt());
            articleVo.setDeleted(article.isDeleted());
            articleVo.setClientIp(article.getClientIp());
            articleVo.setClientUa(article.getClientUa());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
            String strNowDate = simpleDateFormat.format(article.getCreatedAt());
            articleVo.setFirstCreatedAt(strNowDate);

            Pattern pattern = Pattern.compile("<img .*?>");
            Matcher matcher = pattern.matcher(article.getContent());
            if (matcher.find()) {
                articleVo.setFirstImage(matcher.group());
            } else {
                articleVo.setFirstImage("<i>이미지가 없습니다.</i>");
            }

            String regex = "<p>|<\\/p>|<img[^>]+>|<figure[^>]+>.*?</figure>";
            String output = article.getContent().replaceAll(regex, "");
            articleVo.setFirstText(output);

            articleVos.add(articleVo);
        }
        return articleVos;
    }

    @RequestMapping(value = "/community", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCommunity(HttpSession session, Model model,
                                     @RequestParam(value = "p", defaultValue = "1", required = false) int requestPage,
                                     @RequestParam(value = "c", defaultValue = "content", required = false) String searchCriterion,
                                     @RequestParam(value = "q", defaultValue = "", required = false) String searchQuery,
                                     @RequestParam(value = "sort", defaultValue = "latest") String sort) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        ModelAndView modelAndView = new ModelAndView("community/community");


        PagingModel pagingModel = new PagingModel(
                CommunityService.PAGE_COUNT,
                this.communityService.getCount(searchCriterion, searchQuery),
                requestPage
        );
        List<ArticlesEntity> articlesEntities = List.of(this.communityService.getByPage(pagingModel, searchCriterion, searchQuery, sort));
        List<ArticleVo> articleVos = convertArticlesToArticleVos(articlesEntities);

        modelAndView.addObject("articleVos", articleVos);
        modelAndView.addObject("pagingModel", pagingModel);
        modelAndView.addObject("searchCriterion", searchCriterion);
        modelAndView.addObject("searchQuery", searchQuery);
        return modelAndView;
    }


    @RequestMapping(value = "/community/myWriteList", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getMyWriteList(HttpSession session, Model model) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        if (loggedInUser == null) {
            return new ModelAndView("redirect:/user/login"); // 로그인 페이지로 리다이렉트
        }
        ArticlesEntity article = communityService.selectEmail();
        List<ArticlesEntity> articles = communityService.findArticle();
        List<ArticleVo> articleVos = convertArticlesToArticleVos(articles);
        model.addAttribute("articles", articleVos);
        model.addAttribute("loggedInUser", loggedInUser);
        return new ModelAndView("community/myWriteList");
    }

    @RequestMapping(value = "/community/myWriteList", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteIndex(@RequestParam(value = "index") int index) {
        boolean result = this.communityService.deleteByIndex(index);
        return String.valueOf(result);
    }

    @RequestMapping(value = "/community/communityDetail",
            method = RequestMethod.GET,
            produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView getCommunityDetail(@RequestParam(value = "index", defaultValue = "1", required = false) int index,
                                           @RequestParam(value = "articleIndex", defaultValue = "1", required = false) int articleIndex) {
        ModelAndView modelAndView = new ModelAndView("community/communityDetail");
        ArticleVo article = this.communityService.readArticle(index);

        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @RequestMapping(value = "/community/communityDetail/comment",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postComment(HttpServletRequest request,
                              CommentEntity comment,
                              HttpSession session) {
        System.out.println("HttpServletRequest : " + request.getParameter("content"));
        boolean result = this.communityService.putComment(
                request, session, comment);
        return String.valueOf(result);
    }

    @RequestMapping(value = "/community/communityDetail/comment",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String getComment(@SessionAttribute(value = "user", required = false) UserEntity user,
                             @RequestParam(value = "articleIndex") int articleIndex) {
        CommentEntity[] comments = this.communityService.getCommentsOf(articleIndex);
        JSONArray responseArray = new JSONArray();
        for (CommentEntity comment : comments) {
            JSONObject commentObject = new JSONObject(comment);
            commentObject.put("createdAt", comment.getFormattedCreatedAt());
            commentObject.put("mine", user != null && comment.getNickname().equals(user.getNickname()));
            responseArray.put(commentObject);
        }
        return responseArray.toString();
    }

    @RequestMapping(value = "/community/communityDetail/comment", method = RequestMethod.DELETE, produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String deleteComment(CommentEntity comment) {
        boolean result = this.communityService.deleteComment(comment);
        return String.valueOf(result);
    }

    @RequestMapping(value = "/community/communityDetail",
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String postLike(LikeEntity like,
                           HttpSession session) {
        PostLikeResult result = this.communityService.putLike(like , session);
        JSONObject responseObject = new JSONObject() {{
            put("result", result.name().toLowerCase());
        }};
        return responseObject.toString();
    }
}
