package com.cn.demo.dao;

import com.cn.demo.dto.UserInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserDao {
    UserInfo findById(@Param("id") String id);
}
