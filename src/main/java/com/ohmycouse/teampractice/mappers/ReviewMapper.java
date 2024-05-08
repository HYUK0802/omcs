package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.ReviewEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

@Mapper
public interface ReviewMapper {

    int insertReview(ReviewEntity review);

    ReviewEntity[] selectReviewsByContentId(@Param(value = "contentId") int contentId);

    int deleteByReview(@Param(value = "index") int index,
                       @Param(value = "nickname") String nickname);

    int updateByReview(@Param(value = "index") int index,
                       @Param(value = "content") String content); //레코드의 개수반환하기에 int타입 사용한다

    Double selectReviewByScoreAverage(@Param(value = "contentId") int contentId);

    int insertMyPick(MyPickEntity myPick);
}
