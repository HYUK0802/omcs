package com.ohmycouse.teampractice.services;

import com.ohmycouse.teampractice.entities.UserEntity;
import com.ohmycouse.teampractice.mappers.UserMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    private final UserMapper userMapper;

    @Autowired
    public AdminService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<UserEntity> readUser(){
        return userMapper.selectAdmin();
    }

    public boolean deleteUserAdmin(String email){
        return this.userMapper.deleteUserAdmin(email) > 0;
    }

    public boolean updateByUserAdmin(String contact, String changeContact){
        return this.userMapper.updateByUserAdmin(contact, changeContact) > 0;
    }

}
