package com.cn.demo.service;

import com.cn.demo.dao.UserDao;
import com.cn.demo.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;

    public UserInfo getUser(String id) {
        return userDao.findById(id);
    }
}
