package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.MyPickEntity;
import com.ohmycouse.teampractice.entities.DetailPlanEntity;
import com.ohmycouse.teampractice.entities.PlannerEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.entities.WithDrawContactCodeEntity;
import com.ohmycouse.teampractice.enums.*;
import com.ohmycouse.teampractice.enums.ModifyNicknameResult;
import com.ohmycouse.teampractice.mappers.MyPageMapper;

import com.ohmycouse.teampractice.utils.CryptoUtil;
import com.ohmycouse.teampractice.utils.NCloudUtil;
import com.ohmycouse.teampractice.vos.DetailPlanVo;
import com.ohmycouse.teampractice.vos.PlannerVo;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.*;

@Service
public class MyPageService {

    private final MyPageMapper myPageMapper;
    private WithDrawContactCodeEntity withDrawContactCode;


    @Autowired
    public MyPageService(MyPageMapper myPageMapper) {
        this.myPageMapper = myPageMapper;

    }

    public List<PlannerEntity> selectPlanner(UserEntity user) {
        return myPageMapper.selectIndexByPlanner(user.getEmail());
    }
    public List<MyPickEntity> selectMyPick(UserEntity user) {
        return myPageMapper.selectIndexByMyPick(user.getEmail());
    }

    public UserEntity getUserByEmail(String email) {
        return myPageMapper.selectUserByEmail(email);
    }


    public boolean isUserLoggedIn(HttpSession session) {
        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");
        return loggedInUser != null;
    }

    public List<PlannerVo> readAreaCode(UserEntity user) {
        return this.myPageMapper.selectImageByAreaCode(user.getEmail());
    }

    public ModifyNicknameResult modifyNickname(UserEntity user) {
        if (user.getNickname() == null) {
            return ModifyNicknameResult.FAILURE;
        }
        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        return this.myPageMapper.updateUser(user) > 0
                ? ModifyNicknameResult.SUCCESS
                : ModifyNicknameResult.FAILURE;
    }

    public ModifyPasswordResult modifyPassword(String password, UserEntity user, HttpSession session) {

        if (!(session.getAttribute("user") instanceof UserEntity)) {
            return ModifyPasswordResult.FAILURE; //세션에서 user를 가져옴, 로그인 안 함
        }
        UserEntity signedUser = (UserEntity) session.getAttribute("user");
        password = CryptoUtil.hashSha512(password);
        if (password.equals(signedUser.getPassword())) {
            return ModifyPasswordResult.FAILURE_PASSWORD_MISMATCH; // 입력한 기존 비밀번호가 현재 비밀번호랑 다름
        }
        user.setPassword(password);
        return this.myPageMapper.updatePassword(user) > 0
                ? ModifyPasswordResult.SUCCESS : ModifyPasswordResult.FAILURE;
    }
    public boolean deleteIndex(int index) {
        return this.myPageMapper.deleteByIndex(index) > 0;
    }
    public boolean deleteMyPick(int index) {
        return this.myPageMapper.deleteByMyPick(index) > 0;
    }

    //   -------------------------------------------------------------------------------------------------

    public SendDeleteContactCodeResult deleteContactCodeResult(WithDrawContactCodeEntity withDrawContactCode, HttpSession session) {

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        if (withDrawContactCode.getContact() == null ||
                !withDrawContactCode.getContact().matches("^(010)(\\d{8})$")) {
            return SendDeleteContactCodeResult.FAILURE;
        }
        String loggedInUserContact = loggedInUser.getContact();
        String withDrawContact = withDrawContactCode.getContact();

        UserEntity user = this.myPageMapper.selectUserByContact(loggedInUserContact);
        if (user == null || !loggedInUserContact.equals(withDrawContact)) {
            return SendDeleteContactCodeResult.FAILURE;
        }

        withDrawContactCode
                .setCode(RandomStringUtils.randomNumeric(6))
                .setSalt(CryptoUtil.hashSha512(String.format("%s%s%f%f",
                        withDrawContactCode.getCode(),
                        withDrawContactCode.getContact(),
                        Math.random(),
                        Math.random())))
                .setCreatedAt(new Date())
                .setExpiresAt(DateUtils.addMinutes(withDrawContactCode.getCreatedAt(), 5))
                .setExpired(false);
        NCloudUtil.sendSms(withDrawContactCode.getContact(), String.format("[오마코스 회원 탈퇴] 인증번호 [%s]를 입력해 주세요.", withDrawContactCode.getCode()));

        return this.myPageMapper.insertWithDrawContactCodes(withDrawContactCode) > 0
                ? SendDeleteContactCodeResult.SUCCESS
                : SendDeleteContactCodeResult.FAILURE;
    }

