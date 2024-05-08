package com.ohmycouse.teampractice.mappers;

import com.ohmycouse.teampractice.entities.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    RecoverContactCodeEntity selectRecoverContactCodeByContactCodeSalt(@Param(value = "contact") String contact, @Param(value = "code") String code, @Param(value = "salt") String salt);
    int insertUser(UserEntity user);

    int insertRegisterEmailCode(RegisterEmailCodeEntity registerEmailCode);
    int insertRegisterContactCode(RegisterContactCodeEntity registerContactCode);
    int insertRecoverContactCode(RecoverContactCodeEntity recoverContactCode);
    int insertRecoverEmailCode(RecoverEmailCodeEntity recoverEmailCode);

    int updateRegisterContactCode(RegisterContactCodeEntity registerContactCodeEntity);
    int updateRecoverContactCode(RecoverContactCodeEntity recoverContactCode);
    int updateRecoverEmailCode(RecoverEmailCodeEntity recoverEmailCode);
    int updateUser(UserEntity user);
    int RegisterUpdateUser(UserEntity user);

    int updateRegisterEmailCode(RegisterEmailCodeEntity registerEmailCode);


    UserEntity selectUserByEmail(@Param(value="email") String email);
    UserEntity selectUserByNickname(@Param(value="nickname") String nickname);
    UserEntity selectUserByContact(@Param(value="contact") String contact);
    RegisterContactCodeEntity selectRegisterContactCodeByContactCodeSalt(@Param(value = "contact") String contact,
                                                                         @Param(value = "code") String code,
                                                                         @Param(value = "salt") String salt);
    RegisterContactCodeEntity selectRegisterContactCodeByContactCodeSalt(RegisterContactCodeEntity registerContactCode);
    RegisterEmailCodeEntity selectRegisterEmailCodeByEmailCodeSalt(@Param(value = "email")String email,
                                                                   @Param(value = "code") String code,
                                                                   @Param(value = "salt") String salt);

    RecoverEmailCodeEntity selectRecoverEmailCodeByEmailCodeSalt(RecoverEmailCodeEntity recoverEmailCode);


    int deleteRecoverEmailCode(RecoverEmailCodeEntity recoverEmailCode);

    List<UserEntity> selectAdmin();

    int deleteUserAdmin(@Param(value = "email") String email);

    int updateByUserAdmin(@Param(value = "contact") String contact,
                          @Param(value = "changeContact") String changeContact);

}
