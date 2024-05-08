package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.*;
import com.ohmycouse.teampractice.enums.*;
import com.ohmycouse.teampractice.entities.RecoverEmailCodeEntity;
import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.mappers.MyPageMapper;
import com.ohmycouse.teampractice.mappers.UserMapper;
import com.ohmycouse.teampractice.utils.CryptoUtil;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.ohmycouse.teampractice.entities.RecoverContactCodeEntity;
import com.ohmycouse.teampractice.utils.NCloudUtil;
import org.springframework.stereotype.Controller;
import com.ohmycouse.teampractice.entities.RegisterContactCodeEntity;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.*;
import java.util.Date;

@Service
public class UserService {

    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine springTemplateEngine;
    private final UserMapper userMapper;
    private static final Map<String, UserEntity> person = new HashMap<>(); // static




    public UserService(JavaMailSender javaMailSender, SpringTemplateEngine springTemplateEngine, UserMapper userMapper) {
        this.javaMailSender = javaMailSender;
        this.springTemplateEngine = springTemplateEngine;
        this.userMapper = userMapper;

    }



    public VerifyRecoverEmailCodeResult verifyRecoverEmailCodeResult(RecoverEmailCodeEntity recoverEmailCode) {
        if (recoverEmailCode == null ||
                recoverEmailCode.getEmail() == null ||
                recoverEmailCode.getCode() == null ||
                recoverEmailCode.getSalt() == null) {
            return VerifyRecoverEmailCodeResult.FAILURE;
        }
        recoverEmailCode = this.userMapper.selectRecoverEmailCodeByEmailCodeSalt(recoverEmailCode);
        if(recoverEmailCode == null) {
            return VerifyRecoverEmailCodeResult.FAILURE;
        }
        if (new Date().compareTo(recoverEmailCode.getExpiresAt()) > 0) {
            return VerifyRecoverEmailCodeResult.FAILURE_EXPIRED;
        }
        recoverEmailCode.setExpired(true);
        return this.userMapper.updateRecoverEmailCode(recoverEmailCode) > 0
                ? VerifyRecoverEmailCodeResult.SUCCESS
                : VerifyRecoverEmailCodeResult.FAILURE;
    }
    public RecoverPasswordResult recoverPassword(RecoverEmailCodeEntity recoverEmailCode, UserEntity user) {
        if(recoverEmailCode == null ||
        recoverEmailCode.getEmail() == null ||
        recoverEmailCode.getCode() == null ||
        recoverEmailCode.getSalt() == null ||
                user == null ||
                user.getPassword() == null) {
            return  RecoverPasswordResult.FAILURE;
        }
        recoverEmailCode = this.userMapper.selectRecoverEmailCodeByEmailCodeSalt(recoverEmailCode);
        if (recoverEmailCode == null || !recoverEmailCode.isExpired()) {
            return RecoverPasswordResult.FAILURE;
        }
        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        return this.userMapper.updateUser(user) > 0 && this.userMapper.deleteRecoverEmailCode(recoverEmailCode) > 0
                ? RecoverPasswordResult.SUCCESS
                : RecoverPasswordResult.FAILURE;
    }

    public SendRecoverEmailCodeResult sendRecoverEmailCodeResult(RecoverEmailCodeEntity recoverEmailCode) throws MessagingException {
        if(recoverEmailCode == null ||
        recoverEmailCode.getEmail() == null) {
            return SendRecoverEmailCodeResult.FAILURE;
        }
        if (this.userMapper.selectUserByEmail(recoverEmailCode.getEmail()) == null) {
            return SendRecoverEmailCodeResult.FAILURE;
        }
        recoverEmailCode
                .setCode(RandomStringUtils.randomAlphanumeric(6))
                .setSalt(CryptoUtil.hashSha512(String.format("%s%s%f%f",
                        recoverEmailCode.getCode(),
                        recoverEmailCode.getEmail(),
                        Math.random(),
                        Math.random())))
                .setCreatedAt(new Date())
                .setExpiresAt(DateUtils.addHours(recoverEmailCode.getCreatedAt(), 1))
                .setExpired(false);
        String url = String.format("https://omcs.computist-yeo.com/user/recoverpwpage?email=%s&code=%s&salt=%s",
                recoverEmailCode.getEmail(),
                recoverEmailCode.getCode(),
                recoverEmailCode.getSalt());
        Context context = new Context();
        context.setVariable("url", url);

        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setSubject("[오마코스 비밀번호 재설정] 이메일 인증");
        mimeMessageHelper.setFrom("yeo328@yu.ac.kr");
        mimeMessageHelper.setTo(recoverEmailCode.getEmail());
        mimeMessageHelper.setText(this.springTemplateEngine.process("_recoverEmail", context), true);
        this.javaMailSender.send(mimeMessage);

        return this.userMapper.insertRecoverEmailCode(recoverEmailCode) > 0
                ? SendRecoverEmailCodeResult.SUCCESS
                : SendRecoverEmailCodeResult.FAILURE;
    }

