package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.PlaceEntity;
import com.ohmycouse.teampractice.entities.ReviewEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.enums.MyPickResult;
import com.ohmycouse.teampractice.mappers.PlaceMapper;
import com.ohmycouse.teampractice.mappers.ReviewMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Service
public class ReviewService {

    public final ReviewMapper reviewMapper;
    public final PlaceMapper placeMapper;

    public ReviewService(ReviewMapper reviewMapper, PlaceMapper placeMapper) {
        this.reviewMapper = reviewMapper;
        this.placeMapper = placeMapper;
    }


    @Transactional
    public boolean putReview(HttpServletRequest request, ReviewEntity review, UserEntity user) {

        int reviewIndex = Integer.parseInt(request.getParameter("placeIndex"));
        String reviewContent = request.getParameter("reviewContent");
        int star = Integer.parseInt(request.getParameter("reviewStar"));


        review.setContentId(reviewIndex)
                .setContent(reviewContent)
                .setNickname(user.getNickname())
                .setDeleted(false) //삭제여부 처음엔 거짓
                .setCreatedAt(new Date()) //현재시간
                .setScore(star)
                .setClientIp(request.getRemoteAddr()) //사용자ip받아옴
                .setClientUa(request.getHeader("User-Agent")); //사용자브라우저


        return this.reviewMapper.insertReview(review) > 0;
    }
    public MyPickResult putMyPick(HttpServletRequest request,
                                  MyPickEntity myPick,
                                  HttpSession session,
                                  PlaceEntity place
                            ) {
        String placePic = request.getParameter("placePic");
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        String placeTitle = request.getParameter("placeTitle");
        String address = request.getParameter("address");
        int contentId = Integer.parseInt(request.getParameter("contentId"));
        if (loginUser == null) {
            return MyPickResult.NOT_LOGIN;
        }
        myPick .setFirstImage(placePic)
                .setAddr1(address)
                .setEmail(loginUser.getEmail())
                .setContentId(contentId)
                .setTitle(placeTitle);
        this.reviewMapper.insertMyPick(myPick);
        return MyPickResult.LOGIN;
    }


    public ReviewEntity[] selectReviewsByContentId(int contentId) {
        return reviewMapper.selectReviewsByContentId(contentId);
    }

    public boolean deleteByReview(int index, String nickname) {
        return this.reviewMapper.deleteByReview(index, nickname) > 0;
    }

    public boolean updateByReview(int index, String content) {
        return this.reviewMapper.updateByReview(index, content) > 0; //메서드타입이 boolen이기에 반환값자체가 true아니면 false가 나오게 한다.

    }

    public Double selectReviewByScoreAverage(int contentId) {
        Double averageScore = reviewMapper.selectReviewByScoreAverage(contentId);
        if (averageScore != null) {
            return Math.round(averageScore * 10.0) / 10.0;
        }
        return null;
    }
}
