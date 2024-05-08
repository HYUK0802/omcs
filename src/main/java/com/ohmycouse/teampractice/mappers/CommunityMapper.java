package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.ArticlesEntity;
import com.ohmycouse.teampractice.entities.ImageEntity;
import com.ohmycouse.teampractice.models.PagingModel;
import com.ohmycouse.teampractice.entities.*;
import com.ohmycouse.teampractice.vos.ArticleVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommunityMapper {

    int insertArticle(ArticlesEntity article);

    int insertComment(CommentEntity comment);

    int insertImage(ImageEntity image);

    int insertLike(LikeEntity like);

    int updateArticle(ArticlesEntity article);

    int updateComment(CommentEntity comment);

    ArticlesEntity selectArticleByIndex(@Param("index")int index);

    ArticleVo selectArticleVoByIndex(@Param("index")int index);

    ImageEntity selectImage(@Param("index") int index);

    CommentEntity[] selectCommentsByArticleIndex(@Param(value = "articleIndex")int articleIndex);

    CommentEntity selectComment(@Param(value = "index")int index);

    LikeEntity selectLikesByArticleIndex(@Param(value = "articleIndex")int articleIndex);


    List<ArticlesEntity> findArticle();
    List<ArticlesEntity> articleOrderByCreatedAt();
    List<ArticlesEntity> articleOrderByView();

    int updateByIndex(ArticlesEntity article);

    int deleteByIndex(@Param(value = "index")int index);



    UserEntity selectUserByEmail(@Param(value = "email") String email);

    ArticlesEntity findArticleByEmail();

    int selectCount(@Param(value = "searchCriterion")String searchCriterion,
                    @Param(value = "searchQuery")String searchQuery);

    ArticlesEntity[] selectByPage(@Param(value = "pagingModel") PagingModel pagingModel,
                                  @Param(value = "searchCriterion")String searchCriterion,
                                  @Param(value = "searchQuery")String searchQuery,
                                  @Param(value = "sort")String sort);

}
