package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.ArticlesEntity;
import com.ohmycouse.teampractice.entities.ImageEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.enums.EditWriteResult;
import com.ohmycouse.teampractice.entities.*;
import com.ohmycouse.teampractice.enums.PostLikeResult;
import com.ohmycouse.teampractice.mappers.CommunityMapper;
import com.ohmycouse.teampractice.models.PagingModel;
import com.ohmycouse.teampractice.vos.ArticleVo;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@Service
public class CommunityService {

    private final CommunityMapper communityMapper;
    public static final int PAGE_COUNT = 9; // 페이지 당 메모 개수


    @Autowired
    public CommunityService(CommunityMapper communityMapper) {
        this.communityMapper = communityMapper;
    }

    public List<ArticlesEntity> findArticle() {
        return communityMapper.findArticle();
    }

    public List<ArticlesEntity> OrderByCreatedAt() {
        return communityMapper.articleOrderByCreatedAt();
    }

    public List<ArticlesEntity> OrderByView() {
        return communityMapper.articleOrderByView();
    }

    public int getCount(String searchCriterion, String searchQuery) { // 모든 메모의 개수를 조회하는 메소드
        return this.communityMapper.selectCount(searchCriterion, searchQuery); // MemoMapper 를 통해 모든 메모의 개수를 조회하고 반환
    }

    public ArticlesEntity[] getByPage(PagingModel pagingModel, String searchCriterion, String searchQuery, String sort) { //페이지별 메모를 조회하는 메소드

        ArticlesEntity[] articlesEntities = this.communityMapper.selectByPage(pagingModel, searchCriterion, searchQuery, sort); // MemoMapper를 통해 페이지별 메모를 조회하고 MemoEntity배열로 반환
        return articlesEntities;
    }

    public ArticlesEntity selectEmail() {
        return communityMapper.findArticleByEmail();
    }

    public ArticlesEntity selectArticleByIndex(int index) {
        return communityMapper.selectArticleByIndex(index);
    }


    public ArticleVo readArticle(int index) {
        ArticleVo article = this.communityMapper.selectArticleVoByIndex(index);
        if (article != null && !article.isDeleted()) {
            article.setView(article.getView() + 1);
            this.communityMapper.updateArticle(article);
        }
        return article;
    }

    public ImageEntity putImage(UserEntity user, MultipartFile file, HttpServletRequest request) throws IOException {
        // 세션에서 로그인된 유저의 이메일 가져오기
        String email = user.getEmail();
        String nickname = user.getNickname();
//        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");


        ImageEntity image = new ImageEntity()
                .setEmail(email) // 유저의 이메일
                .setName(file.getName())
                .setSize(file.getSize())
                .setContentType(file.getContentType())
                .setData(file.getBytes())
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"));

        this.communityMapper.insertImage(image);
        return image;
    }

    public ImageEntity getImage(int index) {
        return this.communityMapper.selectImage(index);
    }

    @Transactional
    public boolean putArticle(UserEntity user, HttpServletRequest request, ArticlesEntity article, MultipartFile[] files) throws IOException {
        String email = user.getEmail();
        String nickname = user.getNickname();

        article.setView(0)
                .setEmail(email)
                .setNickname(nickname)
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"))
                .setDeleted(false);
        return this.communityMapper.insertArticle(article) >= 1;
    }

    // 수정하기
    public EditWriteResult editWriteResult(ArticlesEntity article) {
        if (article.getTitle() == null ||
                article.getContent() == null) {
            return EditWriteResult.FAILURE;
        }

        return this.communityMapper.updateByIndex(article) > 0
                ? EditWriteResult.SUCCESS
                : EditWriteResult.FAILURE;
    }

    // 삭제하기
    public boolean deleteByIndex(int index) {
        return this.communityMapper.deleteByIndex(index) > 0; // 0이면 삭제 못한

    }

    public boolean putComment(HttpServletRequest request,
                              HttpSession session,
                              CommentEntity comment) {
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        comment.setDeleted(false)
                .setCreatedAt(new Date())
                .setClientIp(request.getRemoteAddr())
                .setClientUa(request.getHeader("User-Agent"))
                .setNickname(loginUser.getNickname());
        return this.communityMapper.insertComment(comment) > 0;
    }

    public CommentEntity[] getCommentsOf(int articleIndex) {
        return this.communityMapper.selectCommentsByArticleIndex(articleIndex);
    }

    public boolean deleteComment(CommentEntity comment) {
        comment = this.communityMapper.selectComment(comment.getIndex());
        if (comment == null) {
            return false;
        }
        comment.setDeleted(true);
        return this.communityMapper.updateComment(comment) > 0;
    }

    public PostLikeResult putLike(LikeEntity like,
                                  HttpSession session) {
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        if (loginUser == null) {
            return PostLikeResult.NOT_LOGIN;
        }
        like.setNickname(loginUser.getNickname());
        this.communityMapper.insertLike(like);
        return PostLikeResult.LOGIN;
    }
}