    public UserEntity selectUserByEmail(String email) {
        return person.get(email);
    }

    public LoginResult login(UserEntity user) {
        UserEntity existingUser = this.userMapper.selectUserByEmail(user.getEmail());
        if (existingUser == null) {
            return LoginResult.FAILURE;
        }
        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        if (!user.getPassword().equals(existingUser.getPassword())) {
            return LoginResult.FAILURE;
        }
        user.setNickname(existingUser.getNickname())
                .setContact(existingUser.getContact())
                .setStatus(existingUser.getStatus())
                .setAdmin(existingUser.getAdmin())
                .setRegisteredAt(existingUser.getRegisteredAt());
        if (user.getStatus().equals("DELETED")) {
            return LoginResult.FAILURE;
        }
        if (user.getStatus().equals("EMAIL_PENDING")) {
            return LoginResult.FAILURE_EMAIL_NOT_VERIFIED;
        }
        if (user.getStatus().equals("SUSPENDED")) {
            return LoginResult.FAILURE_SUSPENDED;
        }
        return LoginResult.SUCCESS;
    }

    public SendRecoverContactCodeResult sendRecoverContactCodeResult(RecoverContactCodeEntity recoverContactCode) {
        if (recoverContactCode.getContact() == null ||
                !recoverContactCode.getContact().matches("^(010)(\\d{8})$")) {
            return SendRecoverContactCodeResult.FAILURE;
        }
        if (this.userMapper.selectUserByContact(recoverContactCode.getContact()) == null) {
            return SendRecoverContactCodeResult.FAILURE;
        }
        recoverContactCode
                .setCode(RandomStringUtils.randomNumeric(6))
                .setSalt(CryptoUtil.hashSha512(String.format("%s%s%f%f",
                        recoverContactCode.getCode(),
                        recoverContactCode.getContact(),
                        Math.random(),
                        Math.random())))
                .setCreatedAt(new Date())
                .setExpiresAt(DateUtils.addMinutes(recoverContactCode.getCreatedAt(), 5))
                .setExpired(false);
        NCloudUtil.sendSms(recoverContactCode.getContact(), String.format("[오마코스 이메일 찾기] 인증번호 [%s]를 입력해 주세요.", recoverContactCode.getCode()));
        return this.userMapper.insertRecoverContactCode(recoverContactCode) > 0
                ? SendRecoverContactCodeResult.SUCCESS
                : SendRecoverContactCodeResult.FAILURE;
    }