    public VerifyDeleteContactCodeResult verifyDeleteContactCodeResult(WithDrawContactCodeEntity withDrawContactCode) {

        if (withDrawContactCode == null ||
                withDrawContactCode.getContact() == null ||
                withDrawContactCode.getCode() == null ||
                withDrawContactCode.getSalt() == null ||
                !withDrawContactCode.getContact().matches("^(010\\d{8})$") ||
                !withDrawContactCode.getCode().matches("^(\\d{6})$") ||
                !withDrawContactCode.getSalt().matches("^([\\da-f]{128})$")) {
            return VerifyDeleteContactCodeResult.FAILURE; //여기서오류터짐
        }

        withDrawContactCode = this.myPageMapper.selectRecoverContactCodeByContactCodeSalt(withDrawContactCode);
        if (withDrawContactCode == null) {
            return VerifyDeleteContactCodeResult.FAILURE;
        }
        if (new Date().compareTo(withDrawContactCode.getExpiresAt()) > 0) {
            return VerifyDeleteContactCodeResult.FAILURE_EXPIRED;
        }
        withDrawContactCode.setExpired(true);
        return this.myPageMapper.updateWithDrawContactCode(withDrawContactCode) > 0
                ? VerifyDeleteContactCodeResult.SUCCESS
                : VerifyDeleteContactCodeResult.FAILURE_EXPIRED;
    }

    public DeleteUserResult deleteUserResult(WithDrawContactCodeEntity withDrawContactCode, HttpSession session) {

        UserEntity loggedInUser = (UserEntity) session.getAttribute("user");

        // 인증되지 않은 경우
        if (loggedInUser == null) {
            return DeleteUserResult.FAILURE;
        }

        // 인증된 경우 사용자 삭제 수행
        if (verifyDeleteContactCodeResult(withDrawContactCode) == VerifyDeleteContactCodeResult.SUCCESS) {
            return this.myPageMapper.deleteWithDrawUser(loggedInUser) > 0
                    ? DeleteUserResult.SUCCESS
                    : DeleteUserResult.FAILURE;
        }

        return DeleteUserResult.FAILURE;
    }

    //   -------------------------------------------------------------------------------------------------
    public PlannerVo readPlan(int index) {
        return myPageMapper.selectIndexPlan(index);
    }

    // 해당 tourIndex를 기준으로 세부 일정 정보를 조회하는 메서드
//    public List<DetailPlanEntity> getDetailPlansByTourIndex(int tourIndex) {
//        return myPageMapper.selectDetailPlansByTourIndex(tourIndex);
//    }
    public List<DetailPlanVo> getDetailPlansByTourIndex(int tourIndex) {
        System.out.println("tourIndex : " + tourIndex);

        return myPageMapper.selectDetailPlansByTourIndex(tourIndex);
    }


    public Map<Integer, List<DetailPlanVo>> groupDetailPlansByDay(List<DetailPlanVo> detailPlans) {
        Map<Integer, List<DetailPlanVo>> groupedDetailPlans = new HashMap<>();

        for (DetailPlanVo detailPlan : detailPlans) {
            int day = detailPlan.getDay();
            List<DetailPlanVo> dayDetailPlans = groupedDetailPlans.getOrDefault(day, new ArrayList<>());

//            if (detailPlan.getPlaceImg() != null && !detailPlan.getPlaceImg().isEmpty()) {
            dayDetailPlans.add(detailPlan);
//            }

            groupedDetailPlans.put(day, dayDetailPlans);
        }

        for (DetailPlanVo detailPlan : detailPlans) {
            System.out.println("들어갔나 "+detailPlan); // 또는 원하는 속성들을 선택적으로 출력
        }
        return groupedDetailPlans;
    }

    public boolean updateMemoService(int index, String memo){
        return this.myPageMapper.updateByMemo(index, memo) > 0;
    }

}


//        if (password.equals(user.getPassword())) {
//            return ModifyPasswordResult.FAILURE_DUPLICATE_PASSWORD; // 입력한 기존 비밀번호가 새 비밀번호랑 같음
//        }