    public RegisterResult register(UserEntity user, RegisterContactCodeEntity registerContactCode) throws MessagingException {
        if (this.userMapper.selectUserByEmail(user.getEmail()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if (this.userMapper.selectUserByNickname(user.getNickname()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_EMAIL;
        }
        if (this.userMapper.selectUserByContact(user.getContact()) != null) {
            return RegisterResult.FAILURE_DUPLICATE_CONTACT;
        }

        registerContactCode = this.userMapper.selectRegisterContactCodeByContactCodeSalt(registerContactCode);
        if (registerContactCode == null || !registerContactCode.isExpired()) {
            return RegisterResult.FAILURE;
        }

        user.setPassword(CryptoUtil.hashSha512(user.getPassword()));
        user.setStatus("EMAIL_PENDING"); //이메일 인증 안된상태 //이메일인증 안할거면 OK
        user.setAdmin(false);

//        email 인증 보내기
        RegisterEmailCodeEntity registerEmailCode = new RegisterEmailCodeEntity();
        registerEmailCode.setEmail(user.getEmail());
        registerEmailCode.setCode(RandomStringUtils.randomAlphanumeric(6));
        registerEmailCode.setSalt(CryptoUtil.hashSha512(String.format("%s%s%f%f",
                registerEmailCode.getEmail(),
                registerEmailCode.getCode(),
                Math.random(),
                Math.random())));
        registerEmailCode.setCreatedAt(new Date());
        registerEmailCode.setExpiresAt(DateUtils.addHours(registerContactCode.getCreatedAt(),1));
        registerEmailCode.setExpired(false);

        String url = String.format("http://localhost:6795/user/emailCode?email=%s&code=%s&salt=%s",
            registerEmailCode.getEmail(),
            registerEmailCode.getCode(),
            registerEmailCode.getSalt());

        //흠 무슨 클래스지
        Context context = new Context();
        context.setVariable("url", url);

        //흠 뭐하는 클래스지
        MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setSubject("[OMCS 회원가입] 이메일 인증");
        mimeMessageHelper.setFrom("yeo328@yu.ac.kr");
        mimeMessageHelper.setTo(user.getEmail());
        mimeMessageHelper.setText(this.springTemplateEngine.process("_registerEmail", context), true);
        this.javaMailSender.send(mimeMessage);

        return this.userMapper.insertUser(user) > 0 && this.userMapper.insertRegisterEmailCode(registerEmailCode) > 0
                ? RegisterResult.SUCCESS : RegisterResult.FAILURE;
    }

    public SendRegisterContactCodeResult sendRegisterContactCode(RegisterContactCodeEntity registerContactCode) {
        if (registerContactCode == null ||
                registerContactCode.getContact() == null ||
                !registerContactCode.getContact().matches("^(010)(\\d{8})$")) {
            return SendRegisterContactCodeResult.FAILURE;
        }
        if (this.userMapper.selectUserByContact(registerContactCode.getContact()) != null) {
            return SendRegisterContactCodeResult.FAILURE_DUPLICATE;
        }
        String code = RandomStringUtils.randomNumeric(6);
        String salt = CryptoUtil.hashSha512(String.format("%s%s%f%f",
                registerContactCode.getContact(),
                code,
                Math.random(),
                Math.random()));
        Date createdAt = new Date();
        Date expiresAt = DateUtils.addMinutes(createdAt, 5);
        registerContactCode.setCode(code)
                .setSalt(salt)
                .setCreatedAt(createdAt)
                .setExpiresAt(expiresAt)
                .setExpired(false);
        NCloudUtil.sendSms(registerContactCode.getContact(), String.format("[오마코스] 인증번호[%s]를 입력해 주세요.", registerContactCode.getCode()));
        return this.userMapper.insertRegisterContactCode(registerContactCode) > 0
                ? SendRegisterContactCodeResult.SUCCESS
                : SendRegisterContactCodeResult.FAILURE;
    }
    public VerifyRegisterContactCodeResult verifyRegisterContactCode(RegisterContactCodeEntity registerContactCode) {
        System.out.println("서비스 연락처"+registerContactCode.getContact());
        System.out.println("서비스 코드"+registerContactCode.getCode());
        System.out.println("서비스 salt"+registerContactCode.getSalt());
        RegisterContactCodeEntity existingRegisterCodeEntity = this.userMapper.selectRegisterContactCodeByContactCodeSalt(
                registerContactCode.getContact(),
                registerContactCode.getCode(),
                registerContactCode.getSalt()
        );
        System.out.println("서비스"+existingRegisterCodeEntity);
        if (existingRegisterCodeEntity == null) {
            return VerifyRegisterContactCodeResult.FAILURE;
        }
        Date currentDate = new Date();
        if (currentDate.compareTo(existingRegisterCodeEntity.getExpiresAt()) > 0) {
            System.out.println(currentDate);
            return VerifyRegisterContactCodeResult.FAILURE_EXPIRED;
        }
        existingRegisterCodeEntity.setExpired(true); //true 주고 만료시킴
        // 인증했다
        return this.userMapper.updateRegisterContactCode(existingRegisterCodeEntity) > 0
                ? VerifyRegisterContactCodeResult.SUCCESS
                : VerifyRegisterContactCodeResult.FAILURE;
    }

    public VerifyRegisterEmailCodeResult verifyRegisterEmailCode(RegisterEmailCodeEntity registerEmailCode) {
        if (registerEmailCode.getEmail() == null ||
                registerEmailCode.getCode() == null ||
                registerEmailCode.getSalt() == null ||
                !registerEmailCode.getEmail().matches("^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$") ||
                !registerEmailCode.getCode().matches("^([\\da-zA-Z]{6})$") ||
                !registerEmailCode.getSalt().matches("^([\\da-f]{128})")) {
            System.out.println("registerEmailCode.getEmail : "+registerEmailCode.getEmail());
            System.out.println("registerEmailCode.getCode() : " + registerEmailCode.getCode());
            System.out.println("registerEmailCode.getSalt() : " + registerEmailCode.getSalt());
            System.out.println("Email.matches : " +
                    !registerEmailCode.getEmail().matches("^(?=.{10,50}$)([\\da-zA-Z\\-_\\.]{5,25})@([\\da-z][\\da-z\\-]*[\\da-z]\\.)?([\\da-z][\\da-z\\-]*[\\da-z])\\.([a-z]{2,15})(\\.[a-z]{2})?$")
            );
            System.out.println("getCode().matches 가져온코드랑 매치 : " +!registerEmailCode.getCode().matches("^([\\da-zA-Z]{6})$")
            );
            System.out.println("ip 검사 : " + !registerEmailCode.getSalt().matches("^([\\da-f]{128})"));
            return VerifyRegisterEmailCodeResult.FAILURE;
        }
        RegisterEmailCodeEntity existingRegisterEmailCodeEntity = this.userMapper.selectRegisterEmailCodeByEmailCodeSalt(registerEmailCode.getEmail(), registerEmailCode.getCode(), registerEmailCode.getSalt());
        System.out.println(registerEmailCode.getEmail());
        System.out.println(registerEmailCode.getCode());
        System.out.println(registerEmailCode.getSalt());
        if (existingRegisterEmailCodeEntity == null) {
            System.out.println("나야");
            return VerifyRegisterEmailCodeResult.FAILURE;
        }
        if (new Date().compareTo(existingRegisterEmailCodeEntity.getExpiresAt()) > 0) {
            return VerifyRegisterEmailCodeResult.FAILURE_EXPIRED;
        }
        existingRegisterEmailCodeEntity.setExpired(true);
        this.userMapper.updateRegisterEmailCode(existingRegisterEmailCodeEntity);

        int updateEmailResult = this.userMapper.updateRegisterEmailCode(existingRegisterEmailCodeEntity);
        UserEntity user = this.userMapper.selectUserByEmail(registerEmailCode.getEmail());
        user.setStatus("OKAY");
        int updateUserResult = this.userMapper.RegisterUpdateUser(user);

        return updateEmailResult > 0 || updateUserResult > 0
                ? VerifyRegisterEmailCodeResult.SUCCESS
                : VerifyRegisterEmailCodeResult.FAILURE;
    }
    public VerifyRecoverContactCodeResult verifyRecoverContactCodeResult(RecoverContactCodeEntity recoverContactCode) {
        if (recoverContactCode == null ||
                recoverContactCode.getContact() == null ||
                recoverContactCode.getCode() == null ||
                recoverContactCode.getSalt() == null /*||
                !recoverContactCode.getContact().matches("^(010\\d{8})$") ||
                !recoverContactCode.getCode().matches("^(\\d{6})$") ||
                !recoverContactCode.getSalt().matches("^([\\da-f]{128})$")*/) {
            System.out.println(recoverContactCode == null);
            System.out.println(recoverContactCode.getContact() == null);
            System.out.println(recoverContactCode.getCode() == null);
            System.out.println(recoverContactCode.getSalt() == null);
            System.out.println("null111");
            return VerifyRecoverContactCodeResult.FAILURE;
        }
        recoverContactCode = this.userMapper.selectRecoverContactCodeByContactCodeSalt(recoverContactCode.getContact(), recoverContactCode.getCode(), recoverContactCode.getSalt());
        if (recoverContactCode == null) {
            System.out.println("null2222");
            return VerifyRecoverContactCodeResult.FAILURE;
        }
        if (new Date().compareTo(recoverContactCode.getExpiresAt()) > 0) {
            System.out.println("Date3333");
            return VerifyRecoverContactCodeResult.FAILURE_EXPIRED;
        }
        recoverContactCode.setExpired(true);
        return this.userMapper.updateRecoverContactCode(recoverContactCode) > 0
                ? VerifyRecoverContactCodeResult.SUCCESS
                : VerifyRecoverContactCodeResult.FAILURE;
    }

    public UserEntity getUserByContact(String contact) {
        return this.userMapper.selectUserByContact(contact);
    }
}
